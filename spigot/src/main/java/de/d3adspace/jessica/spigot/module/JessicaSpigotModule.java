package de.d3adspace.jessica.spigot.module;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;
import de.d3adspace.jessica.core.PermissionsManager;
import de.d3adspace.jessica.spigot.JessicaApplication;
import de.d3adspace.jessica.spigot.JessicaApplicationImpl;
import de.d3adspace.jessica.spigot.permission.ConfigurationPermissionManager;
import de.d3adspace.jessica.spigot.plugin.JessicaSpigotPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;

/**
 * The {@link Module} for Jessica that provides bukkit specific bindings like the plugin instance but also the
 * implementations of our services.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class JessicaSpigotModule extends AbstractModule {

    /**
     * Jessicas's plugin instance.
     */
    private final JessicaSpigotPlugin jessicaSpigotPlugin;

    /**
     * Create a new jessica spigot module based on the spigot plugin instance.
     *
     * @param jessicaSpigotPlugin The plugin instance.
     */
    public JessicaSpigotModule(JessicaSpigotPlugin jessicaSpigotPlugin) {
        this.jessicaSpigotPlugin = jessicaSpigotPlugin;
    }

    @Override
    protected void configure() {
        // Plugin and bukkit specific stuff
        bind(Plugin.class).toInstance(jessicaSpigotPlugin);
        bind(PluginManager.class).toInstance(jessicaSpigotPlugin.getServer().getPluginManager());
        bind(ServicesManager.class).toInstance(jessicaSpigotPlugin.getServer().getServicesManager());

        // Permissions manager
        bind(Configuration.class).annotatedWith(Names.named("permissionsConfiguration")).toInstance(jessicaSpigotPlugin.getConfig());
        bind(PermissionsManager.class).to(ConfigurationPermissionManager.class);

        // Bind main application
        bind(JessicaApplication.class).to(JessicaApplicationImpl.class);
    }
}
