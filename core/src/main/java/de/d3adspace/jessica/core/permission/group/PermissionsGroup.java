package de.d3adspace.jessica.core.permission.group;

import de.d3adspace.jessica.core.permission.container.PermissionsContainer;

import java.util.List;

/**
 * Represents a group that acts as a container of permissions and can be applied to users to give them permissions
 * in a bulk. The group itself is still an entity that can have parent groups. When calculating the effective
 * permissions of a group, all parent groups are ordered by priority and their permissions overwrite. At the end
 * the "child" permissions overwrite all parents permissions.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface PermissionsGroup extends PermissionsContainer {

    /**
     * The name of the group - Should be unique.
     *
     * @return The name.
     */
    String getName();

    /**
     * The priority of the group used for ordering in inheritance.
     *
     * @return The priority of the group.
     */
    int getPriority();

    /**
     * The parent groups.
     * <p>
     * Note: May be unordered (by priority) - Always sort before using for inheritance.
     *
     * @return The parent groups.
     */
    List<PermissionsGroup> getParentGroups();
}
