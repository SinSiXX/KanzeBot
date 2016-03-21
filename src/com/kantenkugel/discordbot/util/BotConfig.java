package com.kantenkugel.discordbot.util;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BotConfig {
    private static final Path configPath = Paths.get("kanzeBotConfig.json");
    private static JSONObject config;

    public static void set(@NotNull String key, @Nullable Object val) {
        if(val == null) {
            config.remove(key);
        } else {
            config.put(key, val);
        }
        save();
    }

    public static <T> T get(@NotNull String key) {
        if(config.has(key)) {
            try {
                return (T) config.get(key);
            } catch(JSONException | ClassCastException ignored) {}
        }
        return null;
    }

    public static <T> T get(@NotNull String key, @NotNull T def) {
        T obj = get(key);
        if(obj == null) {
            obj = def;
            config.put(key, def);
            save();
        }
        return obj;
    }

    public static void save() {
        try {
            Files.write(configPath, config.toString(4).getBytes(StandardCharsets.UTF_8));
        } catch(IOException ignored) {
        }
    }

    public static boolean load() {
        boolean exists = Files.exists(configPath);
        try {
            if(!exists) {
                Files.write(configPath, "{}".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
            }
            config = new JSONObject(new String(Files.readAllBytes(configPath), StandardCharsets.UTF_8));
        } catch(IOException ignored) {
        }
        return exists;
    }

    private BotConfig() {
    }
}
