package org.fountainmc;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.util.EnumFacing;

import org.fountainmc.api.Direction;

import static com.google.common.base.Preconditions.*;

public class NMSConverters {
    private static final ImmutableMap<EnumFacing, Direction> DIRECTIONS_BY_NMS = Maps.immutableEnumMap(ImmutableMap.<EnumFacing, Direction>builder()
            .put(EnumFacing.NORTH, Direction.NORTH)
            .put(EnumFacing.EAST, Direction.EAST)
            .put(EnumFacing.SOUTH, Direction.SOUTH)
            .put(EnumFacing.WEST, Direction.WEST)
            .put(EnumFacing.UP, Direction.UP)
            .put(EnumFacing.DOWN, Direction.DOWN)
            .build());
    private static final ImmutableMap<Direction, EnumFacing> DIRECTIONS_BY_FOUNTAIN = Maps.immutableEnumMap(ImmutableBiMap.copyOf(DIRECTIONS_BY_NMS).inverse());

    public static Direction toFountainDirection(EnumFacing facing) {
        return DIRECTIONS_BY_NMS.get(checkNotNull(facing, "Null facing"));
    }

    public static EnumFacing fromFountainDirection(Direction facing) {
        return DIRECTIONS_BY_FOUNTAIN.get(checkNotNull(facing, "Null facing"));
    }
}
