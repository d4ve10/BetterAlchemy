package de.d4ve10.betteralchemy.potion.potions;

import de.d4ve10.betteralchemy.BetterAlchemy;
import de.d4ve10.betteralchemy.potion.CustomPotion;
import de.d4ve10.betteralchemy.potion.CustomPotionEvent;
import de.d4ve10.betteralchemy.potion.ListenerPotion;
import de.d4ve10.betteralchemy.potion.PotionData;
import de.d4ve10.betteralchemy.potion.potions.corrupted.PotionOfAdventure;
import de.d4ve10.betteralchemy.potion.potions.corrupted.PotionOfHostility;
import de.d4ve10.betteralchemy.potion.potions.corrupted.PotionOfWaterstorm;
import de.d4ve10.betteralchemy.utils.CorruptionHandler;
import de.d4ve10.betteralchemy.utils.PotionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import static org.bukkit.potion.PotionEffectType.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class PotionOfCorruption extends ListenerPotion {

    private static final Map<Object, Object> corruptionEffects = new HashMap<>();
    private static final Map<LivingEntity, Map<Object, Object>> playerCorruptedEffects = new HashMap<>();

    static {
        corruptionEffects.put(LUCK, UNLUCK);
        corruptionEffects.put(UNLUCK, LUCK);
        corruptionEffects.put(SPEED, SLOW);
        corruptionEffects.put(SLOW, SPEED);
        corruptionEffects.put(DOLPHINS_GRACE, SLOW);
        corruptionEffects.put(FAST_DIGGING, SLOW_DIGGING);
        corruptionEffects.put(SLOW_DIGGING, FAST_DIGGING);
        corruptionEffects.put(INCREASE_DAMAGE, WEAKNESS);
        corruptionEffects.put(DAMAGE_RESISTANCE, WEAKNESS);
        corruptionEffects.put(WEAKNESS, INCREASE_DAMAGE);
        corruptionEffects.put(HEAL, HARM);
        corruptionEffects.put(HARM, HEAL);
        corruptionEffects.put(JUMP, SLOW_FALLING);
        corruptionEffects.put(BLINDNESS, NIGHT_VISION);
        corruptionEffects.put(NIGHT_VISION, BLINDNESS);
        corruptionEffects.put(REGENERATION, POISON);
        corruptionEffects.put(POISON, REGENERATION);
        corruptionEffects.put(WITHER, REGENERATION);
        corruptionEffects.put(FIRE_RESISTANCE, WATER_BREATHING);
        corruptionEffects.put(INVISIBILITY, GLOWING);
        corruptionEffects.put(GLOWING, INVISIBILITY);
        corruptionEffects.put(HUNGER, SATURATION);
        corruptionEffects.put(SATURATION, HUNGER);
        corruptionEffects.put(HEALTH_BOOST, HARM);
        corruptionEffects.put(ABSORPTION, HARM);
        corruptionEffects.put(LEVITATION, SLOW_FALLING);
        corruptionEffects.put(SLOW_FALLING, LEVITATION);
        corruptionEffects.put(HERO_OF_THE_VILLAGE, BAD_OMEN);
        corruptionEffects.put(BAD_OMEN, HERO_OF_THE_VILLAGE);

        String potionOfDrowning = PotionUtils.stripPotionName(PotionOfDrowning.class.getSimpleName());
        String potionOfSpectator = PotionUtils.stripPotionName(PotionOfSpectator.class.getSimpleName());
        String potionOfFriendship = PotionUtils.stripPotionName(PotionOfFriendship.class.getSimpleName());
        String potionOfPoisonResistance = PotionUtils.stripPotionName(PotionOfPoisonResistance.class.getSimpleName());
        String potionOfFirestorm = PotionUtils.stripPotionName(PotionOfFirestorm.class.getSimpleName());
        String potionOfWarping = PotionUtils.stripPotionName(PotionOfWarping.class.getSimpleName());

        String potionOfHostility = PotionUtils.stripPotionName(PotionOfHostility.class.getSimpleName());
        String potionOfAdventure = PotionUtils.stripPotionName(PotionOfAdventure.class.getSimpleName());
        String potionOfWaterstorm = PotionUtils.stripPotionName(PotionOfWaterstorm.class.getSimpleName());

        corruptionEffects.put(WATER_BREATHING, potionOfDrowning);
        corruptionEffects.put(CONDUIT_POWER, potionOfDrowning);
        corruptionEffects.put(potionOfDrowning, WATER_BREATHING);
        corruptionEffects.put(potionOfFriendship, potionOfHostility);
        corruptionEffects.put(potionOfHostility, potionOfFriendship);
        corruptionEffects.put(potionOfSpectator, potionOfAdventure);
        corruptionEffects.put(potionOfAdventure, potionOfSpectator);
        corruptionEffects.put(potionOfPoisonResistance, POISON);
        corruptionEffects.put(potionOfFirestorm, potionOfWaterstorm);
        corruptionEffects.put(potionOfWaterstorm, potionOfFirestorm);
        corruptionEffects.put(potionOfWarping, SLOW);
    }

    public PotionOfCorruption() {
        super(new PotionData("Potion Of Corruption", 30, 0), Color.BLACK);
        this.lore.add(ChatColor.BLUE + "Corruption{{DURATION}}");
    }

    @Override
    public void applyEffect(@NotNull LivingEntity entity) {
        if (!this.hasEffect(entity)) {
            entity.getActivePotionEffects().forEach(effect -> applyCorruptionEffect(entity, effect));
            this.getActivePotionEffects(entity).forEach(effect -> applyCorruptionEffect(entity, effect));
        }
        super.applyEffect(entity);
    }

    @Override
    public void removeEffect(@NotNull LivingEntity entity) {
        super.removeEffect(entity);
        for (NamespacedKey key : entity.getPersistentDataContainer().getKeys()) {
            if (key.getNamespace().equalsIgnoreCase(BetterAlchemy.getInstance().getName()) && key.getKey().startsWith("corruption")) {
                Object type = PotionEffectType.getByName(key.getKey().replace("corruption", ""));
                if (type == null)
                    type = key.getKey().replace("corruption", "");
                removeCorruptionEffect(entity, type, true);
            }
        }
    }

    @EventHandler
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof LivingEntity))
            return;
        if (event.getAction().equals(EntityPotionEffectEvent.Action.CHANGED))
            return;
        LivingEntity entity = (LivingEntity) event.getEntity();

        if (!this.hasEffect(entity))
            return;
        if (event.getAction().equals(EntityPotionEffectEvent.Action.ADDED)) {
            PotionEffect potionEffect = event.getNewEffect();
            if (potionEffect == null || potionEffect.getDuration() == 0)
                return;
            if (entity.getPersistentDataContainer().has(
                    new NamespacedKey(
                            BetterAlchemy.getInstance(),
                            "corruption" + potionEffect.getType().getName().toLowerCase(Locale.ENGLISH)),
                    PersistentDataType.STRING))
                return;
            event.setCancelled(true);
            applyCorruptionEffect(entity, potionEffect);
        }
        else if (event.getAction().equals(EntityPotionEffectEvent.Action.REMOVED) || event.getAction().equals(EntityPotionEffectEvent.Action.CLEARED)) {
            PotionEffect potionEffect = event.getOldEffect();
            if (potionEffect == null || potionEffect.getDuration() == 0)
                return;
            removeCorruptionEffect(entity, potionEffect, false);
        }
    }

    @EventHandler
    public void onCustomPotionEffect(CustomPotionEvent event) {
        LivingEntity entity = event.getEntity();
        CustomPotion customPotion = event.getPotion();
        if (customPotion.getClass().getSimpleName().equals(this.getClass().getSimpleName()))
            return;
        if (!this.hasEffect(entity))
            return;
        if (event.getAction().equals(CustomPotionEvent.Action.POTION_APPLY)) {
            applyCorruptionEffect(entity, customPotion);
        }else if (event.getAction().equals(CustomPotionEvent.Action.POTION_REMOVE)) {
            removeCorruptionEffect(entity, customPotion, false);
        }
    }

    @Nullable
    private Object getCorruptionEffect(@NotNull Object object) {
        if (object instanceof PotionEffect)
            return corruptionEffects.get(((PotionEffect) object).getType());
        if (object instanceof CustomPotion)
            return corruptionEffects.get(PotionUtils.stripPotionName(((CustomPotion) object).getClass().getSimpleName()));
        return corruptionEffects.get(object);
    }

    @Nullable
    private Object getPlayerCorruptionEffect(@NotNull LivingEntity entity, @NotNull Object object) {
        if (!playerCorruptedEffects.containsKey(entity))
            return null;
        if (object instanceof PotionEffect)
            return playerCorruptedEffects.get(entity).get(((PotionEffect) object).getType());
        if (object instanceof CustomPotion)
            return playerCorruptedEffects.get(entity).get(PotionUtils.stripPotionName(((CustomPotion) object).getClass().getSimpleName()));
        return playerCorruptedEffects.get(entity).get(object);
    }

    private void applyCorruptionEffect(@NotNull LivingEntity entity, @NotNull Object effect) {
        if (!playerCorruptedEffects.containsKey(entity))
            playerCorruptedEffects.put(entity, new HashMap<>());
        Object corruptedEffect = getCorruptionEffect(effect);
        CorruptionHandler corruptionHandler = new CorruptionHandler(entity, effect, corruptedEffect);
        if (corruptionHandler.hasCorruptionEffect())
            return;
        playerCorruptedEffects.get(entity).put(corruptedEffect, effect instanceof PotionEffect ? ((PotionEffect) effect).getType() : effect);
        corruptionHandler.setCorruptionEffectSetting();
        corruptionHandler.removeNormalEffect();
        corruptionHandler.applyCorruptedEffect();
    }

    private void removeCorruptionEffect(@NotNull LivingEntity entity, @NotNull Object effect, boolean changeEffect) {
        Object corruptedEffect = getPlayerCorruptionEffect(entity, effect);
        CorruptionHandler corruptionHandler = new CorruptionHandler(entity, effect, corruptedEffect);
        if (!corruptionHandler.hasCorruptionEffect())
            return;
        if (changeEffect) {
            corruptionHandler.removeNormalEffect();
            corruptionHandler.applyCorruptedEffect();
        }
        corruptionHandler.removeCorruptionEffectSetting();
    }

}
