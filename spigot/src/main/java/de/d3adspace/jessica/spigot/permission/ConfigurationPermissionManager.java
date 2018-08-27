package de.d3adspace.jessica.spigot.permission;

import com.google.common.collect.Maps;
import de.d3adspace.jessica.core.user.PermissionsUser;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.UUID;

/**
 * A permission manager that is based on a {@link Configuration} working with model classes like
 * {@link PermissionsUserModel}.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class ConfigurationPermissionManager extends AbstractPermissionManager {

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
            HashMap<String, Object> metaData = Maps.newHashMap();
            metaData.put("prefix", ChatColor.GOLD.toString());
            metaData.put("suffix", ChatColor.AQUA.toString());
            PermissionsUser permissionsUser = new PermissionsUserModel(uniqueId, metaData, Maps.newHashMap(), "default");

            savePermissionsUser(permissionsUser);

            return permissionsUser;
        }

        // Load object directly from config.
        return configuration.getSerializable("permissions.player." + uniqueId, PermissionsUserModel.class);
    }

    @Override
    protected void savePermissionsUser(PermissionsUser permissionsUser) {

        configuration.set("permissions.player." + permissionsUser.getUniqueId(), permissionsUser);
    }
}
