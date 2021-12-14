package de.d4ve10.betteralchemy;

import de.d4ve10.betteralchemy.book.BookOfAlchemie;
import de.d4ve10.betteralchemy.listeners.BrewListener;
import de.d4ve10.betteralchemy.listeners.CauldronListener;
import de.d4ve10.betteralchemy.listeners.EntityListener;
import de.d4ve10.betteralchemy.potion.CustomPotion;
import de.d4ve10.betteralchemy.potion.PotionDataPersistentDataType;
import de.d4ve10.betteralchemy.potion.PotionMaker;
import de.d4ve10.betteralchemy.potion.potions.PotionOfFriendship;
import de.d4ve10.betteralchemy.utils.PotionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class BetterAlchemy extends JavaPlugin {
    private static BetterAlchemy instance;

    private PotionMaker potionMaker;
    private PotionDataPersistentDataType potionDataPersistentDataType;

    @NotNull
    public static BetterAlchemy getInstance() {
        return instance;
    }

    @NotNull
    public PotionMaker getPotionMaker() {
        return potionMaker;
    }

    @NotNull
    public PotionDataPersistentDataType getPotionDataPersistentDataType() {
        return potionDataPersistentDataType;
    }


    @Override
    public void onEnable() {
        instance = this;
        potionMaker = new PotionMaker();
        potionDataPersistentDataType = new PotionDataPersistentDataType();
        getServer().getPluginManager().registerEvents(new BrewListener(), this);
        getServer().getPluginManager().registerEvents(new CauldronListener(), this);
        getServer().getPluginManager().registerEvents(new EntityListener(), this);

        (new PotionOfFriendship()).register();
        createBookRecipe();
        Bukkit.getOnlinePlayers().forEach(PotionUtils::applyAllEffects);
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(CustomPotion::unloadAll);
    }

    private void createBookRecipe() {
        BookOfAlchemie book = new BookOfAlchemie();
        ItemStack bookItem = book.create();
        if (bookItem == null)
            return;
        ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(this, "BookOfAlchemie"), bookItem);
        recipe.addIngredient(Material.WRITABLE_BOOK);
        recipe.addIngredient(Material.AMETHYST_SHARD);
        recipe.addIngredient(Material.BONE);
        Bukkit.addRecipe(recipe);
    }

}
