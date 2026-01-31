package com.djtmk.disableai;

import com.djtmk.disableai.commands.DisableAICommand;
import com.djtmk.disableai.commands.DisableAITabCompleter;
import com.djtmk.disableai.config.Messages;
import com.djtmk.disableai.events.EntitySpawnListener;
import com.djtmk.disableai.manager.AIManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.logging.Level;

public final class DisableAI extends JavaPlugin {

    @Getter
    private AIManager aiManager;

    @Getter
    private Messages messages;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initializeConfig();
        initializeManager();
        registerListeners();
        registerCommands();
    }

    private void initializeConfig() {
        this.messages = new Messages(this);
    }

    private void initializeManager() {
        this.aiManager = new AIManager(this);
        this.aiManager.updateAllEntities();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new EntitySpawnListener(aiManager), this);
    }

    private void registerCommands() {
        Optional.ofNullable(getCommand("disableai"))
                .ifPresentOrElse(
                        command -> {
                            command.setExecutor(new DisableAICommand(this, aiManager, messages));
                            command.setTabCompleter(new DisableAITabCompleter(aiManager));
                        },
                        () -> getLogger().log(Level.SEVERE, "Failed to register 'disableai' command!")
                );
    }

    public void reloadAllConfigs() {
        reloadConfig();
        messages.loadConfig();
        aiManager.updateAllEntities();
    }
}
