package de.d4ve10.betteralchemy.listeners;

import de.d4ve10.betteralchemy.BetterAlchemy;
import de.d4ve10.betteralchemy.potion.CustomPotion;
import de.d4ve10.betteralchemy.utils.PotionUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class EntityListener implements Listener {

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item.getType() != Material.POTION || item.getItemMeta() == null)
            return;
        CustomPotion potion = BetterAlchemy.getInstance().getPotionMaker().getPotion(item.getItemMeta());
        if (potion == null)
            return;
        potion.applyEffect(player);
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();
        if (potion.getItem().getItemMeta() == null)
            return;
        CustomPotion customPotion = BetterAlchemy.getInstance().getPotionMaker().getPotion(potion.getItem().getItemMeta());
        if (customPotion == null)
            return;
        event.getAffectedEntities().forEach(customPotion::applyEffect);
    }

    @EventHandler
    public void onLingeringPotionSplash(LingeringPotionSplashEvent event) {
        ThrownPotion potion = event.getEntity();
        CustomPotion customPotion = BetterAlchemy.getInstance().getPotionMaker().getPotion(potion.getItem().getItemMeta());
        if (customPotion == null)
            return;
        event.getAreaEffectCloud().getPersistentDataContainer().set(new NamespacedKey(BetterAlchemy.getInstance(), "CustomPotion"), PersistentDataType.STRING, customPotion.getPotionData().getName());
    }

    @EventHandler
    public void onPlayerInLingering(AreaEffectCloudApplyEvent event) {
        if (event.getEntity().getPersistentDataContainer().has(new NamespacedKey(BetterAlchemy.getInstance(), "CustomPotion"), PersistentDataType.STRING)) {
            String potionName = event.getEntity().getPersistentDataContainer().get(new NamespacedKey(BetterAlchemy.getInstance(), "CustomPotion"), PersistentDataType.STRING);
            CustomPotion customPotion = BetterAlchemy.getInstance().getPotionMaker().getPotion(potionName);
            if (customPotion == null)
                return;
            customPotion.setPotionVariant(CustomPotion.PotionVariant.LINGERING);
            event.getAffectedEntities().forEach(customPotion::applyEffect);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        PotionUtils.removeAllEffects(entity);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getLastDamageCause() != null && player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.CUSTOM)
            event.setDeathMessage(player.getName() + " " + CustomPotion.getLastDeathCause(player));
        CustomPotion.removeLastDeathCause(player);
    }

    @EventHandler
    public void onEntityLoad(EntitiesLoadEvent event) {
        for (Entity entity : event.getEntities()) {
            if (!(entity instanceof LivingEntity))
                continue;
            LivingEntity livingEntity = (LivingEntity) entity;
            PotionUtils.applyAllEffects(livingEntity);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PotionUtils.applyAllEffects(player);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        CustomPotion.unloadAll(player);
    }

}
