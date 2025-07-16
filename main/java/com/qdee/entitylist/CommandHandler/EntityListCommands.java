
package com.qdee.entitylist.commandhandler;

import com.qdee.entitylist.service.EntityService;
import com.qdee.entitylist.util.FileManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.entity.SpawnGroup;



import java.util.List;
import java.util.Map;






public class EntityListCommands {
    
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("entitylist")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("groups")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    List<String> groupNames = EntityService.getAllGroupNames();
                    groupNames.forEach(name ->
                        source.sendFeedback(() -> Text.literal(name).formatted(Formatting.AQUA), false));
                    return 1;
                }))
            .then(CommandManager.literal("all")
                .then(CommandManager.literal("export")
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        MinecraftServer server = source.getServer();
        
                        try {
                            Map<String, List<String>> entities = EntityService.getAllEntities(server);
                            FileManager.exportToJsonMAP(entities, "all_entities_list.json", server);
                            source.sendFeedback(() -> Text.literal("All entities exported to " + FileManager.getFilePath("all_entities_list.json"))
                                .formatted(Formatting.GREEN), false);
                            // FileManager already sends success message
                        } catch (Exception e) {
                            source.sendFeedback(() -> Text.literal("Error exporting all entities list: " + e.getMessage())
                                .formatted(Formatting.RED), false);
                            e.printStackTrace();
                        }

                        return 1;
                    })))
            .then(CommandManager.literal("all")
                .then(CommandManager.literal("print")
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        Map<String, List<String>> groupedEntities = EntityService.getAllEntities(source.getServer());
                        groupedEntities.forEach((group, entities) -> {
                            source.sendFeedback(() -> Text.literal("=== " + group.toUpperCase() + " ===").formatted(Formatting.WHITE), false);
                            entities.forEach(entity -> source.sendFeedback(() -> Text.literal("  " + entity).formatted(Formatting.AQUA), false));
                        });
                        return 1;
                })))
            .then(CommandManager.literal("hostile")
                .then(CommandManager.literal("export")
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        MinecraftServer server = source.getServer();
                        
                        try {
                            List<String> entities = EntityService.getHostileEntities(server);
                            FileManager.exportToJson(entities, "all_hostile_list.json", server);
                            source.sendFeedback(() -> Text.literal("Hostiles list exported to " + FileManager.getFilePath("all_hostile_list.json"))
                                .formatted(Formatting.GREEN), false);
                            // FileManager already sends success message
                        } catch (Exception e) {
                            source.sendFeedback(() -> Text.literal("Error exporting all hostile list: " + e.getMessage())
                                .formatted(Formatting.RED), false);
                            e.printStackTrace();
                        }

                        return 1;
                    })))
            .then(CommandManager.literal("hostile")
                .then(CommandManager.literal("print")
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        List<String> entities = EntityService.getHostileEntities(source.getServer());
                        entities.forEach(entity -> source.sendFeedback(() -> Text.literal(entity).formatted(Formatting.RED), false));
                        return 1;
                    })))
            .then(CommandManager.literal("creature")
                .then(CommandManager.literal("export")
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        MinecraftServer server = source.getServer();
                        
                        try {
                            List<String> entities = EntityService.getCreatureEntities(server);
                            FileManager.exportToJson(entities, "all_creature_list.json", server);
                            source.sendFeedback(() -> Text.literal("Creatures list exported to " + FileManager.getFilePath("all_creature_list.json"))
                                .formatted(Formatting.GREEN), false);
                            // FileManager already sends success message
                        } catch (Exception e) {
                            source.sendFeedback(() -> Text.literal("Error exporting all creature list: " + e.getMessage())
                                .formatted(Formatting.RED), false);
                            e.printStackTrace();
                        }

                        return 1;
                        
                    })))
            .then(CommandManager.literal("creature")
                .then(CommandManager.literal("print")
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        List<String> entities = EntityService.getCreatureEntities(source.getServer());
                        entities.forEach(entity -> source.sendFeedback(() -> Text.literal(entity).formatted(Formatting.GREEN), false));
                        return 1;
                    })))
            .then(CommandManager.literal("help")
                .executes(this::helpCommand)));
    }

    private int helpCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        source.sendFeedback(() -> Text.literal("=== EntityList Commands ===")
            .formatted(Formatting.BOLD, Formatting.YELLOW), false);
        source.sendFeedback(() -> Text.literal("/entitylist all export - Export entities to JSON file")
            .formatted(Formatting.AQUA), false);
        source.sendFeedback(() -> Text.literal("/entitylist all print - Print all entities in chat")
            .formatted(Formatting.AQUA), false);
        source.sendFeedback(() -> Text.literal("/entitylist hostile export  - Export hostiles to JSON file")
            .formatted(Formatting.RED), false);
        source.sendFeedback(() -> Text.literal("/entitylist hostile print - Print all hostile entities in chat")
            .formatted(Formatting.RED), false);
        source.sendFeedback(() -> Text.literal("/entitylist creature export - Export all creatures to JSON file")
            .formatted(Formatting.GREEN), false);
        source.sendFeedback(() -> Text.literal("/entitylist creature print - Print all creature entities in chat")
            .formatted(Formatting.GREEN), false);
        source.sendFeedback(() -> Text.literal("/entitylist help - Show this help message")
            .formatted(Formatting.WHITE), false);  
        return 1;
    }
}
