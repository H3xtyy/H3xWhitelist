package me.h3xty.h3xWhitelist;

import io.papermc.paper.command.brigadier.BasicCommand;
import me.h3xty.h3xWhitelist.commands.ManageWhitelist;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class H3xWhitelist extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        BasicCommand whitelist = new ManageWhitelist(this);
        registerCommand("h3xwhitelist", whitelist);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        boolean whitelistEnabled = getConfig().getBoolean("whitelist-enabled");
        String player = event.getName();

        if (whitelistEnabled) {
            List<String> whitelistedNicknames = getConfig().getStringList("nicknames");

            if (!whitelistedNicknames.contains(player)) {
                String stringMessage = getConfig().getString("not-whitelisted");
                Component kickMessage = MiniMessage.miniMessage().deserialize(stringMessage);
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, kickMessage);
            }
        }
    }
}
