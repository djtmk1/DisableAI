package com.djtmk.disableai.events;

import com.djtmk.disableai.manager.AIManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class Events implements Listener {
    private AIManager aiManager;

    public Events(AIManager aiManager) {
        this.aiManager = aiManager;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            if (aiManager.getDisabledEntities().contains(entity.getType())) {
                entity.setAI(false);
            }
        }
    }
}
