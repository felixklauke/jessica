package de.d3adspace.jessica.spigot.listener;

import de.d3adspace.jessica.core.permission.PermissionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Listener for the {@link PlayerJoinEvent} and responsible for handling incoming players.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class PlayerJoinListener implements Listener {

    /**
     * The permissions manager used for initiating a new player session.
     */
    private final PermissionsManager permissionsManager;

    @Inject
    public PlayerJoinListener(PermissionsManager permissionsManager) {
        this.permissionsManager = permissionsManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {

        Player player = playerJoinEvent.getPlayer();
        UUID uniqueId = player.getUniqueId();

        // Init session and load the players permissions
        permissionsManager.initSession(uniqueId);
    }
}
