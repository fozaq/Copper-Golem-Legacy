package com.github.smallinger.coppergolemlegacy;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.Set;

/**
 * Registriert custom Memory Module Types für den Copper Golem Brain AI
 * Diese Memory Types werden für Item Transport und andere Behaviors benötigt
 */
public class ModMemoryTypes {
    
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = 
        DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, CopperGolemLegacy.MODID);
    
    /**
     * Cooldown Timer für Item Transport zwischen Containern
     * Copper Golem wartet 60-100 Ticks zwischen Transport-Aktionen
     */
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Integer>> TRANSPORT_ITEMS_COOLDOWN_TICKS = 
        MEMORY_MODULE_TYPES.register("transport_items_cooldown_ticks", 
            () -> new MemoryModuleType<>(Optional.of(Codec.INT)));
    
    /**
     * Set von Block-Positionen die der Copper Golem bereits besucht hat
     * Verhindert dass er immer wieder die gleichen Blöcke ansteuert
     */
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Set<GlobalPos>>> VISITED_BLOCK_POSITIONS = 
        MEMORY_MODULE_TYPES.register("visited_block_positions",
            () -> new MemoryModuleType<>(Optional.of(
                GlobalPos.CODEC.listOf().xmap(Sets::newHashSet, Lists::newArrayList)
            )));
    
    /**
     * Set von Block-Positionen die der Copper Golem nicht erreichen kann
     * Verhindert dass er immer wieder versucht unerreichbare Blöcke anzusteuern
     */
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Set<GlobalPos>>> UNREACHABLE_TRANSPORT_BLOCK_POSITIONS = 
        MEMORY_MODULE_TYPES.register("unreachable_transport_block_positions",
            () -> new MemoryModuleType<>(Optional.of(
                GlobalPos.CODEC.listOf().xmap(Sets::newHashSet, Lists::newArrayList)
            )));
    
    /**
     * Cooldown Timer für Gaze/Look Behavior
     * Copper Golem schaut für eine bestimmte Zeit in eine Richtung
     */
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Integer>> GAZE_COOLDOWN_TICKS = 
        MEMORY_MODULE_TYPES.register("gaze_cooldown_ticks",
            () -> new MemoryModuleType<>(Optional.of(Codec.INT)));
}

