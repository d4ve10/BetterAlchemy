package de.d4ve10.betteralchemy.potion;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class PotionDataPersistentDataType implements PersistentDataType<String, PotionData> {

    @NotNull
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @NotNull
    @Override
    public Class<PotionData> getComplexType() {
        return PotionData.class;
    }

    @NotNull
    @Override
    public String toPrimitive(@NotNull PotionData complex, @NotNull PersistentDataAdapterContext context) {
        return complex.toString();
    }

    @NotNull
    @Override
    public PotionData fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return PotionData.convertFromString(primitive);
    }
}
