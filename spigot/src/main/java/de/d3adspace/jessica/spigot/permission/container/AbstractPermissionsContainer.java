package de.d3adspace.jessica.spigot.permission.container;

import com.google.common.collect.Maps;
import de.d3adspace.jessica.core.permission.container.PermissionsContainer;
import de.d3adspace.jessica.core.permission.status.PermissionStatus;

import java.util.List;
import java.util.Map;

/**
 * Abstraction of the {@link PermissionsContainer} that contains some helper methods like
 * {@link #setPermission(String, PermissionStatus)} and {@link #assembleEffectivePermissions(List)} to reduce
 * boiler plate code and lets container implementations focus on the important things.
 * <p>
 * The abstraction is built on {@link #permissions} where the permissions of the container are stored. It's still
 * up to the end user implementation to handle effective permissions and their sources. In most cases
 * {@link #assembleEffectivePermissions(List)} may be helpful when relying on other permission containers.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public abstract class AbstractPermissionsContainer implements PermissionsContainer {

    /**
     * The base permissions of the container.
     */
    private final Map<String, Boolean> permissions;

    /**
     * Create a new container based on the given base permissions.
     *
     * @param permissions The base permissions.
     */
    public AbstractPermissionsContainer(Map<String, Boolean> permissions) {
        this.permissions = permissions;
    }

    @Override
    public Map<String, Boolean> getPermissions() {

        return permissions;
    }

    @Override
    public void setPermission(String permission, PermissionStatus permissionStatus) {

        if (permissionStatus == PermissionStatus.NOT_SET) {
            permissions.remove(permission);
            return;
        }

        permissions.put(permission, permissionStatus == PermissionStatus.TRUE);
    }

    /**
     * Build effective permissions by letting multiple permissions containers overwrite.
     *
     * @param permissionsContainers The permissions containers.
     *
     * @return The effective Permissions.
     */
    protected Map<String, Boolean> assembleEffectivePermissions(List<? extends PermissionsContainer> permissionsContainers) {

        // Assemble the permissions by setting all permissions of the containers
        Map<String, Boolean> effectivePermissions = Maps.newHashMap();

        for (PermissionsContainer permissionsContainer : permissionsContainers) {

            // Overwrite effective permissions
            Map<String, Boolean> effectiveContainerPermissions = permissionsContainer.getEffectivePermissions();
            effectivePermissions.putAll(effectiveContainerPermissions);
        }

        return effectivePermissions;
    }
}
