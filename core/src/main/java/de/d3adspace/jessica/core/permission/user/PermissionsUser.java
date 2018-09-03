package de.d3adspace.jessica.core.permission.user;

import de.d3adspace.jessica.core.permission.container.PermissionsContainer;
import de.d3adspace.jessica.core.permission.group.PermissionsGroup;

import java.util.List;
import java.util.UUID;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface PermissionsUser extends PermissionsContainer {

    /**
     * Get the unique id of the permissions user.
     *
     * @return The unique id of the user.
     */
    UUID getUniqueId();

    /**
     * Get the permission groups of a permissions user.
     *
     * @return The permission groups.
     */
    List<PermissionsGroup> getPermissionGroups();

    /**
     * Add the permissions user to the given permissions group.
     *
     * @param permissionsGroup The permissions group.
     */
    void addGroup(PermissionsGroup permissionsGroup);

    /**
     * Remove the permissions user from the given permissions group.
     *
     * @param permissionsGroup The permissions group.
     */
    void removeGroup(PermissionsGroup permissionsGroup);

    /**
     * Check if the player is in the given permissions group.
     *
     * @param permissionsGroup The permissions group.
     *
     * @return If the player is in the permissions group.
     */
    boolean isInGroup(PermissionsGroup permissionsGroup);
}
