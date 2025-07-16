
package com.qdee.entitylist.service;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Collections;
import java.util.stream.StreamSupport;

public class EntityService {

    public void onServerStarted(MinecraftServer server) {
        // This method can be used to initialize or load data when the server starts
        System.out.println("[EntityList] Server started, ready to handle entity lists.");
    }
    public static List<String> getAllGroupNames() {
        return Arrays.stream(SpawnGroup.values())
                .map(SpawnGroup::getName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

public static Map<String, List<String>> getAllEntities(MinecraftServer server) {
    Registry<EntityType<?>> entityRegistry = server.getRegistryManager().get(RegistryKeys.ENTITY_TYPE);
    Map<String, List<String>> AllEntities = new HashMap<>();
    
    StreamSupport.stream(entityRegistry.spliterator(), false)
        .forEach(entry -> {
            String groupName = entry.getSpawnGroup().getName();
            String entityName = entry.getRegistryEntry().getKey()
                .map(registryKey -> registryKey.getValue().toString())
                .orElse("unknown");
            
            AllEntities.computeIfAbsent(groupName, k -> new ArrayList<>()).add(entityName);
        });
    
    // Sort each group
    AllEntities.values().forEach(Collections::sort);
    
    return AllEntities;
    }

    public static List<String> getHostileEntities(MinecraftServer server) {
        Registry<EntityType<?>> entityRegistry = server.getRegistryManager().get(RegistryKeys.ENTITY_TYPE);
        return StreamSupport.stream(entityRegistry.spliterator(), false)
                .filter(entityType -> entityType.getSpawnGroup() == SpawnGroup.MONSTER)
                .map(entityType -> entityRegistry.getKey(entityType)
                    .map(registryKey -> registryKey.getValue().toString())
                    .orElse("unknown"))
                .sorted()
                .collect(Collectors.toList());

    }

    public static List<String> getCreatureEntities(MinecraftServer server) {
        Registry<EntityType<?>> entityRegistry = server.getRegistryManager().get(RegistryKeys.ENTITY_TYPE);
        return StreamSupport.stream(entityRegistry.spliterator(), false)
                .filter(entityType -> entityType.getSpawnGroup() == SpawnGroup.CREATURE)
                .map(entityType -> entityRegistry.getKey(entityType)
                    .map(registryKey -> registryKey.getValue().toString())
                    .orElse("unknown"))
                .sorted()
                .collect(Collectors.toList());
    }
}
// This class provides methods to retrieve lists of entities based on their spawn groups.
// It uses the Minecraft server's entity registry to filter entities by their spawn group (e.g., MONSTER for hostile entities, CREATURE for animals).