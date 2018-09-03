package de.d3adspace.jessica.spigot.listener;

import de.d3adspace.jessica.core.permission.PermissionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Listener for the {@link PlayerQuitEvent} responsible for handling quitting players.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class PlayerQuitListener implements Listener {

    /**
     * The permissions manager used to destroy player sessions.
     */
    private final PermissionsManager permissionsManager;

    @Inject
    public PlayerQuitListener(PermissionsManager permissionsManager) {
        this.permissionsManager = permissionsManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {

        Player player = playerQuitEvent.getPlayer();
        UUID uniqueId = player.getUniqueId();

        // Destroy the permission session and erase data
        permissionsManager.destroySession(uniqueId);
    }
}
