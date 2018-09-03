package de.d3adspace.jessica.core.permission.container;

import de.d3adspace.jessica.core.permission.status.PermissionStatus;

import java.util.Map;

/**
 * Represents a unit that holds own permissions and probably effective permissions for example by inheritance.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface PermissionsContainer {

    /**
     * Get the permissions the container stores for itself.
     *
     * @return The permissions.
     */
    Map<String, Boolean> getPermissions();

    /**
     * Get the effective permissions of the container.
     *
     * @return The effective permissions.
     */
    Map<String, Boolean> getEffectivePermissions();

    /**
     * Set the given permission to the given status.
     *
     * @param permission       The permission.
     * @param permissionStatus The status of the permission.
     */
    void setPermission(String permission, PermissionStatus permissionStatus);
}
