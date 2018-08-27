package de.d3adspace.jessica.spigot;

import org.bukkit.entity.Player;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface JessicaApplication {

    /**
     * Destroy the application and clean da shiat up.
     */
    void destroy();

    /**
     * Init the permissions of the given player.
     *
     * @param player The player.
     */
    void initPermissions(Player player);

    /**
     * Destroy the permissions of the given player.
     *
     * @param player The player.
     */
    void destroyPermissions(Player player);
}
