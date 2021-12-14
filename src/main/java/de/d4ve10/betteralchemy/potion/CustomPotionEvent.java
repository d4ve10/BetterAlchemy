package de.d4ve10.betteralchemy.potion;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomPotionEvent extends Event {

    public enum Action {
        POTION_APPLY,
        POTION_REMOVE
    }

    private static final HandlerList handlers = new HandlerList();
    private final LivingEntity entity;
    private final CustomPotion potion;
    private final Action action;


    public CustomPotionEvent(@NotNull LivingEntity entity, @NotNull CustomPotion potion, @NotNull Action action) {
        this.entity = entity;
        this.potion = potion;
        this.action = action;
    }

    @NotNull
    public LivingEntity getEntity() {
        return entity;
    }

    @NotNull
    public CustomPotion getPotion() {
        return potion;
    }

    @NotNull
    public Action getAction() {
        return action;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
