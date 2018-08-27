package de.d3adspace.jessica.vault.chat;

import de.d3adspace.jessica.core.PermissionsManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
@Singleton
public class JessicaChat extends Chat {

    private static final String NAME = "JessicaChat";

    /**
     * The permissions manager.
     */
    private final PermissionsManager permissionsManager;

    /**
     * The plugin instance.
     */
    private final Plugin plugin;

    @Inject
    public JessicaChat(Permission permission, PermissionsManager permissionsManager, Plugin plugin) {
        super(permission);
        this.permissionsManager = permissionsManager;
        this.plugin = plugin;
    }

    @Override
    public String getName() {

        return NAME;
    }

    @Override
    public boolean isEnabled() {

        return plugin.isEnabled();
    }

    @Override
    public String getPlayerPrefix(String world, String playerName) {

        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return "";
        }

        return permissionsManager.getUser(player.getUniqueId()).getPrefix();
    }

    @Override
    public void setPlayerPrefix(String s, String s1, String s2) {

    }

    @Override
    public String getPlayerSuffix(String world, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return "";
        }

        return permissionsManager.getUser(player.getUniqueId()).getSuffix();
    }

    @Override
    public void setPlayerSuffix(String s, String s1, String s2) {

    }

    @Override
    public String getGroupPrefix(String s, String s1) {
        return null;
    }

    @Override
    public void setGroupPrefix(String s, String s1, String s2) {

    }

    @Override
    public String getGroupSuffix(String s, String s1) {
        return null;
    }

    @Override
    public void setGroupSuffix(String s, String s1, String s2) {

    }

    @Override
    public int getPlayerInfoInteger(String s, String s1, String s2, int i) {
        return 0;
    }

    @Override
    public void setPlayerInfoInteger(String s, String s1, String s2, int i) {

    }

    @Override
    public int getGroupInfoInteger(String s, String s1, String s2, int i) {
        return 0;
    }

    @Override
    public void setGroupInfoInteger(String s, String s1, String s2, int i) {

    }

    @Override
    public double getPlayerInfoDouble(String s, String s1, String s2, double v) {
        return 0;
    }

    @Override
    public void setPlayerInfoDouble(String s, String s1, String s2, double v) {

    }

    @Override
    public double getGroupInfoDouble(String s, String s1, String s2, double v) {
        return 0;
    }

    @Override
    public void setGroupInfoDouble(String s, String s1, String s2, double v) {

    }

    @Override
    public boolean getPlayerInfoBoolean(String s, String s1, String s2, boolean b) {
        return false;
    }

    @Override
    public void setPlayerInfoBoolean(String s, String s1, String s2, boolean b) {

    }

    @Override
    public boolean getGroupInfoBoolean(String s, String s1, String s2, boolean b) {
        return false;
    }

    @Override
    public void setGroupInfoBoolean(String s, String s1, String s2, boolean b) {

    }

    @Override
    public String getPlayerInfoString(String s, String s1, String s2, String s3) {
        return null;
    }

    @Override
    public void setPlayerInfoString(String s, String s1, String s2, String s3) {

    }

    @Override
    public String getGroupInfoString(String s, String s1, String s2, String s3) {
        return null;
    }

    @Override
    public void setGroupInfoString(String s, String s1, String s2, String s3) {

    }
}
