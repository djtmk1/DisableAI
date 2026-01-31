package com.djtmk.disableai.commands;

import com.djtmk.disableai.DisableAI;
import com.djtmk.disableai.config.Messages;
import com.djtmk.disableai.config.Permissions;
import com.djtmk.disableai.manager.AIManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class DisableAICommand implements CommandExecutor {

    private final DisableAI plugin;
    private final AIManager aiManager;
    private final Messages messages;

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (args.length == 0) {
            sendPluginInfo(sender);
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "reload" -> handleReload(sender);
            case "add" -> handleAdd(sender, args);
            case "remove" -> handleRemove(sender, args);
            case "list" -> handleList(sender);
            default -> sendPluginInfo(sender);
        }
        return true;
    }

    private void sendPluginInfo(CommandSender sender) {
        sender.sendMessage(messages.header(plugin.getDescription().getVersion()));

        if (sender.hasPermission(Permissions.MANAGE)) {
            sender.sendMessage(messages.getHelpAdd());
            sender.sendMessage(messages.getHelpRemove());
            sender.sendMessage(messages.getHelpList());
        }
        if (sender.hasPermission(Permissions.RELOAD)) {
            sender.sendMessage(messages.getHelpReload());
        }
        sender.sendMessage(messages.author(String.join(", ", plugin.getDescription().getAuthors())));
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission(Permissions.RELOAD)) {
            sender.sendMessage(messages.getNoPermission());
            return;
        }
        plugin.reloadAllConfigs();
        sender.sendMessage(messages.getConfigReloaded());
    }

    private void handleAdd(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permissions.MANAGE)) {
            sender.sendMessage(messages.getNoPermission());
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(messages.getUsageAdd());
            return;
        }
        aiManager.parseEntityType(args[1]).ifPresentOrElse(entityType -> processAddEntity(sender, entityType), () -> sender.sendMessage(messages.invalidEntityType(args[1])));
    }

    private void processAddEntity(CommandSender sender, EntityType entityType) {
        if (!entityType.isAlive()) {
            sender.sendMessage(messages.entityNotLiving(entityType.name()));
            return;
        }
        if (aiManager.addDisabledEntity(entityType)) {
            sender.sendMessage(messages.entityAdded(entityType.name()));
        } else {
            sender.sendMessage(messages.entityAlreadyDisabled(entityType.name()));
        }
    }

    private void handleRemove(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permissions.MANAGE)) {
            sender.sendMessage(messages.getNoPermission());
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(messages.getUsageRemove());
            return;
        }
        aiManager.parseEntityType(args[1]).ifPresentOrElse(entityType -> processRemoveEntity(sender, entityType), () -> sender.sendMessage(messages.invalidEntityType(args[1])));
    }

    private void processRemoveEntity(CommandSender sender, EntityType entityType) {
        if (aiManager.removeDisabledEntity(entityType)) {
            sender.sendMessage(messages.entityRemoved(entityType.name()));
        } else {
            sender.sendMessage(messages.entityNotDisabled(entityType.name()));
        }
    }

    private void handleList(CommandSender sender) {
        if (!sender.hasPermission(Permissions.MANAGE)) {
            sender.sendMessage(messages.getNoPermission());
            return;
        }
        Set<EntityType> disabled = aiManager.getDisabledEntities();

        if (disabled.isEmpty()) {
            sender.sendMessage(messages.getNoEntitiesDisabled());
            return;
        }
        String list = disabled.stream().map(EntityType::name).sorted().collect(Collectors.joining(messages.listSeparator()));

        sender.sendMessage(messages.disabledEntitiesHeader(disabled.size()));
        sender.sendMessage(messages.entityList(list));
    }
}

