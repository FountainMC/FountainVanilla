package org.fountainmc.entity;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.techcable.pineapple.collect.ImmutableSets;

import org.fountainmc.api.entity.EntityType;
import org.fountainmc.api.entity.Player;

import static com.google.common.base.Preconditions.*;

@ParametersAreNonnullByDefault
public class WetEntityPlayer extends WetEntityLiving implements Player {

    public WetEntityPlayer(EntityPlayerMP handle) {
        super(handle);
    }

    public EntityPlayerMP getHandle() {
        return (EntityPlayerMP) super.getHandle();
    }

    @Nonnull
    @Override
    public String getName() {
        return getHandle().getName();
    }

    @Nonnull
    @Override
    public UUID getUUID() {
        return getHandle().getUniqueID();
    }

    @Override
    public void sendMessage(String s) {
        getHandle().addChatMessage(new TextComponentString(s));
    }

    private final Set<Entity> hiddenEntities = Collections.newSetFromMap(new WeakHashMap<>()); // Weak hash map for no memory leaks

    @Override
    public void hide(org.fountainmc.api.entity.Entity fountainToHide) {
        Entity toHide = ((WetEntity) checkNotNull(fountainToHide, "Null entity")).getHandle();
        checkArgument(!toHide.equals(getHandle()), "Cant hide player %s from itself!", this);
        if (isConnected() && hiddenEntities.add(toHide) && getHandle().worldObj.equals(toHide.worldObj)) {
            EntityTrackerEntry trackerEntry = ((WorldServer) getHandle().worldObj).theEntityTracker.getTrackerEntry(toHide);
            if (trackerEntry != null) {
                trackerEntry.removeTrackedPlayerSymmetric(this.getHandle());
                if (toHide instanceof Player) {
                    ((EntityPlayerMP) toHide).connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.a.REMOVE_PLAYER, (EntityPlayerMP) toHide));
                }
            }
        }
    }

    @Override
    public boolean canSee(org.fountainmc.api.entity.Entity entity) {
        return this.canSee(((WetEntity) entity).getHandle());
    }

    public boolean canSee(Entity entity) {
        return hiddenEntities.contains(checkNotNull(entity, "Null entity"));
    }

    @Override
    public ImmutableSet<WetEntity> getHiddenEntities() {
        return ImmutableSets.transform(hiddenEntities, Entity::getFountainEntity);
    }

    @Override
    public boolean isConnected() {
        return getHandle().connection != null;
    }

    @Override
    public EntityType<Player> getEntityType() {
        return EntityType.PLAYER;
    }
    
}
