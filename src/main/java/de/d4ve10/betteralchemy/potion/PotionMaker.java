package de.d4ve10.betteralchemy.potion;

import de.d4ve10.betteralchemy.potion.potions.*;
import de.d4ve10.betteralchemy.potion.potions.corrupted.*;
import de.d4ve10.betteralchemy.utils.PotionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Material.*;

public class PotionMaker {

    private final Map<Material[], Class<? extends CustomPotion>> potions = new HashMap<>();
    private final Map<String, Class<? extends CustomPotion>> potionNames = new HashMap<>();

    public PotionMaker() {
        potions.put(new Material[]{LILY_PAD, KELP, TURTLE_EGG, BIG_DRIPLEAF}, PotionOfDrowning.class);
        potions.put(new Material[]{GLOW_INK_SAC, GLOW_BERRIES, SUNFLOWER, GLOW_LICHEN}, PotionOfGlowing.class);
        potions.put(new Material[]{BRAIN_CORAL, SEA_PICKLE, SPORE_BLOSSOM}, PotionOfSpectator.class);
        potions.put(new Material[]{ENDER_PEARL, WARPED_FUNGUS, NETHER_SPROUTS, WARPED_ROOTS, ENDER_EYE, TWISTING_VINES, CHORUS_FRUIT, POPPED_CHORUS_FRUIT}, PotionOfWarping.class);
        potions.put(new Material[]{CRIMSON_ROOTS, CRIMSON_FUNGUS, WEEPING_VINES, FIRE_CORAL}, PotionOfFirestorm.class);
        potions.put(new Material[]{HONEYCOMB, HONEY_BOTTLE, DANDELION, BLUE_ORCHID, ALLIUM, RED_TULIP, OXEYE_DAISY, CORNFLOWER, SUGAR_CANE, PEONY, SUNFLOWER, TUBE_CORAL, HORN_CORAL, BUBBLE_CORAL, LILAC}, PotionOfFriendship.class);
        potions.put(new Material[]{WITHER_ROSE, PUFFERFISH, RED_MUSHROOM}, PotionOfCorruption.class);
        potions.put(new Material[]{SLIME_BALL, BLUE_ORCHID, FERMENTED_SPIDER_EYE, POISONOUS_POTATO, ZOMBIE_HEAD}, PotionOfPoisonResistance.class);
        potions.put(new Material[]{}, PotionOfHostility.class);
        potions.put(new Material[]{}, PotionOfAdventure.class);
        potions.put(new Material[]{}, PotionOfWaterstorm.class);
        createPotionNames();
    }


    @Nullable
    public CustomPotion getPotion(@Nullable String name) {
        if (name == null)
            return null;
        String searchName = PotionUtils.stripPotionName(name);
        Class<? extends CustomPotion> classPotion = potionNames.get(searchName);
        if (classPotion == null)
            return null;
        try {
            return classPotion.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public CustomPotion getPotion(@Nullable ItemMeta meta) {
        if (meta == null)
            return null;
        if (!meta.hasLocalizedName())
            return null;
        return getPotion(meta.getLocalizedName());
    }

    @Nullable
    public CustomPotion createPotion(@NotNull ArrayList<ItemStack> items) {
        for (Map.Entry<Material[], Class<? extends CustomPotion>> entry : potions.entrySet()) {
            if (
                    entry.getKey().length == 0 ||
                    !Arrays.stream(entry.getKey()).allMatch(material -> items.stream().anyMatch(item -> material.equals(item.getType()))))
                continue;
            try {
                CustomPotion customPotion = entry.getValue().getDeclaredConstructor().newInstance();
                Bukkit.broadcastMessage(customPotion + " created!");
                for (ItemStack item : items) {
                    if (Arrays.stream(entry.getKey()).anyMatch(material -> material.equals(item.getType())))
                        item.setAmount(item.getAmount() - 1);
                }
                return customPotion;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void createPotionNames() {
        potions.forEach((key, value) -> potionNames.put(PotionUtils.stripPotionName(value.getSimpleName()), value));
    }
}
