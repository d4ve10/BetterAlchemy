package de.d4ve10.betteralchemy.potion.potions;

import de.d4ve10.betteralchemy.potion.ListenerPotion;
import de.d4ve10.betteralchemy.potion.PotionData;
import de.d4ve10.betteralchemy.utils.TeleportHandler;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;


public class PotionOfWarping extends ListenerPotion {

    public PotionOfWarping() {
        super(new PotionData("Potion Of Warping", 60, 0), Color.fromRGB(11, 76, 65)); // Color.fromRGB(20,176,14)
        this.lore.add(ChatColor.BLUE + "Warping{{DURATION}}");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity))
            return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (!this.hasEffect(entity))
            return;
        this.teleport(entity);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getHitEntity() instanceof LivingEntity))
            return;
        LivingEntity entity = (LivingEntity) event.getHitEntity();
        if (!this.hasEffect(entity))
            return;
        event.setCancelled(true);
        this.teleport(entity);
    }

    private void teleport(@NotNull LivingEntity entity) {
        TeleportHandler teleportHandler = new TeleportHandler(entity, 20, 20);
        teleportHandler.teleport();
    }

}
