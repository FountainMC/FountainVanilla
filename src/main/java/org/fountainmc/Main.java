package org.fountainmc;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.fountainmc.api.Fountain;

public class Main {

    public static void main(String[] args) {
        LogManager.getLogger().info("Starting Fountain...");
        WetServer server = new WetServer(args);
        Fountain.setServer(server);
        MinecraftServer.main(args);
    }

}
