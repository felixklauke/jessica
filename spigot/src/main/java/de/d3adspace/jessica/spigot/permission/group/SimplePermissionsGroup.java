package de.d3adspace.jessica.spigot.permission.group;

import de.d3adspace.jessica.core.permission.group.PermissionsGroup;
import de.d3adspace.jessica.spigot.permission.container.AbstractPermissionsContainer;

import java.util.List;
import java.util.Map;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class SimplePermissionsGroup extends AbstractPermissionsContainer implements PermissionsGroup {

    private final String name;
    private final int priority;
    private final List<PermissionsGroup> parentGroups;

    public SimplePermissionsGroup(String name, int priority, List<PermissionsGroup> parentGroups, Map<String, Boolean> permissions) {
        super(permissions);
        this.name = name;
        this.priority = priority;
        this.parentGroups = parentGroups;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public int getPriority() {

        return priority;
    }

    @Override
    public List<PermissionsGroup> getParentGroups() {

        return parentGroups;
    }

    @Override
    public Map<String, Boolean> getEffectivePermissions() {

        // Calculate the inherited permissions
        Map<String, Boolean> permissions = assembleInheritedPermissions();

        // Overwrite inherited group permissions with group local permissions
        Map<String, Boolean> userPermissions = getPermissions();
        permissions.putAll(userPermissions);

        return permissions;
    }

    /**
     * Assemble all permissions available from inheritance build by the {@link #parentGroups}.
     *
     * @return The effective permissions of the parent groups.
     */
    private Map<String, Boolean> assembleInheritedPermissions() {

        // Sort groups by priority for proper overwriting
        parentGroups.sort(new GroupComparator());

        return assembleEffectivePermissions(parentGroups);
    }
}
