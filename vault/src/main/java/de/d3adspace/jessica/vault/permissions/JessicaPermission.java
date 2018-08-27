package de.d3adspace.jessica.vault.permissions;

import de.d3adspace.jessica.core.PermissionsManager;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
@Singleton
public class JessicaPermission extends Permission {

    private static final String NAME = "JessicaPermissions";
    private static final boolean SUPER_PERMS_COMPAT = false;
    private static final boolean GROUP_SUPPORT = false;

    /**
     * Jessica's internal permissions manager.
     */
    private final PermissionsManager permissionsManager;

    /**
     * The plugin instance.
     */
    private final Plugin plugin;

    /**
     * Create a new jessica permission instance.
     *
     * @param permissionsManager The underlying permissions manager.
     * @param plugin             The plugin instance.
     */
    @Inject
    public JessicaPermission(PermissionsManager permissionsManager, Plugin plugin) {
        this.permissionsManager = permissionsManager;
        this.plugin = plugin;
    }

    @Override
    public String getName() {

        return NAME;
    }

    @Override
    public boolean isEnabled() {

        return plugin.isEnabled();
    }

    @Override
    public boolean hasSuperPermsCompat() {

        return SUPER_PERMS_COMPAT;
    }

    @Override
    public boolean playerHas(String world, String playerName, String permission) {

        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return false;
        }

        return player.hasPermission(permission);
    }

    @Override
    public boolean playerAdd(String world, String playerName, String permission) {

        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return false;
        }

        return permissionsManager.addPermission(player.getUniqueId(), permission);
    }

    @Override
    public boolean playerRemove(String world, String playerName, String permission) {

        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return false;
        }

        return permissionsManager.removePermission(player.getUniqueId(), permission);
    }

    @Override
    public boolean groupHas(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean groupAdd(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean groupRemove(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean playerInGroup(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean playerAddGroup(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean playerRemoveGroup(String s, String s1, String s2) {
        return false;
    }

    @Override
    public String[] getPlayerGroups(String s, String s1) {
        return new String[0];
    }

    @Override
    public String getPrimaryGroup(String s, String s1) {
        return null;
    }

    @Override
    public String[] getGroups() {
        return new String[0];
    }

    @Override
    public boolean hasGroupSupport() {
        return GROUP_SUPPORT;
    }
}
