package de.d3adspace.jessica.spigot.plugin;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import de.d3adspace.jessica.core.permission.PermissionsManager;
import de.d3adspace.jessica.spigot.command.PermissionsCommandExecutor;
import de.d3adspace.jessica.spigot.listener.PlayerJoinListener;
import de.d3adspace.jessica.spigot.listener.PlayerQuitListener;
import de.d3adspace.jessica.spigot.module.JessicaConfigurationModule;
import de.d3adspace.jessica.spigot.module.JessicaSpigotModule;
import de.d3adspace.jessica.spigot.permission.file.model.group.PermissionsGroupModel;
import de.d3adspace.jessica.spigot.permission.file.model.user.PermissionsUserModel;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class JessicaSpigotPlugin extends JavaPlugin {

    @Inject
    PermissionsCommandExecutor permissionsCommandExecutor;
    private Injector injector;

    @Inject
    private PlayerJoinListener playerJoinListener;
    @Inject
    private PlayerQuitListener playerQuitListener;
    @Inject
    private PluginManager pluginManager;

    @Inject
    private PermissionsManager permissionsManager;

    @Override
    public void onLoad() {

        saveDefaultConfig();

        // Register classes for serialization, aliases are handled via annotation
        ConfigurationSerialization.registerClass(PermissionsGroupModel.class);
        ConfigurationSerialization.registerClass(PermissionsUserModel.class);
    }

    @Override
    public void onDisable() {

        // Make sure to cleanup permissions
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

            permissionsManager.destroySession(onlinePlayer.getUniqueId());
        }

        saveConfig();
    }

    @Override
    public void onEnable() {
        // Assemble and prepare guice modules
        Module baseModule = new JessicaSpigotModule(this);
        Module additionalModule = assembleModule();

        // Create main injector and begin lifecycle
        injector = Guice.createInjector(baseModule, additionalModule);
        injector.injectMembers(this);

        // Register events and commands
        getCommand("permissions").setExecutor(permissionsCommandExecutor);
        pluginManager.registerEvents(playerJoinListener, this);
        pluginManager.registerEvents(playerQuitListener, this);

        // Uh, online players?
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

            permissionsManager.initSession(onlinePlayer.getUniqueId());
        }
    }

    /**
     * Assemble the guice module e.g. with configuration based implementation choosing. After that you may combine
     * them via {@link Modules#combine(Module...)}.
     *
     * @return The resulting module.
     */
    private Module assembleModule() {

        // The result that will be combined at the end
        List<Module> modules = Lists.newArrayList();

        // For now we don't make any choices
        modules.add(new JessicaConfigurationModule(getConfig()));

        // Combine results to one module
        return Modules.combine(modules);
    }
}
