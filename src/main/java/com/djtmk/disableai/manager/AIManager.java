package com.djtmk.disableai.manager;

import com.djtmk.disableai.DisableAI;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class AIManager {
    private DisableAI plugin;
    List<EntityType> disabledEntities = new ArrayList<>();

    public AIManager(DisableAI plugin) {
        this.plugin = plugin;
        loadDisabledEntities();
    }

    public void update() {
        for (World world : plugin.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    if (getDisabledEntities().contains(livingEntity.getType())) {
                        if (livingEntity.hasAI()) {
                            livingEntity.setAI(false);
                        }
                    } else {
                        if (!livingEntity.hasAI()) {
                            livingEntity.setAI(true);
                        }
                    }
                }
            }
        }
    }

    public void loadDisabledEntities() {
        disabledEntities.clear();
        for (String line : plugin.getConfig().getStringList("settings.disabledEntities")) {
            EntityType entityType = EntityType.valueOf(line.toUpperCase());
            if (entityType != null) {
                disabledEntities.add(entityType);
            }
        }
    }

    public List<EntityType> getDisabledEntities() {
        return disabledEntities;
    }
}
