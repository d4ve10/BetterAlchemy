package de.d4ve10.betteralchemy.utils;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TeleportHandler {
    private final Entity entity;
    private final Random random;
    private final int x;
    private final int z;

    public TeleportHandler(@NotNull Entity entity, int x, int z) {
        this.entity = entity;
        this.x = x;
        this.z = z;
        this.random = new Random();
    }

    public void teleport() {
        Location location = this.getLocation();
        if (location != null) {
            PotionUtils.spawnParticles(this.entity.getLocation(), 100, Color.PURPLE, 0.5, 1, 0.5);
            this.entity.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            if (this.entity instanceof Player)
                ((Player) this.entity).playSound(entity.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            PotionUtils.spawnParticles(this.entity.getLocation(), 100, Color.PURPLE, 0.5, 1, 0.5);
        }
    }

    @Nullable
    protected Location getLocation() {
        int x = this.random.nextInt(this.x);
        int z = this.random.nextInt(this.z);
        int y = this.random.nextInt(10);
        x = this.randomizeType(x);
        z = this.randomizeType(z);
        y = this.randomizeType(y);

        return TeleportUtils.generateSaveLocation(this.entity.getLocation().add(x, y, z));
    }

    protected int randomizeType(int i) {
        int j = this.random.nextInt(2);
        if (j == 0)
            return -i;
        return i;
    }
}
