package de.d4ve10.betteralchemy.potion.potions.corrupted;

import de.d4ve10.betteralchemy.potion.CustomPotion;
import de.d4ve10.betteralchemy.potion.PotionData;
import de.d4ve10.betteralchemy.utils.PotionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;


public class PotionOfHostility extends CustomPotion {

    public PotionOfHostility() {
        super(new PotionData("Potion Of Hostility", 60, 0), Color.BLACK);
        this.lore.add(ChatColor.BLUE + "Hostility{{DURATION}}");
    }

    @Override
    public void applyEffect(@NotNull LivingEntity entity) {
        Callable<Boolean> callable = () -> {
            setTargetToAllMonsters(entity, entity);
            return true;
        };
        super.applyEffect(entity, callable);
    }

    @Override
    public void removeEffect(@NotNull LivingEntity entity) {
        setTargetToAllMonsters(entity, null);
        super.removeEffect(entity);
    }


    private void setTargetToAllMonsters(@NotNull LivingEntity entity, @Nullable LivingEntity target) {
        entity.getWorld().getNearbyEntities(entity.getLocation(), 100, 20, 100).forEach(nearbyEntity -> {
            if (!(nearbyEntity instanceof Mob))
                return;
            if (entity == nearbyEntity)
                return;
            Mob mob = (Mob) nearbyEntity;
            if (!PotionUtils.isHostile(mob))
                return;
            mob.setTarget(target);
        });
    }

}
