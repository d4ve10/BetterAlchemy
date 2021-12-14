package de.d4ve10.betteralchemy.potion.potions.corrupted;

import de.d4ve10.betteralchemy.potion.ListenerPotion;
import de.d4ve10.betteralchemy.potion.PotionData;
import de.d4ve10.betteralchemy.utils.PotionUtils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;


public class PotionOfWaterstorm extends ListenerPotion {

    private static final Map<LivingEntity, Location> nextLocation = new HashMap<>();

    public PotionOfWaterstorm() {
        super(new PotionData("Potion of Waterstorm", 30, 0), Color.AQUA);
        this.lore.add(ChatColor.BLUE + "Waterstorm{{DURATION}}");
        this.tickRate = 10;
    }

    @Override
    public void applyEffect(@NotNull LivingEntity entity) {
        Callable<Boolean> callable = () -> {
            for (Location pos : PotionUtils.getCircle(entity.getLocation(), 4, 25, false)) {
                int highestBlockY = entity.getWorld().getHighestBlockYAt(pos, HeightMap.MOTION_BLOCKING) + 1;
                if (Math.abs(entity.getLocation().getY() - highestBlockY) > 4)
                    continue;
                Location location = new Location(entity.getWorld(), pos.getX(), highestBlockY, pos.getZ());
                if (location.getBlock().getType() == Material.AIR) {
                    nextLocation.put(entity, location);
                    entity.launchProjectile(ThrownPotion.class);
                }
            }
            return true;
        };

        super.register();
        super.applyEffect(entity, callable);
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if (!(projectile instanceof ThrownPotion))
            return;
        ThrownPotion thrownPotion = (ThrownPotion) projectile;
        ProjectileSource projectileSource = projectile.getShooter();
        if (!(projectileSource instanceof LivingEntity))
            return;
        LivingEntity entity = (LivingEntity) projectileSource;
        if (!this.hasEffect(entity))
            return;
        if (!nextLocation.containsKey(entity))
            return;
        ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        if (potionMeta == null)
            return;
        potionMeta.setBasePotionData(new org.bukkit.potion.PotionData(PotionType.WATER));
        itemStack.setItemMeta(potionMeta);
        thrownPotion.setItem(itemStack);
        Location location = nextLocation.get(entity);
        thrownPotion.setSilent(true);
        thrownPotion.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        thrownPotion.setVelocity(new Vector(0, -10, 0));
        nextLocation.remove(entity);
    }

    @EventHandler
    public void onPotionLand(PotionSplashEvent event) {
        ThrownPotion thrownPotion = event.getPotion();
        if (!(thrownPotion.getShooter() instanceof Player))
            return;
        Player player = (Player) thrownPotion.getShooter();
        if (!this.hasEffect(player))
            return;
        player.stopAllSounds();
    }

}
