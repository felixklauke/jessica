package de.d3adspace.jessica.spigot.permission;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import de.d3adspace.jessica.core.exception.GroupNotFoundException;
import de.d3adspace.jessica.core.permission.PermissionsManager;
import de.d3adspace.jessica.core.permission.group.PermissionsGroup;
import de.d3adspace.jessica.core.permission.status.PermissionStatus;
import de.d3adspace.jessica.core.permission.user.PermissionsUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Using this class can you abstract the whole business logic and let you custom manager only implement persistence and
 * data layer.
 * <p>
 * Must be in {@link Singleton} Scope to ensure data consistency of the {@link #permissionAttachments}.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
@Singleton
public abstract class AbstractPermissionsManager implements PermissionsManager {

    /**
     * The prefix for the internal mother permission.
     */
    private static final String PERMISSION_PREFIX = "jessica-player-permission-";

    /**
     * Contains all currently hold permission attachments.
     */
    private final Map<UUID, PermissionAttachment> permissionAttachments = Maps.newHashMap();

    /**
     * The plugin instance.
     */
    private final Plugin plugin;

    /**
     * The cache for all known permissions users.
     */
    private final LoadingCache<UUID, PermissionsUser> permissionsUserCache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(CacheLoader.from(this::loadUser));

    /**
     * The cache for all known permission groups.
     */
    private final LoadingCache<String, PermissionsGroup> permissionsGroupCache = CacheBuilder.newBuilder()
            .weakValues()
            .build(CacheLoader.from(this::loadGroup));

    /**
     * Create a new permissions manager by its underlying plugin.
     *
     * @param plugin The plugin.
     */
    public AbstractPermissionsManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public PermissionsUser getUser(UUID uniqueId) {

        try {
            return permissionsUserCache.get(uniqueId);
        } catch (ExecutionException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed loading user " + uniqueId, e);
        }

        return null;
    }

    @Override
    public PermissionsGroup getGroup(String groupName) {

        try {
            return permissionsGroupCache.get(groupName);
        } catch (GroupNotFoundException e) {
            throw new RuntimeException("Tried to load an unknown group: " + groupName, e);
        } catch (ExecutionException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed loading group " + groupName, e);
        }

        return null;
    }

    @Override
    public void setPermission(PermissionsUser permissionsUser, String permission, PermissionStatus permissionStatus) {

        permissionsUser.setPermission(permission, permissionStatus);

        // Refresh permissions
        refreshPermissions(permissionsUser);

        saveUser(permissionsUser);
    }

    @Override
    public void setGroupPermission(PermissionsGroup permissionsGroup, String permission, PermissionStatus permissionStatus) {

        permissionsGroup.setPermission(permission, permissionStatus);

        // Refresh group permissions for the corresponding players
        refreshGroupPermissions(permissionsGroup);

        saveGroup(permissionsGroup);
    }

    @Override
    public void addUserGroup(PermissionsUser permissionsUser, PermissionsGroup permissionsGroup) {

        permissionsUser.addGroup(permissionsGroup);

        // Refresh permissions
        refreshPermissions(permissionsUser);

        saveUser(permissionsUser);
    }

    @Override
    public void removeUserGroup(PermissionsUser permissionsUser, PermissionsGroup permissionsGroup) {

        permissionsUser.removeGroup(permissionsGroup);

        // Refresh permissions
        refreshPermissions(permissionsUser);

        saveUser(permissionsUser);
    }

    @Override
    public void initSession(UUID uniqueId) {

        // Check if player is online
        Player player = Bukkit.getPlayer(uniqueId);
        if (player == null) {
            throw new IllegalStateException("Can't begin session for a player that isn't online.");
        }

        // Build permission
        PermissionsUser permissionsUser = getUser(uniqueId);
        Map<String, Boolean> effectivePermissions = permissionsUser.getEffectivePermissions();

        String permissionName = PERMISSION_PREFIX + uniqueId;
        Permission permission = new Permission(permissionName, PermissionDefault.FALSE, effectivePermissions);

        // Let bukkit know about the permission so that the attachments can reference it
        plugin.getServer().getPluginManager().addPermission(permission);

        // Create permission attachment
        PermissionAttachment permissionAttachment = player.addAttachment(plugin, permission.getName(), true);

        // Recalculate player permissions
        player.recalculatePermissions();

        // Register attachment for session
        addPermissionAttachment(uniqueId, permissionAttachment);
    }

    @Override
    public void destroySession(UUID uniqueId) {

        // Check if there is a session for that unique id
        PermissionAttachment permissionAttachment = getPermissionAttachment(uniqueId);
        if (permissionAttachment == null) {
            throw new IllegalStateException("Can't destroy a non existent session.");
        }

        // Check if the corresponding player is online
        Player player = Bukkit.getPlayer(uniqueId);
        if (player == null) {
            throw new IllegalStateException("Can't destroy a session for a player that isn't online.");
        }

        // Destroy mother permission
        Permission permission = getMotherPermission(uniqueId);
        plugin.getServer().getPluginManager().removePermission(permission);

        // Evict permissions user from cache
        permissionsUserCache.invalidate(uniqueId);

        // Remove the attachment from the player
        player.removeAttachment(permissionAttachment);

        // Recalculate permissions for proper cleanup
        player.recalculatePermissions();

        // End the session by removing the permission attachment
        removePermissionAttachment(uniqueId, permissionAttachment);
    }

    /**
     * Load a user from an arbitrary backend.
     *
     * @param uniqueId The unique id of the user.
     *
     * @return The permissions user.
     */
    protected abstract PermissionsUser loadUser(UUID uniqueId);

    /**
     * Load a group from an arbitrary backed.
     *
     * @param groupName The name of the group.
     *
     * @return The permissions group.
     *
     * @throws GroupNotFoundException If no group with that name could be found.
     */
    protected abstract PermissionsGroup loadGroup(String groupName) throws GroupNotFoundException;

    /**
     * Save the given permissions user to an arbitrary backend.
     *
     * @param permissionsUser The user.
     */
    protected abstract void saveUser(PermissionsUser permissionsUser);

    /**
     * Save the given permissions group to an arbitrary backed.
     *
     * @param permissionsGroup The permissions group.
     */
    protected abstract void saveGroup(PermissionsGroup permissionsGroup);

    /**
     * Refresh the permissions of all permissions users with the given permissions group.
     *
     * @param permissionsGroup The permissions group.
     */
    private void refreshGroupPermissions(PermissionsGroup permissionsGroup) {

        // Filter applicable permissions users and refresh their permissions
        List<PermissionsUser> permissionsUsers = getOnlinePermissionsUsers();
        permissionsUsers.stream()
                .filter(permissionsUser -> permissionsUser.isInGroup(permissionsGroup))
                .forEach(this::refreshPermissions);
    }

    /**
     * Get all permissions user that are online and therefor hold a session.
     *
     * @return The online permissions users.
     */
    private List<PermissionsUser> getOnlinePermissionsUsers() {

        return Bukkit.getOnlinePlayers()
                .stream()
                .map(Entity::getUniqueId)
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    /**
     * Refresh the permissions of the given user.
     *
     * @param permissionsUser The permissions user.
     */
    private void refreshPermissions(PermissionsUser permissionsUser) {

        UUID uniqueId = permissionsUser.getUniqueId();

        // Get the mother permission
        Permission permission = getMotherPermission(uniqueId);

        // Check if the permission is available
        if (permission == null) {

            throw new IllegalStateException("Can't recalculate permissions for user " + uniqueId
                    + " as their is no corresponding permission available.");
        }

        // Get all effective permissions
        Map<String, Boolean> effectivePermissions = permissionsUser.getEffectivePermissions();

        // Overwrite permission children
        permission.getChildren().clear();
        permission.getChildren().putAll(effectivePermissions);

        // Recalculate permissibles to apply changes.
        permission.recalculatePermissibles();
    }

    /**
     * Get the mother permission of the player with the given unique id.
     *
     * @param uniqueId The unique id.
     *
     * @return The permission.
     */
    private Permission getMotherPermission(UUID uniqueId) {

        PluginManager pluginManager = plugin.getServer().getPluginManager();
        return pluginManager.getPermission(PERMISSION_PREFIX + uniqueId);
    }

    /**
     * Get the permission attachment for the given unique id.
     *
     * @param uniqueId The unique id.
     *
     * @return The permission attachment.
     */
    private PermissionAttachment getPermissionAttachment(UUID uniqueId) {

        return permissionAttachments.get(uniqueId);
    }

    /**
     * Register the given permission attachment for the given unique id.
     *
     * @param uniqueId             The unique id.
     * @param permissionAttachment The permission attachment.
     */
    private void addPermissionAttachment(UUID uniqueId, PermissionAttachment permissionAttachment) {

        permissionAttachments.put(uniqueId, permissionAttachment);
    }

    /**
     * Unregister the given permission attachment for the given unique id.
     *
     * @param uniqueId             The unique id.
     * @param permissionAttachment The permission attachment.
     */
    private void removePermissionAttachment(UUID uniqueId, PermissionAttachment permissionAttachment) {

        permissionAttachments.replace(uniqueId, permissionAttachment);
    }
}
