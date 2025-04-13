package com.djtmk.disableai.commands;

import com.djtmk.disableai.DisableAI;
import com.djtmk.disableai.manager.AIManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DisableAICommand implements CommandExecutor {
    private DisableAI plugin;
    private AIManager aiManager;

    public DisableAICommand(DisableAI plugin, AIManager aiManager) {
        this.plugin = plugin;
        this.aiManager = aiManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String version = "1.1";
        String author = "djtmk";

        if (args.length < 1) {
            sender.sendMessage(ChatColor.DARK_GRAY + "» " + ChatColor.DARK_PURPLE + "DisableAI " + ChatColor.GRAY
                    + ChatColor.GRAY + "v" + version);
            if (sender.hasPermission("disableai.reload")) {
                sender.sendMessage(ChatColor.GRAY + "/ai reload " + ChatColor.DARK_GRAY + "-" + ChatColor.RESET
                        + " Reload plugin");
            }
            sender.sendMessage(ChatColor.DARK_GRAY + "Author: " + ChatColor.GRAY + author);
            return true;
        }
        if (args.length == 1) {
            if (args[0].toLowerCase().equals("reload")) {
                if (sender.hasPermission("disableai.reload")) {
                    plugin.reloadConfig();
                    aiManager.loadDisabledEntities();
                    aiManager.update();
                    sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_PURPLE + "DisableAI"
                            + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " Config reloaded!");
                } else {
                    sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_PURPLE + "DisableAI"
                            + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " No Permission!");
                }
            }
        }
        return true;
    }
}