package de.d4ve10.betteralchemy.potion.potions.corrupted;

import de.d4ve10.betteralchemy.potion.CustomPotion;
import de.d4ve10.betteralchemy.potion.PotionData;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class PotionOfAdventure extends CustomPotion {

    private static final Map<Player, GameMode> gameModes = new HashMap<>();

    public PotionOfAdventure() {
        super(new PotionData("Potion Of Adventure", 60, 0), Color.BLACK);
        this.lore.add(ChatColor.BLUE + "Adventure{{DURATION}}");
    }

    @Override
    public void applyEffect(@NotNull LivingEntity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.getGameMode() != GameMode.ADVENTURE)
                gameModes.put(player, player.getGameMode());
            player.setGameMode(GameMode.ADVENTURE);
            super.applyEffect(entity, null);
        }
    }

    @Override
    public void removeEffect(@NotNull LivingEntity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.setGameMode(gameModes.getOrDefault(player, GameMode.SURVIVAL));
            super.removeEffect(entity);
        }
    }

}
