package de.d4ve10.betteralchemy.utils;

import de.d4ve10.betteralchemy.BetterAlchemy;
import de.d4ve10.betteralchemy.potion.CustomPotion;
import de.d4ve10.betteralchemy.potion.PotionData;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class CorruptionHandler {

    private String corruptionKeyName;
    private String keyName;
    private final LivingEntity entity;
    private int duration;
    private int amplifier;
    private Runnable applyCorruptionEffect;
    private Runnable removeNormalEffect;

    private CorruptionHandler(@NotNull LivingEntity entity) {
        this.entity = entity;
        this.corruptionKeyName = "";
        this.keyName = "";
        this.duration = 0;
        this.amplifier = 0;
        this.applyCorruptionEffect = () -> {};
        this.removeNormalEffect = () -> {};
    }

    public CorruptionHandler(@NotNull LivingEntity entity, @NotNull Object normalEffect, @Nullable Object corruptionEffect) {
        this(entity);
        if (normalEffect instanceof PotionEffectType) {
            PotionEffect potionEffect = entity.getPotionEffect((PotionEffectType) normalEffect);
            if (potionEffect != null)
                normalEffect = potionEffect;
        }else if (normalEffect instanceof String) {
            CustomPotion customPotion = getCustomPotion(entity, (String) normalEffect);
            if (customPotion != null)
                normalEffect = customPotion;
        }
        if (corruptionEffect instanceof String)
            corruptionEffect = getCustomPotion(entity, (String) corruptionEffect);

        if (normalEffect instanceof CustomPotion) {
            CustomPotion customPotion = (CustomPotion) normalEffect;
            if (corruptionEffect instanceof CustomPotion)
                initialize(customPotion, (CustomPotion) corruptionEffect);
            else if (corruptionEffect instanceof PotionEffectType)
                initialize(customPotion, (PotionEffectType) corruptionEffect);
            else
                initialize(customPotion, (PotionEffectType) null);
        }
        else if (normalEffect instanceof PotionEffect) {
            PotionEffect potionEffect = (PotionEffect) normalEffect;
            if (corruptionEffect instanceof CustomPotion)
                initialize(potionEffect, (CustomPotion) corruptionEffect);
            else if (corruptionEffect instanceof PotionEffectType)
                initialize(potionEffect, (PotionEffectType) corruptionEffect);
            else
                initialize(potionEffect, (PotionEffectType) null);
        }else if (normalEffect instanceof PotionEffectType) {
            initialize((PotionEffectType) normalEffect);
        }else if (normalEffect instanceof String) {
            initialize((String) normalEffect);
        }else
            BetterAlchemy.getInstance().getLogger().warning(
                    "Could not create corruption handler for entity " +
                            entity.getName() + " with normal effect " +
                            normalEffect + " and corruption effect " +
                            corruptionEffect);
    }

    @Nullable
    private CustomPotion getCustomPotion(@NotNull LivingEntity entity, @NotNull String name) {
        CustomPotion customPotion = BetterAlchemy.getInstance().getPotionMaker().getPotion(name);
        if (customPotion == null) {
            BetterAlchemy.getInstance().getLogger().warning("Could not find potion with name " + name + " for entity " + entity.getName());
            return null;
        }
        if (entity.getPersistentDataContainer().has(new NamespacedKey(BetterAlchemy.getInstance(), name), PersistentDataType.STRING)) {
            PotionData potionData = entity.getPersistentDataContainer().get(
                    new NamespacedKey(BetterAlchemy.getInstance(), name),
                    BetterAlchemy.getInstance().getPotionDataPersistentDataType());
            if (potionData != null)
                customPotion.setPotionData(potionData);
        }
        return customPotion;
    }



    private void initialize(@NotNull CustomPotion customPotion, @Nullable CustomPotion corruptionPotion) {
        initialize(customPotion);
        initializeCorruptionEffect(corruptionPotion);
        initializeNormalEffect(customPotion);
    }

    private void initialize(@NotNull PotionEffect potionEffect, @Nullable PotionEffectType corruptionEffectType) {
        initialize(potionEffect);
        initializeCorruptionEffect(corruptionEffectType);
        initializeNormalEffect(potionEffect.getType());
    }

    private void initialize(@NotNull PotionEffect potionEffect, @Nullable CustomPotion corruptionPotion) {
        initialize(potionEffect);
        initializeCorruptionEffect(corruptionPotion);
        initializeNormalEffect(potionEffect.getType());
    }

    private void initialize(@NotNull CustomPotion customPotion, @Nullable PotionEffectType corruptionEffectType) {
        initialize(customPotion);
        initializeCorruptionEffect(corruptionEffectType);
        initializeNormalEffect(customPotion);
    }

    private void initialize(@NotNull CustomPotion customPotion) {
        this.keyName = customPotion.getClass().getSimpleName();
        this.duration = customPotion.getPotionData().getDuration();
        this.amplifier = customPotion.getPotionData().getAmplifier();
    }

    private void initialize(@NotNull PotionEffect potionEffect) {
        this.keyName = potionEffect.getType().getName();
        this.duration = potionEffect.getDuration() / 20;
        this.amplifier = potionEffect.getAmplifier();
    }

    private void initialize(@NotNull PotionEffectType potionEffectType) {
        this.keyName = potionEffectType.getName();
    }

    private void initialize(@NotNull String customPotionName) {
        this.keyName = customPotionName;
    }

    private void initializeNormalEffect(@NotNull CustomPotion customPotion) {
        this.removeNormalEffect = () -> customPotion.removeEffect(entity);
    }

    private void initializeNormalEffect(@NotNull PotionEffectType corruptionEffectType) {
        this.removeNormalEffect = () -> entity.removePotionEffect(corruptionEffectType);
    }

    private void initializeCorruptionEffect(@Nullable CustomPotion corruptionPotion) {
        if (corruptionPotion == null)
            return;
        this.corruptionKeyName = corruptionPotion.getClass().getSimpleName();
        this.applyCorruptionEffect = () -> {
            PotionData potionData = new PotionData(corruptionPotion.getPotionData().getName(), duration, amplifier);
            corruptionPotion.setPotionData(potionData);
            corruptionPotion.applyEffect(entity);
        };
    }

    private void initializeCorruptionEffect(@Nullable PotionEffectType corruptionEffectType) {
        if (corruptionEffectType == null)
            return;
        this.corruptionKeyName = corruptionEffectType.getName();
        this.applyCorruptionEffect = () -> entity.addPotionEffect(new PotionEffect(corruptionEffectType, duration * 20, amplifier));
    }


    public void setCorruptionEffectSetting() {
        this.entity.getPersistentDataContainer().set(
                new NamespacedKey(
                        BetterAlchemy.getInstance(),
                        "corruption" + this.corruptionKeyName.toLowerCase(Locale.ENGLISH)),
                PersistentDataType.STRING,
                "true");
    }

    public boolean hasCorruptionEffect() {
        return this.entity.getPersistentDataContainer().has(
                new NamespacedKey(
                        BetterAlchemy.getInstance(),
                        "corruption" + this.keyName.toLowerCase(Locale.ENGLISH)),
                PersistentDataType.STRING);
    }

    public void removeCorruptionEffectSetting() {
        this.entity.getPersistentDataContainer().remove(
                new NamespacedKey(
                        BetterAlchemy.getInstance(),
                        "corruption" + this.keyName.toLowerCase(Locale.ENGLISH)));
    }

    public void applyCorruptedEffect() {
        applyCorruptionEffect.run();
    }

    public void removeNormalEffect() {
        removeNormalEffect.run();
    }
}
