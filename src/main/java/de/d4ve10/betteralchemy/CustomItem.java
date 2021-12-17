package de.d4ve10.betteralchemy;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface CustomItem {

    @Nullable
    ItemStack create();
}