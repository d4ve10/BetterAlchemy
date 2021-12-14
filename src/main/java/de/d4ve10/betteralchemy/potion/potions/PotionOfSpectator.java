package de.d4ve10.betteralchemy.potion.potions;

import de.d4ve10.betteralchemy.potion.ListenerPotion;
import de.d4ve10.betteralchemy.potion.PotionData;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class PotionOfSpectator extends ListenerPotion {

    private static final Map<Player, GameMode> gameModes = new HashMap<>();

    public PotionOfSpectator() {
        super(new PotionData("Potion Of Spectator", 10, 0), Color.GRAY);
        this.lore.add(ChatColor.BLUE + "Spectating{{DURATION}}");
    }

    @Override
    public void applyEffect(@NotNull LivingEntity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.getGameMode() != GameMode.SPECTATOR)
                gameModes.put(player, player.getGameMode());
            player.setGameMode(GameMode.SPECTATOR);
            super.applyEffect(entity);
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

    @EventHandler
    public void onPlayerTeleport(@NotNull PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE && this.hasEffect(event.getPlayer()))
            event.setCancelled(true);
    }
}
