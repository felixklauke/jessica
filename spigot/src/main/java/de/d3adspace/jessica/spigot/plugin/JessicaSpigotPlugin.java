package de.d3adspace.jessica.spigot.plugin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.d3adspace.jessica.spigot.JessicaApplication;
import de.d3adspace.jessica.spigot.listener.PlayerJoinListener;
import de.d3adspace.jessica.spigot.listener.PlayerQuitListener;
import de.d3adspace.jessica.spigot.module.JessicaSpigotModule;
import de.d3adspace.jessica.spigot.permission.PermissionsUserModel;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

/**
 * Central {@link Plugin} of Jessica.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class JessicaSpigotPlugin extends JavaPlugin {

    /**
     * The jessica application instance.
     */
    @Inject
    private JessicaApplication jessicaApplication;

    /**
     * The plugin manager needed for listener registration.
     */
    @Inject
    private PluginManager pluginManager;

    @Inject
    private PlayerJoinListener playerJoinListener;
    @Inject
    private PlayerQuitListener playerQuitListener;

    @Override
    public void onLoad() {

        saveDefaultConfig();

        // Register configuration serializable classes.
        ConfigurationSerialization.registerClass(PermissionsUserModel.class, "permissionsUser");
    }

    @Override
    public void onEnable() {

        // Create main injector and init members.
        Injector injector = Guice.createInjector(new JessicaSpigotModule(this));
        injector.injectMembers(this);

        // Register listeners
        pluginManager.registerEvents(playerJoinListener, this);
        pluginManager.registerEvents(playerQuitListener, this);
    }

    @Override
    public void onDisable() {

        saveConfig();

        jessicaApplication.destroy();
    }
}
