package de.d3adspace.jessica.spigot;

import de.d3adspace.jessica.core.PermissionsManager;
import de.d3adspace.jessica.core.user.PermissionsUser;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
@Singleton
public class JessicaApplicationImpl implements JessicaApplication {

    /**
     * The prefix of the general jessica parent permission.
     */
    private static final String PERMISSION_PREFIX = "jessicaPermissions-";

    /**
     * The plugin instance.
     */
    private final Plugin plugin;

    /**
     * The plugin manager.
     */
    private final PluginManager pluginManager;

    /**
     * The permissions manager.
     */
    private final PermissionsManager permissionsManager;

    @Inject
    public JessicaApplicationImpl(Plugin plugin, PluginManager pluginManager, PermissionsManager permissionsManager) {
        this.plugin = plugin;
        this.pluginManager = pluginManager;
        this.permissionsManager = permissionsManager;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void initPermissions(Player player) {

        String playerSpecificPermission = PERMISSION_PREFIX + player.getUniqueId();
        Permission permission = pluginManager.getPermission(playerSpecificPermission);

        PermissionsUser permissionsUser = permissionsManager.getUser(player.getUniqueId());
        Map<String, Boolean> effectivePermissions = permissionsUser.getEffectivePermissions();

        if (permission == null) {
            permission = new Permission(playerSpecificPermission, PermissionDefault.FALSE, effectivePermissions);
            pluginManager.addPermission(permission);
        } else {
            permission.getChildren().clear();
            permission.getChildren().putAll(effectivePermissions);
        }

        permission.recalculatePermissibles();

        if (!player.isPermissionSet(playerSpecificPermission) && !player.hasPermission(playerSpecificPermission)) {
            player.addAttachment(plugin, permission.getName(), true);
        }
    }

    @Override
    public void destroyPermissions(Player player) {

        String playerSpecificPermission = PERMISSION_PREFIX + player.getUniqueId();
        pluginManager.removePermission(playerSpecificPermission);
    }
}
