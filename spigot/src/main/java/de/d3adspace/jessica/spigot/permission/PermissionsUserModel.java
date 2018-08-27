package de.d3adspace.jessica.spigot.permission;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.d3adspace.jessica.core.group.PermissionsGroup;
import de.d3adspace.jessica.core.user.PermissionsUser;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
@SerializableAs("permissionsUser")
public class PermissionsUserModel implements PermissionsUser, ConfigurationSerializable {

    private final UUID uniqueId;
    private final Map<String, Object> metaData;
    private final Map<String, Boolean> permissions;
    private final String group;

    public PermissionsUserModel(UUID uniqueId, Map<String, Object> metaData, Map<String, Boolean> permissions, String group) {
        this.uniqueId = uniqueId;
        this.metaData = metaData;
        this.permissions = permissions;
        this.group = group;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public PermissionsGroup getGroup() {
        return null;
    }

    @Override
    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

    @Override
    public Map<String, Boolean> getEffectivePermissions() {
        return permissions;
    }

    public static PermissionsUserModel deserialize(Map<String, Object> data) {
        Object uniqueIdObject = data.get("uniqueId");
        Object metaDataObject = data.get("metadata");
        Object groupNameObject = data.get("group");
        Object permissionsObject = data.get("permissions");

        UUID uniqueId = UUID.fromString(uniqueIdObject.toString());
        String groupName = groupNameObject.toString();
        Map<String, Object> metaData = (Map<String, Object>) metaDataObject;
        List<String> permissionsList = (List<String>) permissionsObject;
        Map<String, Boolean> permissions = Maps.newHashMap();

        for (String permission : permissionsList) {
            boolean status = true;

            if (permission.startsWith("-")) {
                status = false;
                permission = permission.substring(1);
            }

            permissions.put(permission, status);
        }

        return new PermissionsUserModel(uniqueId, metaData, permissions, groupName);
    }

    @Override
    public String getPrefix() {
        Object prefix = metaData.get("prefix");
        return prefix == null ? "" : prefix.toString();
    }

    @Override
    public String getSuffix() {
        Object suffix = metaData.get("suffix");
        return suffix == null ? "" : suffix.toString();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = Maps.newHashMap();
        data.put("uniqueId", uniqueId.toString());
        data.put("metadata", metaData);
        data.put("group", group);

        List<String> permissionsList = Lists.newArrayList();

        permissions.forEach((permission, status) -> {

            if (!status) {
                permission = "-" + permission;
            }

            permissionsList.add(permission);
        });

        data.put("permissions", permissionsList);
        return data;
    }
}
