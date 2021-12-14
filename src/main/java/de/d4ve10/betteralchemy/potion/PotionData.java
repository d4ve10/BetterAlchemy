package de.d4ve10.betteralchemy.potion;

import org.jetbrains.annotations.NotNull;

public class PotionData {

    private String name;
    private int duration;
    private int originalDuration;
    private int amplifier;
    private int originalAmplifier;

    public PotionData(@NotNull String name, int duration, int amplifier) {
        this.name = name;
        this.duration = duration;
        this.originalDuration = duration;
        this.amplifier = amplifier;
        this.originalAmplifier = amplifier;
    }

    public PotionData(@NotNull String name, int duration, int originalDuration, int amplifier, int originalAmplifier) {
        this.name = name;
        this.duration = duration;
        this.originalDuration = originalDuration;
        this.amplifier = amplifier;
        this.originalAmplifier = originalAmplifier;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getOriginalDuration() {
        return originalDuration;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public int getOriginalAmplifier() {
        return originalAmplifier;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
        if (duration > this.originalDuration)
            this.originalDuration = duration;
    }

    public void setAmplifier(int amplifier) {
        this.amplifier = amplifier;
        if (amplifier != this.originalAmplifier)
            this.originalAmplifier = amplifier;
    }

    @NotNull
    public static PotionData convertFromString(@NotNull String string) {
        String[] split = string.split(";");
        if (split.length != 5)
            return new PotionData("", 0, 0);
        return new PotionData(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]));
    }

    @NotNull
    public String toString() {
        return this.name + ";" + this.duration + ";" + this.originalDuration + ";" + this.amplifier + ";" + this.originalAmplifier;
    }

}
