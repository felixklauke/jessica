package de.d3adspace.jessica.spigot.listener;

import de.d3adspace.jessica.spigot.JessicaApplication;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link Listener} for the {@link PlayerJoinEvent} that is responsible for letting the {@link JessicaApplication}
 * know about joined players so their permissions can be initialized properly.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
@Singleton
public class PlayerJoinListener implements Listener {

    /**
     * The jessica application.
     */
    private final JessicaApplication jessicaApplication;

    /**
     * Create a new player join listener by the underlying jessica application.
     *
     * @param jessicaApplication the jessica application.
     */
    @Inject
    public PlayerJoinListener(JessicaApplication jessicaApplication) {
        this.jessicaApplication = jessicaApplication;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {

        // Initialize player permissions
        Player player = playerJoinEvent.getPlayer();
        jessicaApplication.initPermissions(player);
    }
}
