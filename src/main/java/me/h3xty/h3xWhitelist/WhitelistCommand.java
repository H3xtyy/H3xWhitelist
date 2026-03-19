package me.h3xty.h3xWhitelist;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.List;

public class WhitelistCommand implements BasicCommand {

    private final WhitelistUtil whitelistUtil;

    public WhitelistCommand(WhitelistUtil whitelistUtil) {
        this.whitelistUtil = whitelistUtil;
    }

    @Override
    public void execute(@NonNull CommandSourceStack commandSourceStack, String[] args) {
        if (args.length == 0) {
            sendUsageMessage(commandSourceStack);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "on":
                if (whitelistUtil.isWhitelistEnabled()) {
                    sendMessage(commandSourceStack, "<red>Whitelist is already enabled!</red>");
                    return;
                }
                whitelistUtil.enableWhitelist();
                sendMessage(commandSourceStack, "<green>Whitelist enabled!</green>");
                break;

            case "off":
                if (!whitelistUtil.isWhitelistEnabled()) {
                    sendMessage(commandSourceStack, "<red>Whitelist is already disabled!</red>");
                    return;
                }
                whitelistUtil.disableWhitelist();
                sendMessage(commandSourceStack, "<green>Whitelist disabled!</green>");
                break;

            case "add":
                if (args.length < 2) {
                    sendMessage(commandSourceStack, "<green>Usage: /h3xwhitelsit add <nickname></green>");
                    return;
                }
                addNickname(commandSourceStack, args[1]);
                break;

            case "remove":
                if (args.length < 2) {
                    sendMessage(commandSourceStack, "<green>Usage: /h3xwhitelsit remove <nickname></green>");
                    return;
                }
                removeNickname(commandSourceStack, args[1]);
                break;

            case "list":
                if (!whitelistUtil.getWhitelistedNicknames().isEmpty()) {
                    sendMessage(commandSourceStack, "<green>" + whitelistUtil.getWhitelistedNicknames() + "</green>");
                    return;
                }
                sendMessage(commandSourceStack, "<red>There are no whitelisted players!</red>");
                break;

            case "reload":
                whitelistUtil.reloadConfig();
                sendMessage(commandSourceStack, "<green>Config reloaded!</green>");
                break;
        }
    }

    @Override
    public @NonNull Collection<String> suggest(@NonNull CommandSourceStack commandSourceStack, String[] args) {
        if (args.length == 0 || args.length == 1) {
            return List.of("on", "off", "add", "remove", "reload", "list");
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("remove")) {
                return whitelistUtil.getWhitelistedNicknames();
            } else if (args[0].equalsIgnoreCase("add")) {
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            }
        }
        return BasicCommand.super.suggest(commandSourceStack, args);
    }

    @Override
    public String permission() {
        return "h3xwhitelist";
    }

    private void addNickname(CommandSourceStack commandSourceStack, String nickname) {
        if (whitelistUtil.getWhitelistedNicknames().contains(nickname)) {
            sendMessage(commandSourceStack, "<red>" + nickname + " is already on the list!</red>");
            return;
        }

        whitelistUtil.addToWhitelist(nickname);
        sendMessage(commandSourceStack, "<green>" + nickname + " has been added to the whitelist!</green>");
    }

    private void removeNickname(CommandSourceStack commandSourceStack, String nickname) {
        if (!whitelistUtil.getWhitelistedNicknames().contains(nickname)) {
            sendMessage(commandSourceStack, "<red>" + nickname + " is not on the list!</red>");
            return;
        }

        whitelistUtil.removeFromWhitelist(nickname);
        sendMessage(commandSourceStack, "<green>" + nickname + " has been removed from the whitelist!</green>");
    }

    private void sendUsageMessage(@NonNull CommandSourceStack source) {
        String usage = """
            
            <green>Usage:
            /h3xwhitelist on - Enable whitelist
            /h3xwhitelist off - Disable whitelist
            /h3xwhitelist add <nickname> - Add player to whitelist
            /h3xwhitelist remove <nickname> - Remove player from whitelist
            /h3xwhitelist list - List of whitelisted players
            /h3xwhitelist reload - Reload config file</green>
            """;
        sendMessage(source, usage);
    }

    private void sendMessage(@NonNull CommandSourceStack commandSourceStack, String message) {
        Component component = MiniMessage.miniMessage().deserialize(message);
        commandSourceStack.getSender().sendMessage(component);
    }
}
