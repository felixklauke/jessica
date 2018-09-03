package de.d3adspace.jessica.spigot.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.d3adspace.jessica.core.permission.PermissionsManager;
import de.d3adspace.jessica.spigot.permission.file.ConfigurationPermissionsManager;
import org.bukkit.configuration.Configuration;

/**
 * Configures bindings for a configuration based {@link PermissionsManager}.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class JessicaConfigurationModule extends AbstractModule {

    private final Configuration permissionsConfiguration;

    public JessicaConfigurationModule(Configuration permissionsConfiguration) {
        this.permissionsConfiguration = permissionsConfiguration;
    }

    @Override
    protected void configure() {

        bind(Configuration.class).annotatedWith(Names.named("permissionsConfiguration")).toInstance(permissionsConfiguration);
        bind(PermissionsManager.class).to(ConfigurationPermissionsManager.class);
    }
}
