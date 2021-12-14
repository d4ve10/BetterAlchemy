package de.d4ve10.betteralchemy.listeners;

import de.d4ve10.betteralchemy.BetterAlchemy;
import de.d4ve10.betteralchemy.potion.CustomPotion;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BrewListener implements Listener {

    @EventHandler
    public void onBrew(BrewEvent event) {
        BrewerInventory inv = event.getContents();
        ItemStack ingredient = inv.getIngredient();
        List<ItemStack> result = event.getResults();
        if (ingredient == null)
            return;
        int index = -1;
        for (ItemStack item : inv.getContents()) {
            index++;
            if (item == null || (item.getType() != Material.POTION && item.getType() != Material.SPLASH_POTION && item.getType() != Material.LINGERING_POTION))
                continue;
            CustomPotion potion = BetterAlchemy.getInstance().getPotionMaker().getPotion(item.getItemMeta());
            if (potion == null)
                continue;
            if (ingredient.getType() == Material.GUNPOWDER && item.getType() == Material.POTION) {
                potion.setPotionVariant(CustomPotion.PotionVariant.SPLASH);
                potion.getPotionData().setName("Splash " + potion.getPotionData().getName());
            } else if (ingredient.getType() == Material.DRAGON_BREATH && item.getType() == Material.SPLASH_POTION) {
                potion.setPotionVariant(CustomPotion.PotionVariant.LINGERING);
                potion.getPotionData().setName("Lingering " + potion.getPotionData().getName());
            }
            //TODO: add redstone and glowstone effect

            result.set(index, potion.create());
        }
    }

}
