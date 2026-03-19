package me.h3xty.h3xWhitelist;

import java.util.List;

public class WhitelistUtil {
    private final H3xWhitelist plugin;
    private List<String> whitelistedNicknames;
    private boolean whitelistEnabled;
    private String whitelistMessage;

    public WhitelistUtil(H3xWhitelist plugin) {
        this.plugin = plugin;
    }

    public void enableWhitelist() {
        whitelistEnabled = true;
        plugin.getConfig().set("whitelist-enabled", true);
        saveConfig();
    }

    public void disableWhitelist() {
        whitelistEnabled = false;
        plugin.getConfig().set("whitelist-enabled", false);
        saveConfig();
    }

    public boolean isWhitelistEnabled() {
        return whitelistEnabled;
    }

    public void addToWhitelist(String nickname) {
        whitelistedNicknames.add(nickname);
        plugin.getConfig().set("nicknames", whitelistedNicknames);
        saveConfig();
    }

    public void removeFromWhitelist(String nickname) {
        whitelistedNicknames.remove(nickname);
        plugin.getConfig().set("nicknames", whitelistedNicknames);
        saveConfig();
    }

    public List<String> getWhitelistedNicknames() {
        return whitelistedNicknames;
    }

    public String whitelistMessage() {
        return whitelistMessage;
    }

    public void loadConfig() {
        whitelistEnabled = plugin.getConfig().getBoolean("whitelist-enabled");
        whitelistedNicknames = plugin.getConfig().getStringList("nicknames");
        whitelistMessage = plugin.getConfig().getString("not-whitelisted");
    }

    public void saveConfig() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, plugin::saveConfig);
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }
}
