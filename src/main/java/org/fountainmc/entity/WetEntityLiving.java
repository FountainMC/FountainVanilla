package org.fountainmc.entity;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;

import org.fountainmc.api.entity.EntityType;
import org.fountainmc.api.entity.LivingEntity;

import static com.google.common.base.Preconditions.*;

public class WetEntityLiving extends WetEntity implements LivingEntity {

    @Override
    public EntityLivingBase getHandle() {
        return (EntityLivingBase) super.getHandle();
    }

    public WetEntityLiving(EntityLivingBase handle) {
        super(handle);
    }

    @Override
    public double getHealth() {
        return getHandle().getHealth();
    }

    @Override
    public void setHealth(double health) {
        checkArgument(health > 0, "health %s must be greater than 0", health);
        checkArgument(Double.isFinite(health), "Health %s must be finite", health);
        getHandle().setHealth((float) health);
    }

    @Override
    public double getMaxHealth() {
        return getHandle().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
    }

    @Override
    public void setMaxHealth(double amount) {
        checkArgument(amount > 0, "Amount %s must be greater than 0", amount);
        checkArgument(Double.isFinite(amount), "Amount %s must be finite", amount);
        getHandle().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(amount);
        if (amount > getHealth()) setHealth(amount);
    }

    @Override
    public boolean isAlive() {
        return !getHandle().isDead;
    }

    @Override
    public void damage(double amount) {
        checkArgument(amount > 0, "Amount %s must be greater than 0", amount);
        checkArgument(Double.isFinite(amount), "Amount %s must be finite", amount);
        getHandle().attackEntityFrom(DamageSource.generic, (float) amount);
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public EntityType<? extends LivingEntity> getEntityType() {
        return (EntityType<LivingEntity>) super.getEntityType();
    }
}
