package org.fountainmc;

import java.io.File;
import java.net.InetSocketAddress;
import org.apache.logging.log4j.LogManager;
import net.minecraft.server.MinecraftServer;
import org.fountainmc.api.Server;
import org.fountainmc.api.ServerInfo;
import org.fountainmc.api.plugin.PluginManager;

public class WetServer implements Server {

    private PluginManager pluginManager;
    private final String[] launchArguments;
    
    public WetServer(String[] args) {
        this.launchArguments = args;
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

    @Override public int getMaxPlayers() {
        return MinecraftServer.getDedicatedServer().getMaxPlayers();
    }

    @Override public String getOwner() {
        return MinecraftServer.getDedicatedServer().getServerOwner();
    }

    @Override public InetSocketAddress getAddress() {
        return new InetSocketAddress(MinecraftServer.getDedicatedServer().getHostname(), MinecraftServer.getDedicatedServer().getPort());
    }

    @Override public ServerInfo getServerInfo() {
        return this;
    }

    @Override public String[] getLaunchArguments() {
        return launchArguments;
    }
}
