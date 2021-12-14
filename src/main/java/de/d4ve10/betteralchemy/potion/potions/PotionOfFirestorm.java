package de.d4ve10.betteralchemy.potion.potions;

import de.d4ve10.betteralchemy.potion.CustomPotion;
import de.d4ve10.betteralchemy.potion.PotionData;
import de.d4ve10.betteralchemy.utils.PotionUtils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;


public class PotionOfFirestorm extends CustomPotion {

    public PotionOfFirestorm() {
        super(new PotionData("Potion Of Firestorm", 15, 0), Color.ORANGE);
        this.lore.add(ChatColor.BLUE + "Firestorm{{DURATION}}");
        this.tickRate = 5;
    }

    @Override
    public void applyEffect(@NotNull LivingEntity entity) {
        Callable<Boolean> callable = () -> {
            for (Location pos : PotionUtils.getCircle(entity.getLocation(), 3, 16, true)) {
                int highestBlockY = entity.getWorld().getHighestBlockYAt(pos, HeightMap.MOTION_BLOCKING) + 1;
                if (Math.abs(entity.getLocation().getY() - highestBlockY) > 4)
                    continue;
                Location location = new Location(entity.getWorld(), pos.getX(), highestBlockY, pos.getZ());
                if (location.getBlock().getType() == Material.AIR)
                    location.getBlock().setType(Material.FIRE);
            }
            return true;
        };

        super.applyEffect(entity, callable);
    }
}
