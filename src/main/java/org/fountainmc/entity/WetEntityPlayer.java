package org.fountainmc.entity;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import org.fountainmc.api.entity.Player;

public class WetEntityPlayer extends WetEntityLiving implements Player {

    protected final EntityPlayerMP entityPlayerMP = (EntityPlayerMP) handle;

    public WetEntityPlayer(Entity handle) {
        super(handle);
    }

    @Nonnull @Override public String getName() {
        return entityPlayerMP.getName();
    }

    @Nonnull @Override public UUID getUUID() {
        return entityPlayerMP.getUniqueID();
    }

    @Override public void sendMessage(String s) {
        entityPlayerMP.addChatMessage(new TextComponentString(s));
    }
}
