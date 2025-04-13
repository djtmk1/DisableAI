package com.djtmk.disableai;

import com.djtmk.disableai.events.Events;
import com.djtmk.disableai.manager.AIManager;
import com.djtmk.disableai.commands.DisableAICommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class DisableAI extends JavaPlugin {
    public void onEnable() {
        saveDefaultConfig();
        AIManager aiManager = new AIManager(this);
        aiManager.update();
        getServer().getPluginManager().registerEvents(new Events(aiManager), this);
        getCommand("disableai").setExecutor(new DisableAICommand(this, aiManager));
    }
}
