package org.fountainmc.world;

import net.minecraft.world.chunk.Chunk;

import org.fountainmc.AsyncCatcher;
import org.fountainmc.world.block.WetBlockState;

import static com.google.common.base.Preconditions.checkArgument;

public class WetChunk implements org.fountainmc.api.world.Chunk {

    private final Chunk handle;
    private final WetWorld world;

    public WetChunk(WetWorld world, Chunk handle) {
        this.handle = handle;
        this.world = world;
    }

    @Override
    public int getX() {
        return handle.xPosition;
    }

    @Override
    public int getZ() {
        return handle.zPosition;
    }

    @Override
    public WetWorld getWorld() {
        return world;
    }

    @Override
    public WetBlockState getBlockAt(int x, int y, int z) {
        checkArgument(x >> 4 == this.getX(), "X position %s isn't in chunk %s", x, this);
        checkArgument(z >> 4 == this.getZ(), "Z position %s isn't in chunk %s", z, this);
        checkArgument(y > 0, "Negative y position %s", y);
        AsyncCatcher.checkAsyncOp("block access");
        return handle.getBlockState(x, y, z).getFountainState();
    }

    public Chunk getHandle() {
        return handle;
    }

    @Override
    public String toString() {
        return getWorld().getName() + ":" + getX() + "," + getZ();
    }
}
