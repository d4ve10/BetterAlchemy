package de.d4ve10.betteralchemy.potion.potions;

import de.d4ve10.betteralchemy.potion.CustomPotion;
import de.d4ve10.betteralchemy.potion.PotionData;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;


public class PotionOfGlowing extends CustomPotion {

    public PotionOfGlowing() {
        super(new PotionData("Potion Of Glowing", 180, 0), Color.YELLOW);
        this.potionEffectTypes.add(PotionEffectType.GLOWING);
        this.lore.add(ChatColor.BLUE + "Glowing{{DURATION}}");
    }

    @Override
    public void applyEffect(@NotNull LivingEntity entity) {
    }
}
