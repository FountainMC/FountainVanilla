package org.fountainmc.entity;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import org.fountainmc.api.entity.EntityType;
import org.fountainmc.api.entity.Player;

public class WetEntityPlayer extends WetLivingEntity implements Player {

    ArrayList<org.fountainmc.api.entity.Entity> hiddenEntities = new ArrayList<org.fountainmc.api.entity.Entity>();

    protected final EntityPlayerMP entityPlayerMP = (EntityPlayerMP) handle;

    public WetEntityPlayer(Entity handle) {
        super(handle);
    }

    @Nonnull
    @Override
    public String getName() {
        return entityPlayerMP.getName();
    }

    @Nonnull
    @Override
    public UUID getUUID() {
        return entityPlayerMP.getUniqueID();
    }

    @Override
    public void sendMessage(String s) {
        entityPlayerMP.addChatMessage(new TextComponentString(s));
    }

    @Override
    public void hide(org.fountainmc.api.entity.Entity entity) {
        hiddenEntities.add(entity);
    }

    @Override
    public ImmutableList<? extends org.fountainmc.api.entity.Entity> getHiddenEntities() {
        return (ImmutableList<? extends org.fountainmc.api.entity.Entity>) ImmutableList.of(hiddenEntities);
    }

    @Override
    public EntityType<Player> getEntityType() {
        return EntityType.PLAYER;
    }
    
}
