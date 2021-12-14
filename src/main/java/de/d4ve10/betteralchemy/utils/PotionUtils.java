package de.d4ve10.betteralchemy.utils;

import de.d4ve10.betteralchemy.BetterAlchemy;
import de.d4ve10.betteralchemy.potion.CustomPotion;
import de.d4ve10.betteralchemy.potion.PotionData;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PotionUtils {

    private final static List<Class<? extends Mob>> additionalMonsters = new ArrayList<>();

    static {
        additionalMonsters.add(Slime.class);
        additionalMonsters.add(Ghast.class);
        additionalMonsters.add(Wolf.class);
        additionalMonsters.add(Squid.class);
        additionalMonsters.add(IronGolem.class);
    }


    public static void spawnParticles(@NotNull Location location, int amount, @NotNull Color color, double offsetX, double offsetY, double offsetZ) {
        if (location.getWorld() == null)
            return;
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, amount, offsetX, offsetY, offsetZ, dustOptions);
    }

    public static void spawnParticles(@NotNull Location location, int amount, @NotNull Color color) {
        spawnParticles(location, amount, color, 0, 0, 0);
    }

    public static void removeAllEffects(@NotNull LivingEntity entity) {
        for (NamespacedKey key : entity.getPersistentDataContainer().getKeys()) {
            if (key.getNamespace().equalsIgnoreCase(BetterAlchemy.getInstance().getName()) && key.getKey().startsWith("potionof")) {
                CustomPotion potion = BetterAlchemy.getInstance().getPotionMaker().getPotion(key.getKey());
                if (potion == null)
                    continue;
                potion.removeEffect(entity);
            }else if (key.getNamespace().equalsIgnoreCase(BetterAlchemy.getInstance().getName())) {
                entity.getPersistentDataContainer().remove(key);
            }
        }
    }

    public static void applyAllEffects(@NotNull LivingEntity entity) {
        for (NamespacedKey key : entity.getPersistentDataContainer().getKeys()) {
            if (key.getNamespace().equalsIgnoreCase(BetterAlchemy.getInstance().getName()) && key.getKey().startsWith("potionof")) {
                PotionData potionData = entity.getPersistentDataContainer().get(key, BetterAlchemy.getInstance().getPotionDataPersistentDataType());
                if (potionData == null)
                    continue;
                CustomPotion potion = BetterAlchemy.getInstance().getPotionMaker().getPotion(key.getKey());
                if (potion == null)
                    continue;
                potion.setPotionData(potionData);
                potion.applyEffect(entity);
            }
        }
    }

    @NotNull
    public static String stripPotionName(@NotNull String potionName) {
        return ChatColor.stripColor(potionName).toLowerCase(Locale.ROOT).replaceAll("\\s+", "").replace("splash", "").replace("lingering", "");
    }

    @NotNull
    public static List<Location> getCircle(@NotNull Location center, double radius, int amount, boolean halfCircle) {
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        double directionX = -Math.sin(Math.toRadians(center.getYaw()));
        double directionZ = Math.cos(Math.toRadians(center.getYaw()));
        //System.out.println("DIRECTION: " + directionX + " " + directionZ);
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double angle = i * increment;
            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);
            //System.out.println("DIFFERENCE: " + Math.abs(cosAngle - directionX) + " " + Math.abs(sinAngle - directionZ));
            if (halfCircle && Math.abs(cosAngle - directionX) <= 0.8 && Math.abs(sinAngle - directionZ) <= 0.8) {
                //System.out.println("LOCATION: " + (center.getX() + (radius * cosAngle)) + " " + (center.getZ() + (radius * sinAngle)));
                continue;
            }
            double x = center.getX() + (radius * cosAngle);
            double z = center.getZ() + (radius * sinAngle);
            Location location = new Location(world, x, center.getY(), z);
            locations.add(location);
        }
        return locations;
    }

    public static boolean isHostile(@NotNull Mob mob) {
        if (mob instanceof Monster)
            return true;
        for (Class<? extends Mob> additionalMonster : additionalMonsters) {
            if (additionalMonster.isInstance(mob))
                return true;
        }
        return false;
    }

}
