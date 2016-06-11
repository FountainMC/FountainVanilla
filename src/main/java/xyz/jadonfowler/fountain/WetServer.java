package xyz.jadonfowler.fountain;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import net.minecraft.server.MinecraftServer;
import xyz.jadonfowler.fountain.api.Server;
import xyz.jadonfowler.fountain.api.plugin.PluginManager;

public class WetServer implements Server {

    private PluginManager pluginManager;

    public WetServer() {
        pluginManager = new PluginManager();
        LogManager.getLogger().info("Loading plugins...");
        pluginManager.loadPlugins(new File("plugins"));
    }

    @Override public String getName() {
        return MinecraftServer.getDedicatedServer().getName();
    }

    @Override public String getVersion() {
        return MinecraftServer.getDedicatedServer().getMinecraftVersion();
    }

    @Override public String getMotd() {
        return MinecraftServer.getDedicatedServer().getMotd();
    }

    @Override public PluginManager getPluginManager() {
        return pluginManager;
    }

}
