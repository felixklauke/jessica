package de.d3adspace.jessica.spigot.permission.file.model.user;

import com.google.common.collect.Maps;
import de.d3adspace.jessica.core.permission.PermissionsManager;
import de.d3adspace.jessica.core.permission.group.PermissionsGroup;
import de.d3adspace.jessica.core.permission.user.PermissionsUser;
import de.d3adspace.jessica.spigot.permission.user.SimplePermissionsUser;
import de.d3adspace.jessica.spigot.utils.PermissionConversionUtils;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Persistence model for the {@link PermissionsUserModel} built with Bukkit's {@link ConfigurationSerializable}. You may
 * use {@link #fromPermissionUser(PermissionsUser)} and {@link #toPermissionsUser(PermissionsManager)} for conversion
 * with business models.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
@SerializableAs("permissionsUser")
public class PermissionsUserModel implements ConfigurationSerializable {

    /**
     * The unique id of the user.
     */
    private final UUID uniqueId;

    /**
     * The permissions of the user in string notation.
     */
    private final List<String> permissions;

    /**
     * The groups of the user represented by their names.
     */
    private final List<String> groups;

    /**
     * Create a new model by all its data - Use this with care and consider using
     * {@link #fromPermissionUser(PermissionsUser)} (PermissionsGroup)} instead to ensure correct data conversion.
     *
     * @param uniqueId    The unique id.
     * @param permissions The permissions.
     * @param groups      The groups.
     */
    public PermissionsUserModel(UUID uniqueId, List<String> permissions, List<String> groups) {
        this.uniqueId = uniqueId;
        this.permissions = permissions;
        this.groups = groups;
    }

    /**
     * Factory method to create the persistence model from the business object.
     *
     * @param permissionsUser The permissions user business model.
     *
     * @return The persistence model.
     */
    public static PermissionsUserModel fromPermissionUser(PermissionsUser permissionsUser) {

        // User permissions
        List<String> permissions = PermissionConversionUtils.convertToStringNotation(permissionsUser.getPermissions());

        // Groups
        List<String> groupNames = permissionsUser.getPermissionGroups()
                .stream()
                .map(PermissionsGroup::getName)
                .collect(Collectors.toList());

        return new PermissionsUserModel(permissionsUser.getUniqueId(), permissions, groupNames);
    }

    /**
     * Deserialization method for the {@link ConfigurationSerializable}.
     *
     * @param data The serialized data.
     *
     * @return The model instance.
     */
    public static PermissionsUserModel deserialize(Map<String, Object> data) {

        UUID uniqueId = UUID.fromString((String) data.get("uniqueId"));
        List<String> permissions = (List<String>) data.get("permissions");
        List<String> groups = (List<String>) data.get("groups");

        return new PermissionsUserModel(uniqueId, permissions, groups);
    }

    /**
     * Translate the persistence model into a business model.
     *
     * @param permissionsManager The permissions manager used to resolve other business objects.
     *
     * @return The business model.
     */
    public PermissionsUser toPermissionsUser(PermissionsManager permissionsManager) {

        // User Permissions
        Map<String, Boolean> permissions = PermissionConversionUtils.convertToPairNotation(this.permissions);

        // Groups
        List<PermissionsGroup> permissionsGroups = groups.stream()
                .map(permissionsManager::getGroup)
                .collect(Collectors.toList());

        return new SimplePermissionsUser(uniqueId, permissions, permissionsGroups);
    }

    @Override
    public Map<String, Object> serialize() {

        Map<String, Object> data = Maps.newHashMap();

        data.put("uniqueId", uniqueId.toString());
        data.put("permissions", permissions);
        data.put("groups", groups);

        return data;
    }
}
