package org.fountainmc.world.block;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import org.fountainmc.NMSConverters;
import org.fountainmc.WetServer;
import org.fountainmc.api.Direction;
import org.fountainmc.api.world.block.Chest;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@BlockStateImpl("chest")
public class WetChest extends WetBlockState implements Chest {

    public WetChest(WetServer server, IBlockState handle) {
        super(server, handle);
    }

    @Override
    public Direction getDirection() {
        return NMSConverters.toFountainDirection(getHandle().getValue(BlockChest.FACING));
    }

    @Override
    public WetChest withDirection(Direction direction) {
        checkArgument(!checkNotNull(direction, "Null direction").isVertical(), "Direction %s is vertical");
        return (WetChest) getHandle().withProperty(BlockChest.FACING, NMSConverters.fromFountainDirection(direction)).getFountainState();
    }

}
