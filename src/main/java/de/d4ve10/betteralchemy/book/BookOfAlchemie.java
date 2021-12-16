package de.d4ve10.betteralchemy.book;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BookOfAlchemie {

    @Nullable
    private final String author;

    @Nullable
    private final String title;

    @NotNull
    private final List<String> lore;

    @NotNull
    private final List<String> pages;

    public BookOfAlchemie() {
        this.author = "";
        this.title = "";
        this.lore = new ArrayList<>();
        this.pages = new ArrayList<>();
    }

    @Nullable
    public ItemStack create() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        if (meta == null)
            return null;
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Book of Alchemie");
        meta.setLocalizedName("Book Of Alchemie");
        meta.setAuthor(this.author);
        meta.setTitle(this.title);
        meta.setGeneration(BookMeta.Generation.ORIGINAL);
        meta.setLore(this.lore);
        meta.setPages(this.pages);
        book.setItemMeta(meta);
        return book;
    }
}
