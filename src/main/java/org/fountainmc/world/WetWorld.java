package org.fountainmc.world;

import org.fountainmc.api.world.World;
import org.fountainmc.api.world.BlockPosition;
import org.fountainmc.api.world.Chunk;

public class WetWorld implements World {

    private net.minecraft.world.World world;
    
    public WetWorld(net.minecraft.world.World world) {
        this.world = world;
    }

    @Override public String getName() {
        return "world";
    }

    @Override public Chunk getChunk(int x, int y) {
        return new WetChunk(world.getChunkFromChunkCoords(x, y));
    }

    @Override public BlockPosition getBlockAt(int x, int y, int z) {
        return new BlockPosition(this, x, y, z);
    }

}
