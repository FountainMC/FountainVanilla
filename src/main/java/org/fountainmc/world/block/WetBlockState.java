package org.fountainmc.world.block;

import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.function.BiFunction;

import javax.annotation.Nonnull;

import com.google.common.base.Verify;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import org.fountainmc.WetServer;
import org.fountainmc.api.BlockType;
import org.fountainmc.api.world.block.BlockState;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

public class WetBlockState implements BlockState {

    private final WetServer server;
    @Getter private final IBlockState handle;

    @Getter private static ImmutableMap<Block, BiFunction<WetServer, IBlockState, ? extends WetBlockState>> factories;

    private static ImmutableMap<Block, BiFunction<WetServer, IBlockState, ? extends WetBlockState>> scanClasspath() {
        ImmutableMap.Builder<Block, BiFunction<WetServer, IBlockState, ? extends WetBlockState>> builder = ImmutableMap.builder();
        Reflections reflections = new Reflections(getReflectionsConfiguration("org.fountainmc.world.block"));
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(BlockStateImpl.class);
        if (types != null) {
            for (Class<?> type : types) {
                Verify.verify(WetBlockState.class.isAssignableFrom(type), "Class %s isn't instanceof WetBlockState", type.getTypeName());
                for (String blockName : ImmutableList.copyOf(type.getAnnotation(BlockStateImpl.class).value())) {
                    Block block = Verify.verifyNotNull(Block.getBlockFromName(blockName),
                            "Class %s specified unknown block name minecraft:%s.", type.getTypeName(), blockName);
                    builder.put(block, (server, state) -> {
                        try {
                            Constructor<?> constructor = type.getConstructor(WetServer.class, IBlockState.class);
                            return (WetBlockState) constructor.newInstance(server, state);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    });
                }
            }
        }
        return builder.build();
    }

    public static ConfigurationBuilder getReflectionsConfiguration(String packageName) {
        return new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(packageName))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName + ".")))
                .setScanners(new TypeElementsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
    }

    public static void createFactories() {
        factories = scanClasspath();
    }

    public static WetBlockState createState(WetServer server, IBlockState handle) {
        if (factories == null)
            createFactories();
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
