package de.d4ve10.betteralchemy.potion.potions;

import de.d4ve10.betteralchemy.potion.ListenerPotion;
import de.d4ve10.betteralchemy.potion.PotionData;
import de.d4ve10.betteralchemy.utils.PotionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class PotionOfFriendship extends ListenerPotion {

    private static final List<Mob> mobs = new ArrayList<>();

    public PotionOfFriendship() {
        super(new PotionData("Potion Of Friendship", 300, 0), Color.LIME);
        this.lore.add(ChatColor.BLUE + "Friendship{{DURATION}}");
    }

    @Override
    public void applyEffect(@NotNull LivingEntity entity) {
        for (Mob mob : mobs) {
            if (mob.getTarget() == entity)
                mob.setTarget(null);
        }
        super.applyEffect(entity, null);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Mob))
            return;
        if (!(event.getTarget() instanceof LivingEntity))
            return;
        Mob mob = (Mob) event.getEntity();
        if (event.getTarget() == null) {
            mobs.remove(mob);
            return;
        }
        if (!PotionUtils.isHostile(mob))
            return;
        LivingEntity target = (LivingEntity) event.getTarget();
        if (this.hasEffect(target))
            event.setCancelled(true);
        if (!mobs.contains(mob))
            mobs.add(mob);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity))
            return;
        LivingEntity player = (LivingEntity) event.getEntity();
        if (this.hasEffect(player))
            event.setCancelled(true);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getHitEntity() instanceof LivingEntity))
            return;
        LivingEntity target = (LivingEntity) event.getHitEntity();
        if (!this.hasEffect(target))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Mob))
            return;
        Mob mob = (Mob) event.getEntity();
        mobs.remove(mob);
    }
}
