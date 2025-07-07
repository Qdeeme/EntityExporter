package com.example.moblist;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * MobListMod is a Fabric client-side mod that provides a /moblist command to display
 * a list of all mob entities registered in the game. The mod also automatically
 * exports the mob list to a JSON file when joining a world.
 *
 * Usage:
 *   /moblist - Lists all mobs in chat.
 *   
 * The mob list is automatically exported to moblist.json when you join a world.
 */
public class MobListMod implements ClientModInitializer {

    private static Map<String, String> cachedMobs = null;

    @Override
    public void onInitializeClient() {
        System.out.println("[MobListMod] Client mod initializing...");

        // Register client-side command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("moblist")
                .executes(context -> {
                    printMobListToChat();
                    return 1;
                })
            );
        });

        // Register world join event to auto-export mob list
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            // Run after a short delay to ensure everything is loaded
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // 2 second delay
                    exportMobListToFile();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        });

        System.out.println("[MobListMod] Client command registered: /moblist");
        System.out.println("[MobListMod] Auto-export on world join enabled");
        
        // Cache the mob list on initialization
        getCachedMobList();
        System.out.println("[MobListMod] Cached " + cachedMobs.size() + " mobs on initialization.");
        System.out.println("[MobListMod] Client mod initialized successfully.");
    }

    /**
     * Prints the list of all registered mobs to the in-game chat.
     */
    private void printMobListToChat() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) {
            System.out.println("[MobListMod] No player found to send messages to");
            return;
        }

        Map<String, String> mobs = getCachedMobList();

        // Send header
        client.player.sendMessage(Text.literal("§6[MobListMod] Found " + mobs.size() + " mobs:"), false);
        client.player.sendMessage(Text.literal("§7────────────────────────────────"), false);

        // Send mob list to chat
        for (Map.Entry<String, String> entry : mobs.entrySet()) {
            client.player.sendMessage(Text.literal("§a" + entry.getKey() + " §7(" + entry.getValue() + ")"), false);
        }

        // Send footer
        client.player.sendMessage(Text.literal("§7────────────────────────────────"), false);
        client.player.sendMessage(Text.literal("§6Total: " + mobs.size() + " mobs"), false);
    }

    /**
     * Returns a cached map of all registered mob entities.
     * Key: EntityType ID (e.g., "minecraft:zombie")
     * Value: Class simple name (e.g., "ZombieEntity")
     */
    private static Map<String, String> getCachedMobList() {
        if (cachedMobs == null) {
            cachedMobs = new HashMap<>();
            for (EntityType<?> type : Registries.ENTITY_TYPE) {
                if (MobEntity.class.isAssignableFrom(type.getBaseClass())) {
                    cachedMobs.put(Registries.ENTITY_TYPE.getId(type).toString(), type.getBaseClass().getSimpleName());
                }
            }
        }
        return cachedMobs;
    }

    /**
     * Exports the mob list to a JSON file in the world directory or game directory.
     * Creates nested directories as needed.
     */
    private void exportMobListToFile() {
        MinecraftClient client = MinecraftClient.getInstance();

        // Get world directory (if in world) or game directory
        File targetDir;
        String worldName = "unknown";
        
        if (client.getServer() != null && client.getServer().getSaveProperties() != null) {
            // Integrated server (singleplayer world) - use world folder
            worldName = client.getServer().getSaveProperties().getLevelName();
            targetDir = new File(client.runDirectory, "saves/" + worldName);
        } else {
            // Fallback to game directory
            targetDir = client.runDirectory;
        }

        // Create nested directory structure: moblist/exports/
        File exportDir = new File(targetDir, "moblist/exports");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        Map<String, String> mobs = getCachedMobList();

        // Create filename with timestamp
        String timestamp = java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
        );
        File outputFile = new File(exportDir, "moblist_" + timestamp + ".json");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(outputFile)) {
            gson.toJson(mobs, writer);

            System.out.println("[MobListMod] Auto-exported " + mobs.size() + " mobs to: " + outputFile.getAbsolutePath());

            // Send success message to player if they're in-game
            if (client.player != null) {
                client.player.sendMessage(Text.literal("§a[MobListMod] Mob list auto-exported with " + mobs.size() + " mobs!"), false);
                client.player.sendMessage(Text.literal("§7File saved to: moblist/exports/moblist_" + timestamp + ".json"), false);
            }

        } catch (IOException e) {
            System.out.println("[MobListMod] Failed to export mob list:");
            e.printStackTrace();

            // Send error message to player if they're in-game
            if (client.player != null) {
                client.player.sendMessage(Text.literal("§c[MobListMod] Failed to auto-export mob list! Check console for details."), false);
            }
        }
    }
}