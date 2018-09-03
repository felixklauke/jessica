package de.d3adspace.jessica.spigot.permission.file.model.group;

import com.google.common.collect.Maps;
import de.d3adspace.jessica.core.permission.PermissionsManager;
import de.d3adspace.jessica.core.permission.group.PermissionsGroup;
import de.d3adspace.jessica.spigot.permission.group.SimplePermissionsGroup;
import de.d3adspace.jessica.spigot.utils.PermissionConversionUtils;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Persistence model for the {@link PermissionsGroup} built with Bukkit's {@link ConfigurationSerializable}. You may use
 * {@link #fromPermissionsGroup(PermissionsGroup)} and {@link #toPermissionsGroup(PermissionsManager)} for conversion
 * with business models.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
@SerializableAs("permissionsGroup")
public class PermissionsGroupModel implements ConfigurationSerializable {

    /**
     * The name of the group.
     */
    private final String name;

    /**
     * The priority of the group.
     */
    private final int priority;

    /**
     * Parent groups represented by their names.
     */
    private final List<String> parentGroups;

    /**
     * The permissions of the group in string notation.
     */
    private final List<String> permissions;

    /**
     * Create a new model by all its data - Use this with care and consider using
     * {@link #fromPermissionsGroup(PermissionsGroup)} instead to ensure correct data conversion.
     *
     * @param name         The name.
     * @param priority     The priority.
     * @param parentGroups The parent groups.
     * @param permissions  The permissions.
     */
    public PermissionsGroupModel(String name, int priority, List<String> parentGroups, List<String> permissions) {
        this.name = name;
        this.priority = priority;
        this.parentGroups = parentGroups;
        this.permissions = permissions;
    }

    /**
     * Factory method to create the persistence model from the business object.
     *
     * @param permissionsGroup The permissions group business model.
     *
     * @return The persistence model.
     */
    public static PermissionsGroupModel fromPermissionsGroup(PermissionsGroup permissionsGroup) {

        // Parent groups
        List<String> parentGroupNames = permissionsGroup.getParentGroups()
                .stream()
                .map(PermissionsGroup::getName)
                .collect(Collectors.toList());

        // Permissions
        List<String> permissions = PermissionConversionUtils.convertToStringNotation(permissionsGroup.getPermissions());

        return new PermissionsGroupModel(permissionsGroup.getName(), permissionsGroup.getPriority(), parentGroupNames, permissions);
    }

    /**
     * Deserialization method for the {@link ConfigurationSerializable}.
     *
     * @param data The serialized data.
     *
     * @return The model instance.
     */
    public static PermissionsGroupModel deserialize(Map<String, Object> data) {

        String name = (String) data.get("name");
        int priority = (int) data.get("priority");
        List<String> parents = (List<String>) data.get("parents");
        List<String> permissions = (List<String>) data.get("permissions");

        return new PermissionsGroupModel(name, priority, parents, permissions);
    }

    /**
     * Translate the persistence model into a business model.
     *
     * @param permissionsManager The permissions manager used to resolve other business objects.
     *
     * @return The business model.
     */
    public PermissionsGroup toPermissionsGroup(PermissionsManager permissionsManager) {

        // Parent groups
        List<PermissionsGroup> parentGroups = this.parentGroups.stream()
                .map(permissionsManager::getGroup)
                .collect(Collectors.toList());

        // Permissions
        Map<String, Boolean> permissions = PermissionConversionUtils.convertToPairNotation(this.permissions);

        return new SimplePermissionsGroup(name, priority, parentGroups, permissions);
    }

    @Override
    public Map<String, Object> serialize() {

        Map<String, Object> data = Maps.newHashMap();

        data.put("name", name);
        data.put("priority", priority);
        data.put("parents", parentGroups);
        data.put("permissions", permissions);

        return data;
    }
}
