package de.d3adspace.jessica.spigot.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.d3adspace.jessica.spigot.plugin.JessicaSpigotPlugin;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;

/**
 * Contains bindings regarding general plugin resources and instances.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class JessicaSpigotModule extends AbstractModule {

    /**
     * The plugin instance used to obtain the server resources.
     */
    private final JessicaSpigotPlugin jessicaSpigotPlugin;

    /**
     * Create a new module instance by the underlying plugin instance.
     *
     * @param jessicaSpigotPlugin The plugin instance.
     */
    public JessicaSpigotModule(JessicaSpigotPlugin jessicaSpigotPlugin) {
        this.jessicaSpigotPlugin = jessicaSpigotPlugin;
    }

    @Override
    protected void configure() {

        bind(Plugin.class).toInstance(jessicaSpigotPlugin);
        bind(Configuration.class).annotatedWith(Names.named("mainConfig")).toInstance(jessicaSpigotPlugin.getConfig());

        Server server = jessicaSpigotPlugin.getServer();
        bind(Server.class).toInstance(server);
        bind(ServicesManager.class).toInstance(server.getServicesManager());
        bind(PluginManager.class).toInstance(server.getPluginManager());
    }
}
