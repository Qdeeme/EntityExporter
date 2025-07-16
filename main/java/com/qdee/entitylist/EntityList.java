package com.qdee.entitylist;

import com.qdee.entitylist.service.EntityService;
import com.qdee.entitylist.commandhandler.EntityListCommands;

import  net.fabricmc.api.ModInitializer;
import  net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import  net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import  net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import  net.minecraft.resource.ResourceType;
import  net.minecraft.resource.ResourceManager;
import  net.minecraft.server.command.ServerCommandSource;
import  net.minecraft.server.MinecraftServer;

public class EntityList implements ModInitializer {

    @Override
    public void onInitialize() {
        System.out.println("[EntityList] Mod initializing...");

        // Register resource reload listener
        ServerLifecycleEvents.SERVER_STARTED.register(new EntityService() ::onServerStarted);
        // Register command
        CommandRegistrationCallback.EVENT.register((new EntityListCommands() ::register));
    }
}