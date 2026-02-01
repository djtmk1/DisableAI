package com.djtmk.disableai.config;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Messages {

    private String prefix;
    private String noPermission;
    private String configReloaded;
    private String noEntitiesDisabled;
    private String helpAdd;
    private String helpRemove;
    private String helpList;
    private String helpReload;
    private String usageAdd;
    private String usageRemove;
    private String header;
    private String author;
    private String entityAdded;
    private String entityRemoved;
    private String entityAlreadyDisabled;
    private String entityNotDisabled;
    private String entityNotLiving;
    private String invalidEntityType;
    private String disabledEntitiesHeader;
    private String entityList;
    private String listSeparator;

    @Getter
    private final JavaPlugin plugin;
    private final File file;
    private FileConfiguration config;

    public Messages(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "messages.yml");
        loadConfig();
    }

    public void loadConfig() {
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        loadMessages();
    }

    private void loadMessages() {
        prefix = config.getString("prefix", "&8[&5DisableAI&8] &r");
        noPermission = config.getString("no-permission", "&cNo permission!");
        configReloaded = config.getString("config-reloaded", "&aConfig reloaded!");
        noEntitiesDisabled = config.getString("no-entities-disabled", "&eNo entities are disabled.");

        helpAdd = config.getString("help.add", "&7/ai add <entity> &8- &rAdd entity to disabled list");
        helpRemove = config.getString("help.remove", "&7/ai remove <entity> &8- &rRemove entity from disabled list");
        helpList = config.getString("help.list", "&7/ai list &8- &rList all disabled entities");
        helpReload = config.getString("help.reload", "&7/ai reload &8- &rReload plugin");

        usageAdd = config.getString("usage.add", "&cUsage: /ai add <entity>");
        usageRemove = config.getString("usage.remove", "&cUsage: /ai remove <entity>");

        header = config.getString("header", "&8» &5DisableAI &7v{version}");
        author = config.getString("author", "&8Author: &7{name}");

        entityAdded = config.getString("entity.added", "&aAdded &e{name}&a to disabled list!");
        entityRemoved = config.getString("entity.removed", "&aRemoved &e{name}&a from disabled list!");
        entityAlreadyDisabled = config.getString("entity.already-disabled", "&e{name} is already disabled!");
        entityNotDisabled = config.getString("entity.not-disabled", "&e{name} is not in the disabled list!");
        entityNotLiving = config.getString("entity.not-living", "&cEntity {name} is not a living entity!");
        invalidEntityType = config.getString("entity.invalid-type", "&cInvalid entity type: {name}");
        disabledEntitiesHeader = config.getString("entity.list-header", "&aDisabled entities ({count}):");
        entityList = config.getString("entity.list-format", "&e{list}");
        listSeparator = config.getString("entity.list-separator", "&7, &e");
    }

    public String getNoPermission() {
        return colorize(prefix + noPermission);
    }

    public String getConfigReloaded() {
        return colorize(prefix + configReloaded);
    }

    public String getNoEntitiesDisabled() {
        return colorize(prefix + noEntitiesDisabled);
    }

    public String getHelpAdd() {
        return colorize(helpAdd);
    }

    public String getHelpRemove() {
        return colorize(helpRemove);
    }

    public String getHelpList() {
        return colorize(helpList);
    }

    public String getHelpReload() {
        return colorize(helpReload);
    }

    public String getUsageAdd() {
        return colorize(prefix + usageAdd);
    }

    public String getUsageRemove() {
        return colorize(prefix + usageRemove);
    }

    public String header(String version) {
        return colorize(header.replace("{version}", version));
    }

    public String author(String name) {
        return colorize(author.replace("{name}", name));
    }

    public String entityAdded(String name) {
        return colorize(prefix + entityAdded.replace("{name}", name));
    }

    public String entityRemoved(String name) {
        return colorize(prefix + entityRemoved.replace("{name}", name));
    }

    public String entityAlreadyDisabled(String name) {
        return colorize(prefix + entityAlreadyDisabled.replace("{name}", name));
    }

    public String entityNotDisabled(String name) {
        return colorize(prefix + entityNotDisabled.replace("{name}", name));
    }

    public String entityNotLiving(String name) {
        return colorize(prefix + entityNotLiving.replace("{name}", name));
    }

    public String invalidEntityType(String name) {
        return colorize(prefix + invalidEntityType.replace("{name}", name));
    }

    public String disabledEntitiesHeader(int count) {
        return colorize(prefix + disabledEntitiesHeader.replace("{count}", String.valueOf(count)));
    }

    public String entityList(String list) {
        return colorize(entityList.replace("{list}", list));
    }

    public String listSeparator() {
        return colorize(listSeparator);
    }

    private String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
