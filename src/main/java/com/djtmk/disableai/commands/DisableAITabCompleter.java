package com.djtmk.disableai.commands;

import com.djtmk.disableai.config.Permissions;
import com.djtmk.disableai.manager.AIManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public final class DisableAITabCompleter implements TabCompleter {

    private static final List<String> LIVING_ENTITIES = Arrays.stream(EntityType.values())
            .filter(EntityType::isAlive)
            .map(EntityType::name)
            .sorted()
            .toList();

    private final AIManager aiManager;

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command,
                                      @NonNull String alias, String[] args) {
        return switch (args.length) {
            case 1 -> getFirstArgCompletions(sender, args[0]);
            case 2 -> getSecondArgCompletions(sender, args);
            default -> Collections.emptyList();
        };
    }

    private List<String> getFirstArgCompletions(CommandSender sender, String input) {
        ArrayList<String> commands = new java.util.ArrayList<String>();

        if (sender.hasPermission(Permissions.MANAGE)) {
            commands.addAll(List.of("add", "remove", "list"));
        }
        if (sender.hasPermission(Permissions.RELOAD)) {
            commands.add("reload");
        }
        return filterStartsWith(commands, input);
    }

    private List<String> getSecondArgCompletions(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permissions.MANAGE)) {
            return Collections.emptyList();
        }
        return switch (args[0].toLowerCase()) {
            case "add" -> filterStartsWith(getAvailableEntities(), args[1]);
            case "remove" -> filterStartsWith(getDisabledEntityNames(), args[1]);
            default -> Collections.emptyList();
        };
    }

    private List<String> getAvailableEntities() {
        return LIVING_ENTITIES.stream()
                .filter(name -> !aiManager.getDisabledEntities().contains(EntityType.valueOf(name)))
                .toList();
    }

    private List<String> getDisabledEntityNames() {
        return aiManager.getDisabledEntities().stream()
                .map(EntityType::name)
                .sorted()
                .toList();
    }

    private List<String> filterStartsWith(List<String> list, String prefix) {
        return list.stream()
                .filter(s -> s.toLowerCase().startsWith(prefix.toLowerCase()))
                .toList();
    }
}
