package de.d4ve10.betteralchemy.potion.potions;

import de.d4ve10.betteralchemy.potion.ListenerPotion;
import de.d4ve10.betteralchemy.potion.PotionData;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class PotionOfPoisonResistance extends ListenerPotion {

    public PotionOfPoisonResistance() {
        super(new PotionData("Potion Of Poison Resistance", 180, 0), Color.GREEN);
        this.lore.add(ChatColor.BLUE + "Poison Resistance{{DURATION}}");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity))
            return;
        LivingEntity entity = (LivingEntity) event.getEntity();

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.POISON))
            return;
        if (!this.hasEffect(entity))
            return;
        event.setCancelled(true);
    }
}
