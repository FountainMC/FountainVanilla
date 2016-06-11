package xyz.jadonfowler.fountain.world.block;

import net.minecraft.util.math.BlockPos;
import xyz.jadonfowler.fountain.api.world.Location;
import xyz.jadonfowler.fountain.api.world.World;
import xyz.jadonfowler.fountain.api.world.block.Block;
import xyz.jadonfowler.fountain.world.WetChunk;

public class WetBlock implements Block {

    private final WetChunk chunk;
    private final int x;
    private final int y;
    private final int z;

    public WetBlock(WetChunk chunk, int x, int y, int z) {
        this.chunk = chunk;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override public int getId() {
        return net.minecraft.block.Block
                .getIdFromBlock(chunk.getHandle().getBlockState(new BlockPos(x, y, z)).getBlock());
    }

    @Override public int getX() {
        return x;
    }

    @Override public int getY() {
        return y;
    }

    @Override public int getZ() {
        return z;
    }

    @Override public Location getLocation() {
        return new Location(getWorld(), x, y, z);
    }

    private World getWorld() {
        return chunk.getWorld();
    }

}
