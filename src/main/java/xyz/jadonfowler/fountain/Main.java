package xyz.jadonfowler.fountain;

import net.minecraft.server.MinecraftServer;
import xyz.jadonfowler.fountain.api.Fountain;

public class Main {

    public static void main(String[] args) {
        WaterServer server = new WaterServer();
        Fountain.setServer(server);
        MinecraftServer.main(args);
    }
}
