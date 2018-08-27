package de.d3adspace.jessica.spigot.permission;

import de.d3adspace.jessica.core.PermissionsManager;
import de.d3adspace.jessica.core.user.PermissionsUser;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.UUID;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public abstract class AbstractPermissionManager implements PermissionsManager {

    @Override
    public PermissionsUser getUser(UUID uniqueId) {
        return null;
    }

    @Override
    public boolean addPermission(UUID uniqueId, String permission) {

        // Modify user and set in config
        PermissionsUser permissionsUser = getUser(uniqueId);
        permissionsUser.getPermissions().put(permission, !permission.startsWith("-"));
        savePermissionsUser(permissionsUser);

        // Recalculate permissions
        Permission perm = Bukkit.getPluginManager().getPermission("jessicaPermissions-" + uniqueId);
        if (perm != null) {
            perm.getChildren().put(permission, !permission.startsWith("-"));
            Bukkit.getPlayer(uniqueId).recalculatePermissions();
        }

        return true;
    }

    @Override
    public boolean removePermission(UUID uniqueId, String permission) {

        PermissionsUser permissionsUser = getUser(uniqueId);
        permissionsUser.getPermissions().remove(permission);
        savePermissionsUser(permissionsUser);

        Permission perm = Bukkit.getPluginManager().getPermission("jessicaPermissions-" + uniqueId);
        if (perm != null) {
            perm.getChildren().remove(permission);
            perm.recalculatePermissibles();
            Bukkit.getPlayer(uniqueId).recalculatePermissions();
        }

        return true;
    }

    protected abstract void savePermissionsUser(PermissionsUser permissionsUser);
}
