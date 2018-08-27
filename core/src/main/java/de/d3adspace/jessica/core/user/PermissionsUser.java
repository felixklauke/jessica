package de.d3adspace.jessica.core.user;

import de.d3adspace.jessica.core.group.PermissionsGroup;

import java.util.Map;
import java.util.UUID;

/**
 * Represents a single user.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface PermissionsUser {

    /**
     * Get the unique id of the user.
     *
     * @return The unique id.
     */
    UUID getUniqueId();

    /**
     * Get the permissions group of the player.
     *
     * @return The group.
     */
    PermissionsGroup getGroup();

    /**
     * Get the permissions of the user itself without group permissions.
     *
     * @return The permissions.
     */
    Map<String, Boolean> getPermissions();

    /**
     * Get the effective permissions including user and group permissions.
     *
     * @return The effective permissions.
     */
    Map<String, Boolean> getEffectivePermissions();
}
