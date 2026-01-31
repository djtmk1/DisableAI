package com.djtmk.disableai.events;

import com.djtmk.disableai.manager.AIManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

@RequiredArgsConstructor
public final class EntitySpawnListener implements Listener {

    private final AIManager aiManager;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!aiManager.isAIDisabled(event.getEntityType())) {
            return;
        }
        event.getEntity().setAI(false);
    }
}
