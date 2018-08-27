package de.d3adspace.jessica.spigot.plugin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import de.d3adspace.jessica.spigot.JessicaApplication;
import de.d3adspace.jessica.spigot.command.PermissionsCommand;
import de.d3adspace.jessica.spigot.listener.PlayerJoinListener;
import de.d3adspace.jessica.spigot.listener.PlayerQuitListener;
import de.d3adspace.jessica.spigot.module.JessicaSpigotModule;
import de.d3adspace.jessica.spigot.module.JessicaVaultModule;
import de.d3adspace.jessica.spigot.permission.PermissionsUserModel;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

/**
 * Central {@link Plugin} of Jessica.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class JessicaSpigotPlugin extends JavaPlugin {

    private static final String VAULT_PLUGIN_NAME = "Vault";

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
    private ServicesManager servicesManager;

    @Inject
    private PlayerJoinListener playerJoinListener;
    @Inject
    private PlayerQuitListener playerQuitListener;

    @Inject
    private PermissionsCommand permissionsCommand;

    @Override
    public void onLoad() {

        saveDefaultConfig();

        // Register configuration serializable classes.
        ConfigurationSerialization.registerClass(PermissionsUserModel.class, "permissionsUser");
    }

    @Override
    public void onEnable() {
        // Check if vault is loaded and we can provide vault support.
        boolean vaultSupport = pluginManager.getPlugin(VAULT_PLUGIN_NAME) != null;

        // Assemble module
        Module module = new JessicaSpigotModule(this);
        if (vaultSupport) {
            module = Modules.combine(module, new JessicaVaultModule());
        }

        // Create main injector and init members.
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(this);

        if (vaultSupport) {
            Permission permission = injector.getInstance(Permission.class);
            servicesManager.register(Permission.class, permission, this, ServicePriority.High);
        }

        // Register listeners.
        pluginManager.registerEvents(playerJoinListener, this);
        pluginManager.registerEvents(playerQuitListener, this);

        // Register commands.
        PluginCommand permissionsPluginCommand = getCommand("perm");
        permissionsPluginCommand.setExecutor(permissionsCommand);
        permissionsPluginCommand.setTabCompleter(permissionsCommand);
    }

    @Override
    public void onDisable() {

        saveConfig();

        jessicaApplication.destroy();
    }
}
