package org.fountainmc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.gson.annotations.SerializedName;
import net.minecraft.server.MinecraftServer;

import static com.google.common.base.Preconditions.checkNotNull;

public class FountainConfig extends Metrics {

    public FountainConfig() throws IOException {
        super("Fountain", WetServer.VERSION);
    }

    @Override
    public String getFullServerVersion() {
        return MinecraftServer.getDedicatedServer().getMinecraftVersion();
    }

    @Override
    public int getPlayersOnline() {
        return MinecraftServer.getDedicatedServer().getPlayerList().getCurrentPlayerCount();
    }

    @SerializedName("metrics") private Map<String, Object> metricsConfiguration = new HashMap<>();

    @Override
    public Map<String, Object> getMetricsConfiguration() {
        return metricsConfiguration;
    }

    public static FountainConfig load(File configFile) throws IOException {
        final FountainConfig config;
        if (!checkNotNull(configFile, "Null config file").createNewFile()) {
            config = WetServer.GSON.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream(configFile), Charsets.UTF_8)),
                    FountainConfig.class);
        } else {
            config = new FountainConfig();
        }
        WetServer.GSON.toJson(config, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), Charsets.UTF_8)));
        return config;
    }

}
