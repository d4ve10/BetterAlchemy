package de.d4ve10.betteralchemy.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class TeleportUtils {

    public static final Vector3D[] VOLUME;
    private static final Set<Material> UNSAFE_MATERIALS = new HashSet<>();

    static {
        UNSAFE_MATERIALS.add(Material.LAVA);
        UNSAFE_MATERIALS.add(Material.FIRE);
        UNSAFE_MATERIALS.add(Material.CACTUS);
        UNSAFE_MATERIALS.add(Material.MAGMA_BLOCK);
        List<Vector3D> pos = new ArrayList<>();

        for (int x = -3; x <= 3; ++x) {
            for (int y = -3; y <= 3; ++y) {
                for (int z = -3; z <= 3; ++z) {
                    pos.add(new Vector3D(x, y, z));
                }
            }
        }

        pos.sort(Comparator.comparingInt(a -> a.x * a.x + a.y * a.y + a.z * a.z));
        VOLUME = pos.toArray(new Vector3D[0]);
    }

    private TeleportUtils() {
    }

    public static boolean isBlockAboveAir(@NotNull World world, int x, int y, int z) {
        return y > world.getMaxHeight() || !world.getBlockAt(x, y - 1, z).getType().isSolid();
    }

    public static boolean isBlockUnsafe(@NotNull World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        Block below = world.getBlockAt(x, y - 1, z);
        Block above = world.getBlockAt(x, y + 1, z);
        return UNSAFE_MATERIALS.contains(below.getType()) || block.getType().isSolid() || above.getType().isSolid() || isBlockAboveAir(world, x, y, z);
    }

    @Nullable
    public static Location generateSaveLocation(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null)
            return null;
        int x = location.getBlockX();
        int y = (int) location.getY();
        int z = location.getBlockZ();
        int origX = x;
        int origY = y;
        int origZ = z;
        location.setY(world.getHighestBlockYAt(location));

        while (isBlockAboveAir(world, x, y, z)) {
            --y;
            if (y < 0) {
                y = origY;
                break;
            }
        }

        if (isBlockUnsafe(world, x, y, z)) {
            x = Math.round(location.getX()) == (long) x ? x - 1 : x + 1;
            z = Math.round(location.getZ()) == (long) z ? z - 1 : z + 1;
        }

        for (int i = 0; isBlockUnsafe(world, x, y, z); z = origZ + VOLUME[i].z) {
            ++i;
            if (i >= VOLUME.length) {
                x = origX;
                y = origY + 3;
                z = origZ;
                break;
            }

            x = origX + VOLUME[i].x;
            y = origY + VOLUME[i].y;
        }

        while (isBlockUnsafe(world, x, y, z)) {
            ++y;
            if (y >= world.getMaxHeight()) {
                ++x;
                break;
            }
        }

        while (isBlockUnsafe(world, x, y, z)) {
            --y;
            if (y <= 1) {
                ++x;
                y = world.getHighestBlockYAt(x, z);
                if (x - 48 > location.getBlockX()) {
                    return null;
                }
            }
        }

        return new Location(world, (double) x + 0.5d, y, (double) z + 0.5d, location.getYaw(), location.getPitch());
    }

    public static class Vector3D {
        public final int x;
        public final int y;
        public final int z;

        public Vector3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

}
