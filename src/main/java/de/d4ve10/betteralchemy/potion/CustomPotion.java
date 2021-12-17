package de.d4ve10.betteralchemy.potion;

import de.d4ve10.betteralchemy.BetterAlchemy;
import de.d4ve10.betteralchemy.CustomItem;
import de.d4ve10.betteralchemy.utils.PotionUtils;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.Callable;

public abstract class CustomPotion implements CustomItem {

    private static final Map<String, Map<LivingEntity, BukkitRunnable>> activeThreads = new HashMap<>();
    private static final Map<String, Map<LivingEntity, BukkitRunnable>> activeParticles = new HashMap<>();
    private static final Map<String, Map<Player, BossBar>> activeBars = new HashMap<>();
    private static final Map<String, Map<LivingEntity, CustomPotion>> activeEffects = new HashMap<>();
    private static final Map<LivingEntity, String> activeDeathMessages = new HashMap<>();

    public enum PotionVariant {
        NORMAL,
        SPLASH,
        LINGERING
    }


    @NotNull
    protected String deathMessage;

    protected int tickRate;

    @NotNull
    protected Color color;

    @NotNull
    protected final ChatColor chatColor;

    @NotNull
    protected final List<PotionEffectType> potionEffectTypes;

    @NotNull
    protected final ArrayList<String> lore;

    @NotNull
    protected PotionData potionData;

    @NotNull
    protected PotionVariant potionVariant;

    @NotNull
    public static String getLastDeathCause(@NotNull Player player) {
        if (CustomPotion.activeDeathMessages.containsKey(player))
            return CustomPotion.activeDeathMessages.get(player);
        return "died";
    }

    public boolean hasEffect(@NotNull LivingEntity entity) {
        return CustomPotion.activeThreads.get(this.getClass().getSimpleName()).containsKey(entity);
    }

    public List<String> getActivePotionEffects(@NotNull LivingEntity entity) {
        List<String> potions = new ArrayList<>();
        activeEffects.forEach((key, value) -> {
            if (value.containsKey(entity)) {
                potions.add(PotionUtils.stripPotionName(value.get(entity).getClass().getSimpleName()));
            }
        });
        return potions;
    }

    @NotNull
    public PotionData getPotionData() {
        return potionData;
    }

    protected void updateLastDeathCause(@NotNull Player player) {
        activeDeathMessages.put(player, deathMessage);
    }

    public static void removeLastDeathCause(@NotNull Player player) {
        activeDeathMessages.remove(player);
    }

    public void setPotionVariant(@NotNull PotionVariant potionVariant) {
        this.potionVariant = potionVariant;
    }

    public void setPotionData(@NotNull PotionData potionData) {
        this.potionData = potionData;
    }


    public CustomPotion(@NotNull PotionData potionData, @NotNull Color color) {
        this();
        this.potionData = potionData;
        this.color = color;
    }

    private CustomPotion() {
        this.tickRate = 20;
        this.potionVariant = PotionVariant.NORMAL;
        this.potionData = new PotionData("", 0, 0);
        this.potionEffectTypes = new ArrayList<>();
        this.color = Color.WHITE;
        this.deathMessage = "died";
        this.chatColor = ChatColor.WHITE;
        this.lore = new ArrayList<>();
        String className = this.getClass().getSimpleName();
        if (!activeThreads.containsKey(className))
            activeThreads.put(className, new HashMap<>());
        if (!activeParticles.containsKey(className))
            activeParticles.put(className, new HashMap<>());
        if (!activeBars.containsKey(className))
            activeBars.put(className, new HashMap<>());
        if (!activeEffects.containsKey(className))
            activeEffects.put(className, new HashMap<>());
    }

    @Nullable
    public final ItemStack create() {
        Material material;
        if (potionVariant == PotionVariant.SPLASH)
            material = Material.SPLASH_POTION;
        else if (potionVariant == PotionVariant.LINGERING) {
            material = Material.LINGERING_POTION;
            this.getPotionData().setDuration(this.potionData.getOriginalDuration() / 6);
        }
        else
            material = Material.POTION;
        ItemStack potion = new ItemStack(material);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        if (meta == null)
            return null;
        meta.setColor(this.color);
        if (!this.potionEffectTypes.isEmpty()) {
            this.potionEffectTypes.forEach(potionEffectType ->
                    meta.addCustomEffect(potionEffectType.createEffect(
                            this.potionData.getDuration() * 20,
                            this.potionData.getAmplifier()),
                            true));
        }else {
            meta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 0, false, false, false), true);
        }
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setDisplayName(this.chatColor + this.potionData.getName());
        meta.setLocalizedName(this.potionData.getName());
        if (this.lore.size() > 0)
            meta.setLore(this.getDurationLore());
        potion.setItemMeta(meta);
        this.potionData.setDuration(this.potionData.getOriginalDuration());
        return potion;
    }

    abstract public void applyEffect(@NotNull LivingEntity entity);

    protected final void applyEffect(@NotNull LivingEntity entity, @Nullable Callable<Boolean> runnable) {
        String className = this.getClass().getSimpleName();
        if (this.potionVariant == PotionVariant.LINGERING)
            this.potionData.setDuration(this.potionData.getOriginalDuration() / 6);
        Map<LivingEntity, BukkitRunnable> potionTasks = activeThreads.get(className);
        Map<LivingEntity, BukkitRunnable> potionParticles = activeParticles.get(className);
        Map<Player, BossBar> bossBars = activeBars.get(className);
        Map<LivingEntity, CustomPotion> potionEffects = activeEffects.get(className);
        if (potionTasks.containsKey(entity) && !potionTasks.get(entity).isCancelled()) {
            potionTasks.get(entity).cancel();
        }
        if (potionParticles.containsKey(entity) && !potionParticles.get(entity).isCancelled()) {
            potionParticles.get(entity).cancel();
        }
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (!bossBars.containsKey(player)) {
                BossBar bossBar = Bukkit.createBossBar(this.getPotionData().getName(), BarColor.PURPLE, BarStyle.SOLID);
                bossBar.addPlayer(player);
                bossBars.put(player, bossBar);
            }
        }
        if (potionEffects.containsKey(entity) && potionEffects.get(entity) instanceof ListenerPotion) {
            potionEffects.remove(entity);
        }


        double maxDuration = this.potionVariant == PotionVariant.LINGERING ? this.potionData.getDuration() : this.potionData.getOriginalDuration();
        BukkitRunnable task = new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                counter++;
                if (potionData.getDuration() <= 0) {
                    removeEffect(entity);
                    cancel();
                    return;
                }
                entity.getPersistentDataContainer().set(new NamespacedKey(BetterAlchemy.getInstance(), className),
                        BetterAlchemy.getInstance().getPotionDataPersistentDataType(),
                        potionData);
                try {
                    if (runnable != null && !runnable.call())
                        return;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // run every second
                if (counter % (20 / tickRate) == 0) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        if (bossBars.containsKey(player)) {
                            BossBar potionBar = bossBars.get(player);
                            potionBar.setProgress(potionData.getDuration() / maxDuration);
                            potionBar.addPlayer(player);
                            potionBar.setVisible(true);
                        }
                    }
                    potionData.setDuration(potionData.getDuration() - 1);
                }
            }
        };
        BukkitRunnable particleTask = new BukkitRunnable() {
            @Override
            public void run() {
                spawnParticles(entity.getLocation().add(0, 1, 0), 1);
            }
        };
        task.runTaskTimer(BetterAlchemy.getInstance(), 0, tickRate);
        particleTask.runTaskTimer(BetterAlchemy.getInstance(), 0, 2);

        potionTasks.put(entity, task);
        potionParticles.put(entity, particleTask);
        potionEffects.put(entity, this);
        activeThreads.put(className, potionTasks);
        activeParticles.put(className, potionParticles);
        activeBars.put(className, bossBars);
        activeEffects.put(className, potionEffects);
        Bukkit.getServer().getPluginManager().callEvent(new CustomPotionEvent(entity, this, CustomPotionEvent.Action.POTION_APPLY));
    }

    public void removeEffect(@NotNull LivingEntity entity) {
        String className = this.getClass().getSimpleName();
        CustomPotion.removeEffect(entity, className);
    }

    public static void removeEffect(@NotNull LivingEntity entity, @NotNull String name) {
        Map<LivingEntity, BukkitRunnable> potionTasks = activeThreads.get(name);
        Map<LivingEntity, BukkitRunnable> potionParticles = activeParticles.get(name);
        Map<Player, BossBar> bossBars = activeBars.get(name);
        Map<LivingEntity, CustomPotion> potionEffects = activeEffects.get(name);
        entity.getPersistentDataContainer().remove(new NamespacedKey(BetterAlchemy.getInstance(), name));
        if (potionTasks != null && potionTasks.containsKey(entity)) {
            potionTasks.get(entity).cancel();
            potionTasks.remove(entity);
        }
        if (potionParticles != null && potionParticles.containsKey(entity)) {
            potionParticles.get(entity).cancel();
            potionParticles.remove(entity);
        }
        if (bossBars != null && entity instanceof Player && bossBars.containsKey((Player) entity)) {
            BossBar bossBar = bossBars.get(entity);
            bossBar.setVisible(false);
            bossBar.removePlayer((Player) entity);
            bossBars.remove(entity);
        }
        if (potionEffects != null) {
            Bukkit.getServer().getPluginManager().callEvent(new CustomPotionEvent(entity, potionEffects.get(entity), CustomPotionEvent.Action.POTION_REMOVE));
            potionEffects.remove(entity);
        }
    }

    public static void unloadAll(@NotNull LivingEntity entity) {
        activeThreads.forEach((name, tasks) -> {
            if (tasks.containsKey(entity)) {
                tasks.get(entity).cancel();
                tasks.remove(entity);
            }
        });
        activeParticles.forEach((name, tasks) -> {
            if (tasks.containsKey(entity)) {
                tasks.get(entity).cancel();
                tasks.remove(entity);
            }
        });
        if (entity instanceof Player) {
            Player player = (Player) entity;
            activeBars.forEach((name, bars) -> {
                if (bars.containsKey(player)) {
                    BossBar bossBar = bars.get(player);
                    bossBar.setVisible(false);
                    bossBar.removePlayer(player);
                    bars.remove(player);
                }
            });
        }
        activeEffects.forEach((name, effects) -> effects.remove(entity));
    }

    public final void spawnParticles(@NotNull Location location, int amount) {
        PotionUtils.spawnParticles(location, amount, this.color, 0.5, 0.5, 0.5);
    }

    @NotNull
    private List<String> getDurationLore() {
        List<String> lore = new ArrayList<>();
        for (String line : this.lore) {
            String newLine = line;
            if (newLine.contains("{{DURATION}}")) {
                if (this.potionData.getAmplifier() == 1)
                    newLine = newLine.replace("{{DURATION}}", " II{{DURATION}}");
                if (this.potionData.getDuration() > (Short.MAX_VALUE / 2))
                    newLine = newLine.replace("{{DURATION}}", " ∞");
                else
                    newLine = newLine.replace("{{DURATION}}", String.format(Locale.ENGLISH, " (%d:%02d)", (this.potionData.getDuration() % 3600) / 60, this.potionData.getDuration() % 60));
            }
            lore.add(newLine);
        }
        return lore;
    }

    @NotNull
    @Override
    public String toString() {
        return this.potionData.getName();
    }
}
