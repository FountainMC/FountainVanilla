package org.fountainmc;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import org.fountainmc.api.Fountain;
import org.fountainmc.api.Material;
import org.fountainmc.api.Server;
import org.fountainmc.api.command.CommandManager;
import org.fountainmc.api.entity.EntityType;
import org.fountainmc.api.event.EventManager;
import org.fountainmc.api.plugin.PluginManager;
import org.fountainmc.plugin.WetEventManager;
import org.fountainmc.plugin.WetPluginManager;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@ParametersAreNonnullByDefault
public class WetServer implements Server {

    private final EventManager eventManager;
    private final PluginManager pluginManager;
    private final ImmutableList<String> launchArguments;

    public static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .setLenient()
            .registerTypeAdapter(UUID.class, new TypeAdapter<UUID>() {

                @Override
                public void write(JsonWriter out, UUID value) throws IOException {
                    out.value(value.toString());
                }

                @Override
                public UUID read(JsonReader in) throws IOException {
                    return UUID.fromString(in.nextString());
                }

            })
            .create();

    public static final String VERSION = "1.9.4-alpha1-SNAPSHOT";

    public WetServer(String[] args) {
        this.eventManager = new WetEventManager();
        this.pluginManager = new WetPluginManager();
        this.launchArguments = ImmutableList.copyOf(args);
        try {
            FountainConfig.load(new File("fountain.json"));
        } catch (IOException e) {
            System.err.println("Couldn't load fountain.json!");
            e.printStackTrace();
            System.exit(1);
        }
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
    public EventManager getEventManager() {
        return eventManager;
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
    public Material getMaterial(int id) {
        Block block = Block.getBlockById(checkNotNull(id, "Null id"));
        if (block == null) {
            Item item = Item.getItemById(id);
            if (item == null) {
                throw new IllegalArgumentException("Unknown material id " + id);
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

    @Override
    public EntityType<?> getEntityType(String name) {
        return null;
    }

}
