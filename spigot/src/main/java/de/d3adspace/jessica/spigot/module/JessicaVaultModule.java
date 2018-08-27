package de.d3adspace.jessica.spigot.module;

import com.google.inject.AbstractModule;
import de.d3adspace.jessica.vault.chat.JessicaChat;
import de.d3adspace.jessica.vault.permissions.JessicaPermission;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class JessicaVaultModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(Permission.class).to(JessicaPermission.class);
        bind(Chat.class).to(JessicaChat.class);
    }
}
