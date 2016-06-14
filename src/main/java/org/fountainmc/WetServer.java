package org.fountainmc;

import java.io.File;
import java.net.InetSocketAddress;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.LogManager;
import org.fountainmc.api.Fountain;
import org.fountainmc.api.Material;
import org.fountainmc.api.Server;
import org.fountainmc.api.command.CommandManager;
import org.fountainmc.api.plugin.PluginManager;

import static com.google.common.base.Preconditions.*;

@ParametersAreNonnullByDefault
public class WetServer implements Server {
    private final PluginManager pluginManager;
    private final ImmutableList<String> launchArguments;

    public WetServer(String[] args) {
        pluginManager = new PluginManager();
        this.launchArguments = ImmutableList.copyOf(args);
        LogManager.getLogger().info("Loading plugins...");
        pluginManager.loadPlugins(new File("plugins"));
    }

    @Override
    public String getName() {
        return MinecraftServer.getDedicatedServer().getName();
    }

    @Override
    public String getVersion() {
        return MinecraftServer.getDedicatedServer().getMinecraftVersion();
    }

    @Override
    public String getMotd() {
        return MinecraftServer.getDedicatedServer().getMotd();
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public CommandManager getCommandManager() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxPlayers() {
        return MinecraftServer.getDedicatedServer().getMaxPlayers();
    }

    @Override
    public String getOwner() {
        return MinecraftServer.getDedicatedServer().getServerOwner();
    }

    @Override
    public InetSocketAddress getAddress() {
        return new InetSocketAddress(MinecraftServer.getDedicatedServer().getHostname(), MinecraftServer.getDedicatedServer().getPort());
    }

    @Override
    public ImmutableList<String> getLaunchArguments() {
        return launchArguments;
    }

    @Override
    public Material getMaterial(String name) {
        Block block = Block.getBlockFromName(checkNotNull(name, "Null name"));
        if (block == null) {
            Item item = Item.getByNameOrId(name);
            if (item == null) {
                throw new IllegalArgumentException("Unknown material name " + name);
            } else {
                return item.getFountainType();
            }
        } else {
            return block.getFountainType();
        }
    }

    @Override
    public WetBlockType getBlockType(String name) {
        Block block = Block.getBlockFromName(checkNotNull(name, "Null name"));
        checkArgument(block != null, "Unknown block name %s", name);
        return block.getFountainType();
    }

    public WetItemType getItemType(String name) {
        Item item = Item.getByNameOrId(checkNotNull(name, "Null name"));
        checkArgument(item != null, "Unknown item name %s", name);
        return item.getFountainType();
    }

    public static WetServer getInstance() {
        return (WetServer) Fountain.getServer();
    }
}
