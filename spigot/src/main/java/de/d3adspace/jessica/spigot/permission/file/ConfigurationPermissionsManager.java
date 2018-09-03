package de.d3adspace.jessica.spigot.permission.file;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.d3adspace.jessica.core.exception.GroupNotFoundException;
import de.d3adspace.jessica.core.permission.PermissionsManager;
import de.d3adspace.jessica.core.permission.group.PermissionsGroup;
import de.d3adspace.jessica.core.permission.user.PermissionsUser;
import de.d3adspace.jessica.spigot.permission.AbstractPermissionsManager;
import de.d3adspace.jessica.spigot.permission.file.model.group.PermissionsGroupModel;
import de.d3adspace.jessica.spigot.permission.file.model.user.PermissionsUserModel;
import de.d3adspace.jessica.spigot.permission.group.SimplePermissionsGroup;
import de.d3adspace.jessica.spigot.permission.user.SimplePermissionsUser;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * An {@link PermissionsManager} based on bukkits {@link Configuration} stored in {@link #permissionsConfiguration}.
 * <p>
 * Bound in {@link Singleton} Scope for performance and convenience reasons.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
@Singleton
public class ConfigurationPermissionsManager extends AbstractPermissionsManager {

    /**
     * The permissions configuration.
     */
    private final Configuration permissionsConfiguration;

    @Inject
    public ConfigurationPermissionsManager(Plugin plugin, @Named("permissionsConfiguration") Configuration permissionsConfiguration) {
        super(plugin);
        this.permissionsConfiguration = permissionsConfiguration;
    }

    @Override
    protected PermissionsUser loadUser(UUID uniqueId) {

        // Get user section and resolve persistence model
        ConfigurationSection userSection = getUserSection();
        PermissionsUserModel userModel = (PermissionsUserModel) userSection.get(uniqueId.toString());

        // Initially create and save a new permissions user if it doesn't exist yet
        if (userModel == null) {
            PermissionsUser permissionsUser = new SimplePermissionsUser(uniqueId, Maps.newHashMap(), Lists.newArrayList());
            userModel = PermissionsUserModel.fromPermissionUser(permissionsUser);
            savePermissionsUserModel(uniqueId, userModel);
            return permissionsUser;
        }

        return userModel.toPermissionsUser(this);
    }

    @Override
    protected PermissionsGroup loadGroup(String groupName) throws GroupNotFoundException {

        ConfigurationSection groupSection = getGroupSection();
        PermissionsGroupModel permissionsGroupModel = (PermissionsGroupModel) groupSection.get(groupName);

        // Throw exception if the group couldn't be found
        if (permissionsGroupModel == null) {

            throw new GroupNotFoundException("Couldn't find group with name " + groupName);
        }

        return permissionsGroupModel.toPermissionsGroup(ConfigurationPermissionsManager.this);
    }

    @Override
    protected void saveUser(PermissionsUser permissionsUser) {

        PermissionsUserModel userModel = PermissionsUserModel.fromPermissionUser(permissionsUser);
        savePermissionsUserModel(permissionsUser.getUniqueId(), userModel);
    }

    @Override
    protected void saveGroup(PermissionsGroup permissionsGroup) {

        ConfigurationSection groupSection = getGroupSection();

        PermissionsGroupModel groupModel = PermissionsGroupModel.fromPermissionsGroup(permissionsGroup);
        groupSection.set(permissionsGroup.getName(), groupModel);
    }

    @Override
    public List<PermissionsGroup> getGroups() {

        ConfigurationSection groupSection = getGroupSection();
        Map<String, Object> values = groupSection.getValues(false);

        // Needed to cast a collection
        return values.values()
                .stream()
                .map(PermissionsGroup.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public void addGroup(String name, int priority, List<PermissionsGroup> parentGroups, Map<String, Object> metadata, Map<String, Boolean> permissions) {

        PermissionsGroup permissionsGroup = new SimplePermissionsGroup(name, priority, parentGroups, permissions);
        saveGroup(permissionsGroup);
    }

    @Override
    public void removeGroup(String name) {

        throw new UnsupportedOperationException();
    }

    /**
     * Save the given model in the configuration for teh given unique id.
     *
     * @param uniqueId  The unique id.
     * @param userModel The model.
     */
    private void savePermissionsUserModel(UUID uniqueId, PermissionsUserModel userModel) {

        ConfigurationSection userSection = getUserSection();
        userSection.set(uniqueId.toString(), userModel);
    }

    /**
     * Get or create the user section in the config.
     *
     * @return The section.
     */
    private ConfigurationSection getUserSection() {

        ConfigurationSection userSection = permissionsConfiguration.getConfigurationSection("users");
        if (userSection == null) {
            userSection = permissionsConfiguration.createSection("users");
        }

        return userSection;
    }

    /**
     * Get or create the user section in the config.
     *
     * @return The section.
     */
    private ConfigurationSection getGroupSection() {

        ConfigurationSection userSection = permissionsConfiguration.getConfigurationSection("groups");
        if (userSection == null) {
            userSection = permissionsConfiguration.createSection("groups");
        }

        return userSection;
    }
}
