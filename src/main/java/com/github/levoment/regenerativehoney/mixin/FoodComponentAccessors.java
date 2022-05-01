package com.github.levoment.regenerativehoney.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(FoodComponent.class)
public interface FoodComponentAccessors {

    @Mutable
    @Accessor("saturationModifier")
    public void setSaturationModifier(float saturation);

    @Mutable
    @Accessor("statusEffects")
    public void setStatusEffects(List<Pair<StatusEffectInstance, Float>> statusEffects);
}
