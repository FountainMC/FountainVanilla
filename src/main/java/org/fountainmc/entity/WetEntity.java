package org.fountainmc.entity;

import org.fountainmc.NMSConverters;
import org.fountainmc.api.entity.Entity;
import org.fountainmc.api.world.Location;

import static com.google.common.base.Preconditions.checkNotNull;

public class WetEntity implements Entity {

    protected final net.minecraft.entity.Entity handle;

    public WetEntity(net.minecraft.entity.Entity handle) {
        this.handle = checkNotNull(handle, "Null entity");
    }

    @Override public Location getLocation() {
        return NMSConverters.toFountainLocation(handle.getEntityWorld(), handle.getPosition());
    }

    @Override public boolean isOnGround() {
        return handle.onGround;
    }
}
