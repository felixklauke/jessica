package de.d3adspace.jessica.core.permission;

import de.d3adspace.jessica.core.exception.GroupNotFoundException;
import de.d3adspace.jessica.core.permission.group.PermissionsGroup;
import de.d3adspace.jessica.core.permission.status.PermissionStatus;
import de.d3adspace.jessica.core.permission.user.PermissionsUser;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface PermissionsManager {

    /**
     * Get a permissions user by its unique id.
     * <p>
     * This will create a basic user when the user doesn't exist.
     *
     * @param uniqueId The unique id.
     *
     * @return The permissions user.
     */
    PermissionsUser getUser(UUID uniqueId);

    /**
     * Get a permissions group by its unique name.
     *
     * @param groupName The groups name.
     *
     * @return The permissions group.
     */
    PermissionsGroup getGroup(String groupName) throws GroupNotFoundException;

    /**
     * Get all existent permissions groups out there.
     *
     * @return The permissions groups.
     */
    List<PermissionsGroup> getGroups();

    /**
     * Set the given permission to the given status for the given user.
     *
     * @param permissionsUser  The permissions user.
     * @param permission       The permission.
     * @param permissionStatus The status of the permission.
     */
    void setPermission(PermissionsUser permissionsUser, String permission, PermissionStatus permissionStatus);

    /**
     * Set the given permission to the given status for the given group.
     *
     * @param permissionsGroup The permissions group.
     * @param permission       The permission.
     * @param permissionStatus The status of the permission.
     */
    void setGroupPermission(PermissionsGroup permissionsGroup, String permission, PermissionStatus permissionStatus);

    /**
     * Add the given permissions user to the given permissions group.
     *
     * @param permissionsUser  The permissions user.
     * @param permissionsGroup The permissions group.
     */
    void addUserGroup(PermissionsUser permissionsUser, PermissionsGroup permissionsGroup);

    /**
     * Remove the given permissions user from the given permissions group.
     *
     * @param permissionsUser  The permissions user.
     * @param permissionsGroup The permissions group.
     */
    void removeUserGroup(PermissionsUser permissionsUser, PermissionsGroup permissionsGroup);

    /**
     * Create a new permissions group.
     *
     * @param name         The name of the group.
     * @param priority     The priority.
     * @param parentGroups The parent groups.
     * @param metadata     The metadata.
     * @param permissions  The permissions.
     */
    void addGroup(String name, int priority, List<PermissionsGroup> parentGroups, Map<String, Object> metadata, Map<String, Boolean> permissions);

    /**
     * Delete the group with the given name.
     *
     * @param name The name.
     */
    void removeGroup(String name);

    /**
     * Let the given permissions user begin a new session.
     *
     * @param uniqueId The permissions user.
     */
    void initSession(UUID uniqueId);

    /**
     * Destroy the session of the given permissions user.
     *
     * @param uniqueId The permissions user.
     */
    void destroySession(UUID uniqueId);
}
