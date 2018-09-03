package de.d3adspace.jessica.spigot.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Set;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class PermissionsCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        int argumentCount = args.length;

        // Switch execution based on argument count
        if (argumentCount == 0) {
            return printPermissions(commandSender);
        }

        return false;
    }

    /**
     * Print all permissions for the given command sender.
     *
     * @param commandSender The command sender.
     */
    private boolean printPermissions(CommandSender commandSender) {

        commandSender.sendMessage(ChatColor.GOLD + "Your Permissions: ");

        // Get effective permissions
        Set<PermissionAttachmentInfo> effectivePermissions = commandSender.getEffectivePermissions();

        // Show permissions and their values to the command sender
        for (PermissionAttachmentInfo effectivePermission : effectivePermissions) {

            commandSender.sendMessage("- " + effectivePermission.getPermission() + " = " + effectivePermission.getValue());
        }

        return true;
    }
}
