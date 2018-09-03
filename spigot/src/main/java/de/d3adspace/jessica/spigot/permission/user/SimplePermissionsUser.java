package de.d3adspace.jessica.spigot.permission.user;

import de.d3adspace.jessica.core.permission.group.PermissionsGroup;
import de.d3adspace.jessica.core.permission.user.PermissionsUser;
import de.d3adspace.jessica.spigot.permission.container.AbstractPermissionsContainer;
import de.d3adspace.jessica.spigot.permission.group.GroupComparator;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class SimplePermissionsUser extends AbstractPermissionsContainer implements PermissionsUser {

    private final UUID uniqueId;
    private final List<PermissionsGroup> permissionsGroups;

    public SimplePermissionsUser(UUID uniqueId, Map<String, Boolean> permissions, List<PermissionsGroup> permissionsGroups) {
        super(permissions);
        this.uniqueId = uniqueId;
        this.permissionsGroups = permissionsGroups;
    }

    @Override
    public UUID getUniqueId() {

        return uniqueId;
    }

    @Override
    public List<PermissionsGroup> getPermissionGroups() {

        return permissionsGroups;
    }

    @Override
    public void addGroup(PermissionsGroup permissionsGroup) {

        permissionsGroups.add(permissionsGroup);
    }

    @Override
    public void removeGroup(PermissionsGroup permissionsGroup) {

        permissionsGroups.remove(permissionsGroup);
    }

    @Override
    public boolean isInGroup(PermissionsGroup permissionsGroup) {

        // Search for group name
        return permissionsGroups.stream()
                .map(PermissionsGroup::getName)
                .anyMatch(groupName -> groupName.equalsIgnoreCase(permissionsGroup.getName()));
    }

    @Override
    public Map<String, Boolean> getEffectivePermissions() {

        // Assemble the group permissions
        Map<String, Boolean> permissions = assembleGroupPermissions();

        // Overwrite group permissions with user permissions
        Map<String, Boolean> userPermissions = getPermissions();
        permissions.putAll(userPermissions);

        return permissions;
    }

    /**
     * Assemble the group permissions.
     *
     * @return The effective group permissions.
     */
    private Map<String, Boolean> assembleGroupPermissions() {

        // Sort groups by priority
        permissionsGroups.sort(new GroupComparator());

        return assembleEffectivePermissions(permissionsGroups);
    }
}
