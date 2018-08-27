package de.d3adspace.jessica.spigot.command;

import com.google.common.collect.Lists;
import de.d3adspace.jessica.core.PermissionsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.PermissionAttachmentInfo;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
@Singleton
public class PermissionsCommand implements CommandExecutor, TabCompleter {

    private final PermissionsManager permissionsManager;

    @Inject
    public PermissionsCommand(PermissionsManager permissionsManager) {
        this.permissionsManager = permissionsManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        int argumentCount = args.length;

        if (argumentCount == 0) {
            printPermissions(commandSender);
            return true;
        }

        return false;
    }

    /**
     * Show the player his effective permissions.
     *
     * @param commandSender The command sender.
     */
    private void printPermissions(CommandSender commandSender) {
        Set<PermissionAttachmentInfo> effectivePermissions = commandSender.getEffectivePermissions();
        for (PermissionAttachmentInfo effectivePermission : effectivePermissions) {
            commandSender.sendMessage(effectivePermission.getPermission() + " = " + effectivePermission.getValue());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        return Lists.newArrayList();
    }
}
