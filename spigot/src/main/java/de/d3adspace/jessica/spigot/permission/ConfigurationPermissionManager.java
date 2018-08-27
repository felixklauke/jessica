package de.d3adspace.jessica.spigot.permission;

import com.google.common.collect.Maps;
import de.d3adspace.jessica.core.PermissionsManager;
import de.d3adspace.jessica.core.user.PermissionsUser;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

/**
 * A permission manager that is based on a {@link Configuration} working with model classes like
 * {@link PermissionsUserModel}.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class ConfigurationPermissionManager implements PermissionsManager {

    /**
     * The underlying configuration.
     */
    private final Configuration configuration;

    /**
     * Create a new configuration based permission manager based on a given config.
     *
     * @param configuration The config.
     */
    @Inject
    public ConfigurationPermissionManager(@Named("permissionsConfiguration") Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public PermissionsUser getUser(UUID uniqueId) {

        // Check if we already know that player.
        if (!configuration.contains("permissions.player." + uniqueId)) {

            // The permissions user object. TODO: Configurable default values.
            PermissionsUser permissionsUser = new PermissionsUserModel(uniqueId, Maps.newHashMap(), "default");

            configuration.set("permissions.player." + uniqueId, permissionsUser);

            return permissionsUser;
        }

        // Load object directly from config.
        return configuration.getSerializable("permissions.player." + uniqueId, PermissionsUserModel.class);
    }
}
