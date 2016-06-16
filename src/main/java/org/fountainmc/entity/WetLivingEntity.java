package org.fountainmc.entity;

import net.minecraft.entity.Entity;
import org.fountainmc.api.entity.EntityType;
import org.fountainmc.api.entity.LivingEntity;

public class WetLivingEntity extends WetEntity implements LivingEntity {

    protected final net.minecraft.entity.EntityLiving livingHandle = (net.minecraft.entity.EntityLiving) this.handle;

    public WetLivingEntity(Entity handle) {
        super(handle);
    }

    @Override
    public double getHealth() {
        return livingHandle.getHealth();
    }

    @Override
    public void setHealth(double i) {
        livingHandle.setHealth((float) i);
    }

    @Override
    public double getMaxHealth() {
        return livingHandle.getMaxHealth();
    }

    @Override
    public void setMaxHealth(int maxHealth) {
    }

    @Override
    public EntityType<? extends LivingEntity> getEntityType() {
        return null;
    }
    
}
