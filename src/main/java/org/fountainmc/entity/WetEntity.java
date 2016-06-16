package org.fountainmc.entity;

import java.util.Collection;

import net.minecraft.util.DamageSource;
import org.fountainmc.NMSConverters;
import org.fountainmc.api.entity.Entity;
import org.fountainmc.api.entity.EntityType;
import org.fountainmc.api.world.Location;

import static com.google.common.base.Preconditions.checkNotNull;

public class WetEntity implements Entity {

    protected final net.minecraft.entity.Entity handle;

    public WetEntity(net.minecraft.entity.Entity handle) {
        this.handle = checkNotNull(handle, "Null entity");
    }

    @Override
    public Location getLocation() {
        return NMSConverters.toFountainLocation(handle.getEntityWorld(), handle.getPosition());
    }

    @Override
    public boolean isOnGround() {
        return handle.onGround;
    }

    @Override
    public void setLocation(Location loc) {
    }

    @Override
    public float getPitch() {
        return handle.rotationPitch;
    }

    @Override
    public void setPitch(float pitch) {
        handle.rotationPitch = pitch % 360;
    }

    @Override
    public float getYaw() {
        return handle.rotationYaw;
    }

    @Override
    public void setYaw(float yaw) {
        handle.rotationYaw = yaw % 360;
    }

    @Override
    public Entity getPassenger() {
        return null;
    }

    @Override
    public void setPassenger(Entity passenger) {
    }

    @Override
    public Entity getVehicle() {
        return null;
    }

    @Override
    public void setVehicle(Entity vehicle) {
    }

    @Override
    public Entity getBottomVehicle() {
        return null;
    }

    @Override
    public Collection<Entity> getNearbyEntities(double distance) {
        return null;
    }

    @Override
    public void damage(int amount) {
        handle.attackEntityFrom(DamageSource.generic, amount);
    }

    @Override
    public EntityType<?> getEntityType() {
        return null;
    }

}
