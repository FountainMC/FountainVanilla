package org.fountainmc.world;

import org.fountainmc.api.world.Chunk;
import org.fountainmc.api.world.World;
import org.fountainmc.api.world.Block;

public class WetChunk implements Chunk {
    
    private final net.minecraft.world.chunk.Chunk chunk;
    private final net.minecraft.world.World world;
    private final int x;
    private final int z;
    
    public WetChunk(net.minecraft.world.chunk.Chunk chunk) {
        this.chunk = chunk;
        this.world = chunk.getWorld();
        this.x = chunk.xPosition;
        this.z = chunk.zPosition;
    }
    

    @Override public int getX() {
        return x;
    }

    @Override public int getZ() {
        return z;
    }

    @Override public World getWorld() {
        return null;
    }

    @Override public Block getBlockAt(int x, int y, int z) {
        return new Block(getWorld(), x, y, z);
    }
    
    public net.minecraft.world.chunk.Chunk getHandle() {
        return chunk;
    }

}
