package de.d4ve10.betteralchemy.potion;

import de.d4ve10.betteralchemy.BetterAlchemy;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public abstract class ListenerPotion extends CustomPotion implements Listener {

    private static final Map<String, ListenerPotion> mainListeners = new HashMap<>();
    private static final Map<String, Boolean> registered = new HashMap<>();

    public ListenerPotion(@NotNull PotionData potionData, @NotNull Color color) {
        super(potionData, color);
        if (!mainListeners.containsKey(this.getClass().getSimpleName()))
            mainListeners.put(this.getClass().getSimpleName(), this);
    }

    @Override
    public void applyEffect(@NotNull LivingEntity entity) {
        register();
        super.applyEffect(entity, null);
    }

    public void register() {
        String className = this.getClass().getSimpleName();
        if (!mainListeners.containsKey(className) || !registered.containsKey(className) || !registered.get(className)) {
            ListenerPotion mainListener = mainListeners.getOrDefault(className, this);
            Bukkit.getServer().getPluginManager().registerEvents(mainListener, BetterAlchemy.getInstance());
            Bukkit.getServer().getLogger().log(Level.INFO, "Registered " + className + " as Listener");
            mainListeners.put(className, mainListener);
            registered.put(className, true);
        }
    }

}
