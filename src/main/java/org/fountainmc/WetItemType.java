package org.fountainmc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Verify.verifyNotNull;

public class WetItemType implements org.fountainmc.api.Material {

    private final WetServer server;
    private final Item item;

    public WetItemType(WetServer server, Item material) {
        this.server = checkNotNull(server, "Null server");
        this.item = checkNotNull(material, "Null material");
    }

    @Override
    public int getId() {
        return Item.getIdFromItem(item);
    }

    @Override
    public String getName() {
        return verifyNotNull(Item.REGISTRY.getNameForObject(item), "Null item name").toString();
    }

    @Override
    public boolean isBlock() {
        return false;
    }

    @Override
    public boolean isEdible() {
        return item instanceof ItemFood;
    }

    @Override
    public boolean isVanilla() {
        return true;
    }

}
