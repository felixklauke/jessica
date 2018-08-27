package de.d3adspace.jessica.spigot.listener;

import de.d3adspace.jessica.spigot.JessicaApplication;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;

/**
 * {@link Listener} for the {@link PlayerQuitEvent} responsible for cleaning up whatever the leaving {@link Player}
 * lefts for us.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class PlayerQuitListener implements Listener {

    /**
     * The underlying jessica application.
     */
    private final JessicaApplication jessicaApplication;

    /**
     * Create a new quit listener by the underlying jessica application.
     *
     * @param jessicaApplication The jessica application.
     */
    @Inject
    public PlayerQuitListener(JessicaApplication jessicaApplication) {
        this.jessicaApplication = jessicaApplication;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {

        Player player = playerQuitEvent.getPlayer();
        jessicaApplication.destroyPermissions(player);
    }
}
