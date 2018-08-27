package de.d3adspace.jessica.core.group;

import java.util.Map;

/**
 * Represents a permission group.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface PermissionsGroup {

    /**
     * Get the priority of the group.
     *
     * @return The priority.
     */
    int getPriority();

    /**
     * Get the name of the group.
     *
     * @return The name.
     */
    String getName();

    /**
     * Get the permissions of the group.
     *
     * @return The groups permissions.
     */
    Map<String, Boolean> getPermissions();
}
