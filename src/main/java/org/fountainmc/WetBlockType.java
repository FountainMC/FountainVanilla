package org.fountainmc;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.fountainmc.api.BlockType;
import org.fountainmc.world.block.WetBlockState;

import static com.google.common.base.Preconditions.checkNotNull;

public class WetBlockType implements BlockType {

    private final WetServer server;
    private final Block block;

    public WetBlockType(WetServer server, Block block) {
        this.server = checkNotNull(server, "Null server");
        this.block = checkNotNull(block, "Null block");
    }

    public Material getMaterial() {
        return block.blockMaterial;
    }

    @Override
    public boolean isFlammable() {
        return getMaterial().getCanBurn();
    }

    @Override
    public boolean isOpaque() {
        return getMaterial().isOpaque();
    }

    @Override
    public boolean isTransparent() {
        return getMaterial().blocksLight();
    }

    @Override
    public WetBlockState getDefaultState() {
        return block.getDefaultState().getFountainState();
    }

    @Override
    public int getId() {
        return Block.getIdFromBlock(block);
    }

    @Override
    public String getName() {
        return Block.REGISTRY.getNameForObject(block).toString();
    }

    @Override
    public boolean isEdible() {
        return false;
    }

    @Override
    public boolean isVanilla() {
        return true;
    }

    @Override
    public boolean isBlock() {
        return true;
    }

}
