package org.fountainmc.entity;

import net.minecraft.entity.Entity;
import org.fountainmc.api.entity.EntityLiving;

public class WetEntityLiving extends WetEntity implements EntityLiving {

    protected final net.minecraft.entity.EntityLiving livingHandle = (net.minecraft.entity.EntityLiving) this.handle;

    public WetEntityLiving(Entity handle) {
        super(handle);
    }

    @Override public int getHealth() {
        return Math.round(livingHandle.getHealth());
    }

    @Override public void setHealth(int i) {
        livingHandle.setHealth((float) i);
    }
}
