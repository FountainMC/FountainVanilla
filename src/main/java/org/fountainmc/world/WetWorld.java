package org.fountainmc.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.fountainmc.AsyncCatcher;
import org.fountainmc.WetServer;
import org.fountainmc.api.world.Chunk;
import org.fountainmc.world.block.WetBlockState;

import static com.google.common.base.Preconditions.*;

public class WetWorld implements org.fountainmc.api.world.World {
    private final WetServer server;
    private World world;

    public WetWorld(WetServer server, World world) {
        this.server = checkNotNull(server, "Null server");
        this.world = checkNotNull(world, "Null world");
    }

    @Override
    public String getName() {
        return world.getWorldInfo().getWorldName();
    }

    @Override
    public Chunk getChunk(int x, int y) {
        AsyncCatcher.checkAsyncOp("chunk access");
        return new WetChunk(this, world.getChunkFromChunkCoords(x, y));
    }

    @Override
    public WetBlockState getBlockAt(int x, int y, int z) {
        AsyncCatcher.checkAsyncOp("block access");
        return world.getBlockState(new BlockPos(x, y, z)).getFountainState();
    }

}
