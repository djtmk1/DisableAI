package com.djtmk.disableai.manager;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

@RequiredArgsConstructor
public final class AIManager {

    private final JavaPlugin plugin;
    private final Set<EntityType> disabledEntities = EnumSet.noneOf(EntityType.class);

    public Set<EntityType> getDisabledEntities() {
        return Collections.unmodifiableSet(disabledEntities);
    }

    public void loadDisabledEntities() {
        disabledEntities.clear();
        plugin.getConfig().getStringList("settings.disabledEntities").stream()
                .map(this::parseEntityType).flatMap(Optional::stream).forEach(disabledEntities::add);
    }

    public Optional<EntityType> parseEntityType(String name) {
        try {
            return Optional.of(EntityType.valueOf(name.toUpperCase()));
        } catch (IllegalArgumentException illegalArgumentException) {
            plugin.getLogger().log(Level.WARNING, "Invalid entity type: {0}", name);
            return Optional.empty();
        }
    }

    public void updateAllEntities() {
        loadDisabledEntities();
        plugin.getServer().getWorlds().stream().flatMap(world ->
                world.getLivingEntities().stream()).forEach(this::updateEntityAI);
    }

    public void updateEntityAI(LivingEntity entity) {
        boolean shouldDisable = disabledEntities.contains(entity.getType());
        if (entity.hasAI() == shouldDisable) {
            entity.setAI(!shouldDisable);
        }
    }

    public boolean isAIDisabled(EntityType type) {
        return disabledEntities.contains(type);
    }

    public boolean addDisabledEntity(EntityType type) {
        if (disabledEntities.add(type)) {
            saveToConfigAsync();
            applyToExistingEntities(type, false);
            return true;
        }
        return false;
    }

    public boolean removeDisabledEntity(EntityType type) {
        if (disabledEntities.remove(type)) {
            saveToConfigAsync();
            applyToExistingEntities(type, true);
            return true;
        }
        return false;
    }

    private void applyToExistingEntities(EntityType type, boolean enableAI) {
        plugin.getServer().getWorlds().stream().flatMap(world ->
                world.getLivingEntities().stream()).filter(entity ->
                entity.getType() == type).forEach(entity -> entity.setAI(enableAI));
    }

    private void saveToConfigAsync() {
        plugin.getConfig().set("settings.disabledEntities", disabledEntities.stream().map(EntityType::name).sorted().toList());
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, plugin::saveConfig);
    }
}
