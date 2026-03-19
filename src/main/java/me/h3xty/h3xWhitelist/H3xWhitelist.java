package me.h3xty.h3xWhitelist;

import io.papermc.paper.command.brigadier.BasicCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class H3xWhitelist extends JavaPlugin implements Listener {
    private WhitelistUtil whitelistUtil;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        whitelistUtil = new WhitelistUtil(this);
        whitelistUtil.loadConfig();
        getServer().getPluginManager().registerEvents(this, this);
        BasicCommand whitelist = new WhitelistCommand(whitelistUtil);
        registerCommand("h3xwhitelist", whitelist);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        if (whitelistUtil.isWhitelistEnabled() && !whitelistUtil.getWhitelistedNicknames().contains(event.getName())) {
            Component kickMessage = MiniMessage.miniMessage().deserialize(whitelistUtil.whitelistMessage());
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, kickMessage);
        }
    }
}
