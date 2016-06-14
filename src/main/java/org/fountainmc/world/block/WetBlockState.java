package org.fountainmc.world.block;

import lombok.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;

import com.google.common.base.Verify;
import com.google.common.base.VerifyException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.techcable.pineapple.SneakyThrow;

import org.fountainmc.WetServer;
import org.fountainmc.api.BlockType;
import org.fountainmc.api.world.block.BlockState;
import org.reflections.Reflections;

import static com.google.common.base.Preconditions.*;

public class WetBlockState implements BlockState {
    private final WetServer server;
    @Getter
    private final IBlockState handle;

    @Getter(lazy = true)
    private static final ImmutableMap<Block, BiFunction<WetServer, IBlockState, ? extends WetBlockState>> factories = scanClasspath();

    @SneakyThrows(IllegalAccessException.class)
    private static ImmutableMap<Block, BiFunction<WetServer, IBlockState, ? extends WetBlockState>> scanClasspath() {
        ImmutableMap.Builder<Block, BiFunction<WetServer, IBlockState, ? extends WetBlockState>> builder = ImmutableMap.builder();
        for (Class<?> type : Reflections.collect("org.fountainmc.world.block", (s) -> true).getTypesAnnotatedWith(BlockStateImpl.class)) {
            Verify.verify(type.isAssignableFrom(WetBlockState.class), "Class %s isn't instanceof WetBlockState", type.getTypeName());
            for (String blockName : ImmutableList.copyOf(type.getAnnotation(BlockStateImpl.class).value())) {
                Block block = Verify.verifyNotNull(Block.getBlockFromName("minecraft:" + blockName), "Class %s specified unknown block name minecraft:%s.", type.getTypeName(), blockName);
                final MethodHandle constructorHandle;
                try {
                    constructorHandle = MethodHandles.publicLookup().findConstructor(type, MethodType.methodType(type, WetServer.class, IBlockState.class));
                } catch (NoSuchMethodException e) {
                    throw new VerifyException("Can't find constructor for " + type.getTypeName());
                }
                builder.put(block, (server, state) -> {
                    try {
                        return (WetBlockState) constructorHandle.invoke(server, state);
                    } catch (Throwable throwable) {
                        throw SneakyThrow.sneakyThrow(throwable);
                    }
                });
            }
        }
        return builder.build();
    }

    public static WetBlockState createState(WetServer server, IBlockState handle) {
        return getFactories().getOrDefault(checkNotNull(handle, "Null block state").getBlock(), WetBlockState::new).apply(server, handle);
    }

    protected WetBlockState(WetServer server, IBlockState handle) {
        this.server = checkNotNull(server, "Null server");
        this.handle = checkNotNull(handle, "Null state");
    }

    @Nonnull
    @Override
    public BlockType getBlockType() {
        return handle.getBlock().getFountainType();
    }
}
