package org.fountainmc.world;

import lombok.*;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.fountainmc.AsyncCatcher;
import org.fountainmc.WetServer;
import org.fountainmc.api.world.Chunk;
import org.fountainmc.api.world.block.BlockState;
import org.fountainmc.world.block.WetBlockState;

import static com.google.common.base.Preconditions.checkNotNull;

public class WetWorld implements org.fountainmc.api.world.World {

    @Getter
    private final WetServer server;
    private final World world;

    @NonNull
    public World getHandle() {
        return world;
    }

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
    public WetBlockState getBlock(int x, int y, int z) {
        AsyncCatcher.checkAsyncOp("block access");
        return world.getBlockState(new BlockPos(x, y, z)).getFountainState();
    }

    @Override
    public void setBlock(int x, int y, int z, BlockState state) {
        world.setBlockState(new BlockPos(x, y, z), ((WetBlockState) state).getHandle());
    }

    @Override
    public String toString() {
        return getName();
    }
}
