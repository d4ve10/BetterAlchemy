package de.d4ve10.betteralchemy.book;

import de.d4ve10.betteralchemy.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BookOfAlchemie implements CustomItem {

    private final String author;

    private final String title;

    private final List<String> lore;

    private final List<String> pages;

    public BookOfAlchemie() {
        this.author = "Dave10";
        this.title = "Book of Alchemy";
        this.lore = new ArrayList<>();
        this.pages = new ArrayList<>();
        this.initializeBook();
    }

    @Nullable
    public ItemStack create() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        if (meta == null)
            return null;
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Book of Alchemy");
        meta.setLocalizedName("Book Of Alchemy");
        meta.setAuthor(this.author);
        meta.setTitle(this.title);
        meta.setGeneration(BookMeta.Generation.ORIGINAL);
        meta.setLore(this.lore);
        meta.setPages(this.pages);
        book.setItemMeta(meta);
        return book;
    }


    private void initializeBook() {
        this.pages.add(
                getTitle("Creating a new potion") +
                    getList("Place a water cauldron above a heat source",
                            "Throw the needed ingredients into the cauldron",
                            "Right-click the cauldron with a stick to create the potion",
                            "Extract the potions with empty bottles")
        );
        this.pages.add(
                getTitle("Potion of Corruption") +
                        getIngredients("Wither Rose", "Pufferfish", "Red Mushroom")
        );
        this.pages.add(
                getTitle("Potion of Corruption") +
                        getEffect("Inverts every effect the player has. For example, if a player has the swiftness effect, he now gets the slowness effect")
        );

        this.pages.add(
                getTitle("Potion of Drowning") +
                        getIngredients("Lily Pad", "Kelp", "Turtle Egg", "Big Dripleaf")
        );
        this.pages.add(
                getTitle("Potion of Drowning") +
                        getEffect("The player gets the drowning effect and needs Water Breathing to not get drowned") +
                        getCorruptedEffect("The player gets Water Breathing")
        );

        this.pages.add(
                getTitle("Potion of Glowing") +
                        getIngredients("Glow Ink Sac", "Glow Berries", "Sunflower", "Glow Lichen")
        );
        this.pages.add(
                getTitle("Potion of Glowing") +
                        getEffect("Gives the player the glowing effect") +
                        getCorruptedEffect("Gives the player the Invisibility effect")
        );

        this.pages.add(
                getTitle("Potion of Spectator") +
                        getIngredients("Brain Coral", "Sea Pickle", "Spore Blossom")
        );
        this.pages.add(
                getTitle("Potion of Spectator") +
                        getEffect("Gives the player Spectator Mode for a brief moment") +
                        getCorruptedEffect("Gives the player Adventure Mode")
        );

        this.pages.add(
                getTitle("Potion of Warping") +
                        getIngredients("Ender Pearl", "Warped Fungus", "Nether Sprouts", "Warped Roots", "Eye of Ender", "Twisting Vines", "Chorus Fruit", "Popped Chorus Fruit")
        );
        this.pages.add(
                getTitle("Potion of Warping") +
                        getEffect("Teleports the player randomly in a range of ~20 blocks when he is getting hit by a projectile or takes damage") +
                        getCorruptedEffect("Gives the player slowness")
        );

        this.pages.add(
                getTitle("Potion of Firestorm") +
                        getIngredients("Crimson Roots", "Crimson Fungus", "Weeping Vines", "Fire Coral")
        );
        this.pages.add(
                getTitle("Potion of Firestorm") +
                        getEffect("Creates fire around the player") +
                        getCorruptedEffect("Creates water splash potions around the player which can extinguish fire")
        );

        this.pages.add(
                getTitle("Potion of Friendship") +
                        getIngredients("Honeycomb", "Honey Bottle", "Dandelion", "Blue Orchid", "Allium", "Red Tulip", "Oxeye Daisy", "Cornflower", "Sugar Cane", "Peony")
        );
        this.pages.add(
                getTitle("Potion of Friendship") +
                        ChatColor.DARK_GREEN + "Additional " + getIngredients("Sunflower", "Tube Coral", "Horn Coral", "Bubble Coral", "Lilac")
        );
        this.pages.add(
                getTitle("Potion of Friendship") +
                        getEffect("Mobs are friendly towards the player and won't attack him") +
                        getCorruptedEffect("All Mobs in a range of ~100 blocks are now targeting the player")
        );

        this.pages.add(
                getTitle("Potion of Poison Resistance") +
                        getIngredients("Slime Ball", "Blue Orchid", "Fermented Spider Eye", "Poisonous Potato", "Zombie Head")
        );
        this.pages.add(
                getTitle("Potion of Poison Resistance") +
                        getEffect("The player is immune against poison") +
                        getCorruptedEffect("Gives the player poison")
        );
    }

    @NotNull
    private String getTitle(@NotNull String title) {
        return ChatColor.DARK_GREEN + "" + ChatColor.BOLD + title + ":" + ChatColor.RESET + "\n\n";
    }


    @NotNull
    private String getIngredients(@NotNull String... ingredients) {
        return ChatColor.DARK_GREEN + "Ingredients:\n" + ChatColor.RESET + getList(ingredients);
    }

    @NotNull
    private String getList(@NotNull String ...items) {
        StringBuilder builder = new StringBuilder();
        for (String item : items)
            builder.append(ChatColor.BLUE).append("- ").append(item).append(ChatColor.RESET).append("\n");
        return builder.toString();
    }

    @NotNull
    private String getEffect(@NotNull String effect) {
        return getEffect(effect, "Effect");
    }

    @NotNull
    private String getCorruptedEffect(@NotNull String effect) {
        return getEffect(effect, "Corrupted Effect");
    }

    @NotNull
    private String getEffect(@NotNull String effect, @NotNull String type) {
        return ChatColor.DARK_GREEN + type + ":\n" + ChatColor.RESET +
                ChatColor.GRAY + effect + ChatColor.RESET + "\n\n";
    }

}
