package de.d4ve10.betteralchemy.potion.potions;

import de.d4ve10.betteralchemy.potion.CustomPotion;
import de.d4ve10.betteralchemy.potion.PotionData;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

public class PotionOfDrowning extends CustomPotion {

    public PotionOfDrowning() {
        super(new PotionData("Potion Of Drowning", 30, 0), Color.AQUA);
        this.deathMessage = "drowned";
        this.lore.add(ChatColor.BLUE + "Drowning{{DURATION}}");
    }

    @Override
    public void applyEffect(@NotNull LivingEntity entity) {
        Callable<Boolean> task = () -> {
            boolean isCreative = entity instanceof Player && (((Player) entity).getGameMode() == GameMode.CREATIVE || ((Player) entity).getGameMode() == GameMode.SPECTATOR);
            if (!entity.hasPotionEffect(PotionEffectType.WATER_BREATHING) && !isCreative) {
                double healthBefore = entity.getHealth();
                entity.damage(2);
                double healthAfter = entity.getHealth();
                if (healthAfter == healthBefore)
                    return false;
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    player.playSound(entity.getLocation(), Sound.ENTITY_PLAYER_HURT_DROWN, 1, 1);
                    this.updateLastDeathCause(player);
                }
            }
            return true;
        };
        super.applyEffect(entity, task);
    }
}
