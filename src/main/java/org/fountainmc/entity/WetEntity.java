package org.fountainmc.entity;

import lombok.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.techcable.pineapple.collect.ImmutableLists;

import org.fountainmc.AsyncCatcher;
import org.fountainmc.api.entity.EntityType;
import org.fountainmc.api.world.Location;
import org.fountainmc.world.WetWorld;

import static com.google.common.base.Preconditions.*;

@ParametersAreNonnullByDefault
public class WetEntity implements org.fountainmc.api.entity.Entity {

    public static WetEntity createEntity(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            if (entity instanceof EntityPlayerMP) {
                return new WetEntityPlayer((EntityPlayerMP) entity);
            } else {
                return new WetEntityLiving((EntityLivingBase) entity);
            }
        } else {
            return new WetEntity(entity);
        }
    }

    @Getter
    private final Entity handle;

    public WetEntity(net.minecraft.entity.Entity handle) {
        this.handle = checkNotNull(handle, "Null entity");
    }

    @Override
    @Nonnull
    public Location getLocation() {
        return new Location(this.getWorld(), getHandle().locX, getHandle().locY, getHandle().locZ);
    }

    @Nonnull
    public WetWorld getWorld() {
        return getHandle().world.getFountainWorld();
    }

    @Override
    public boolean isOnGround() {
        return getHandle().onGround;
    }

    @Override
    public void teleport(Location location) {
        checkNotNull(location, "Null location");
        teleport(((WetWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
    }

    public void teleport(World world, double x, double y, double z) {
        this.teleport(world, x, y, z, this.getYaw(), this.getPitch());
    }

    public void teleport(World world, double x, double y, double z, float yaw, float pitch) {
        AsyncCatcher.checkAsyncOp("teleport");
        checkNotNull(world, "Null world");
        checkArgument(Double.isFinite(x), "X position '%s' isn't finite", x);
        checkArgument(Double.isFinite(y), "Y position '%s' isn't finite", y);
        checkArgument(Double.isFinite(z), "Z position '%s' isn't finite", z);
        checkArgument(Float.isFinite(yaw), "Yaw position '%s' isn't finite", yaw);
        checkArgument(Float.isFinite(pitch), "Pitch position '%s' isn't finite", pitch);
        if (getHandle().dead) return;
        if (getHandle().isVehicle()) this.ejectAll();
        this.dismountVehicle();

        getHandle().world = world;
        getHandle().setPositionRotation(x, y, z, yaw, pitch);
    }

    public void teleportRotation(float yaw, float pitch) {
        AsyncCatcher.checkAsyncOp("set rotation");
        this.teleport(
                getHandle().world,
                getHandle().locX,
                getHandle().locY,
                getHandle().locZ,
                yaw,
                pitch
        );
    }

    @Override
    public float getPitch() {
        return getHandle().pitch;
    }

    @Override
    public void setPitch(float pitch) {
        this.teleportRotation(getYaw(), pitch);
    }

    @Override
    public float getYaw() {
        return getHandle().yaw;
    }

    @Override
    public void setYaw(float yaw) {
        this.teleportRotation(yaw, getPitch());
    }

    @Nullable
    @Override
    public WetEntity getPrimaryPassenger() {
        Entity ridingEntity = getHandle().getP();
        return ridingEntity == null ? null : ridingEntity.getFountainEntity();
    }

    @Nonnull
    @Override
    public ImmutableList<org.fountainmc.api.entity.Entity> getPassengers() {
        return ImmutableLists.transform(getHandle().bx(), Entity::getFountainEntity);
    }

    @Override
    public boolean hasPassengers() {
        return getHandle().isVehicle();
    }

    @Override
    public void setPrimaryPassenger(org.fountainmc.api.entity.Entity passenger) {
        checkArgument(checkNotNull(passenger, "Null entity").getWorld().equals(this.getWorld()), "Passenger's world %s doesn't equal entity's world %s", passenger.getWorld(), this.getWorld());
        AsyncCatcher.checkAsyncOp("set primary passengers");
        this.ejectPrimaryPassenger();
        getHandle().startRiding(((WetEntity) passenger).getHandle(), true);
    }

    @Override
    public boolean addPassenger(org.fountainmc.api.entity.Entity passenger, boolean force) {
        checkArgument(checkNotNull(passenger, "Null entity").getWorld().equals(this.getWorld()), "Passenger's world %s doesn't equal entity's world %s", passenger.getWorld(), this.getWorld());
        AsyncCatcher.checkAsyncOp("add a passenger");
        Entity passengerHandle = ((WetEntity) passenger).getHandle();
        checkState(force || getHandle().canFitPassenger(passengerHandle), "Entity %s can't fit passenger %s, because it can only have %s passengers", this, passenger, getMaximumPassengers());
        return getHandle().startRiding(passengerHandle, force);
    }

    @Override
    public void dismountVehicle() {
        getHandle().stopRiding();
    }

    @Override
    public void ejectAll() {
        AsyncCatcher.checkAsyncOp("eject all passengers");
        getHandle().az();
    }

    @Override
    public void ejectPassenger(org.fountainmc.api.entity.Entity entity) {
        checkState(this.equals(checkNotNull(entity, "Null passenger").getVehicle()), "Entity %s isn't a passenger of %s", entity, this);
        AsyncCatcher.checkAsyncOp("eject a passenger");
        entity.dismountVehicle();
    }

    @Override
    public int getMaximumPassengers() {
        return getHandle().getMaxPassengers();
    }

    @Nullable
    @Override
    public WetEntity getVehicle() {
        Entity ridingEntity = getHandle().getVehicle();
        return ridingEntity == null ? null : ridingEntity.getFountainEntity();
    }

    @Nullable
    @Override
    public WetEntity getBottomVehicle() {
        Entity lowestRidingEntity = getHandle().getLowestRidingEntity();
        return lowestRidingEntity == null ? null : lowestRidingEntity.getFountainEntity();
    }

    @Override
    public void leaveVehicle() {
        getHandle().az();
    }

    @Override
    public ImmutableCollection<org.fountainmc.api.entity.Entity> getNearbyEntities(double v) {
        AsyncCatcher.checkAsyncOp("get nearby entities");
        return ImmutableLists.transform(
                getWorld().getHandle().getEntitiesInAABBexcluding(getHandle(), getHandle().getBoundingBox().grow(v), null),
                Entity::getFountainEntity
        );
    }

    @Nonnull
    @Override
    public EntityType<?> getEntityType() {
        return getWorld().getServer().getEntityType(EntityList.getName(getHandle().getClass()));
    }

    @Override
    public int hashCode() {
        return getHandle().hashCode();
    }

    @Override
    public String toString() {
        return getHandle().getName() + " in " + getWorld() + " at " + getHandle().locX + " " + getHandle().locY + " " + getHandle().locZ;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj == this || obj != null && obj instanceof WetEntity && ((WetEntity) obj).getHandle().equals(this.getHandle());
    }

}
