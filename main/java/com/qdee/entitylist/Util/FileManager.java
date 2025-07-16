
package com.qdee.entitylist.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class FileManager {

    private static final String FOLDER_NAME = "Entity_list";

    public static void exportToJson(List<String> entities, String fileName, MinecraftServer server) {
        // Create folder if it doesn't exist
        File folder = new File(FOLDER_NAME);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        File file = new File(folder, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(entities, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportToJsonMAP(Map<String, List<String>> groupedEntities, String fileName, MinecraftServer server) {
        // Create folder if it doesn't exist
        File folder = new File(FOLDER_NAME);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        // Create file in the folder
        File file = new File(folder, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(groupedEntities, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFilePath(String fileName) {
        return new File(FOLDER_NAME, fileName).getAbsolutePath();
    }
}