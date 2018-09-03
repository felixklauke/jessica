package de.d3adspace.jessica.spigot.permission.group;

import de.d3adspace.jessica.core.permission.group.PermissionsGroup;

import java.util.Comparator;

/**
 * A {@link Comparator<PermissionsGroup>} that compares {@link PermissionsGroup} via their priority.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class GroupComparator implements Comparator<PermissionsGroup> {

    @Override
    public int compare(PermissionsGroup first, PermissionsGroup second) {

        return first.getPriority() - second.getPriority();
    }
}
