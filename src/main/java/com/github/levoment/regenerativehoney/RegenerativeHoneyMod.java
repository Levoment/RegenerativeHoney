package com.github.levoment.regenerativehoney;

import com.github.levoment.regenerativehoney.mixin.FoodComponentAccessors;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponents;
import net.minecraft.potion.Potions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RegenerativeHoneyMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("regenerativehoney");

	@Override
	public void onInitialize() {
		// Get the saturation modifier of the Chorus Fruit
		float chorusFruitSaturation = FoodComponents.CHORUS_FRUIT.getSaturationModifier();
		// Set the saturation of the Honey bottle to be like the chorus fruit saturation
		((FoodComponentAccessors)FoodComponents.HONEY_BOTTLE).setSaturationModifier(chorusFruitSaturation);

		// Create a list to hold the status effects of strong regeneration
		List<StatusEffectInstance> listOfStatusEffectInstance = new ArrayList<>();
		// Get the list of status effects for the long regeneration potion
		listOfStatusEffectInstance = Potions.STRONG_REGENERATION.getEffects();

		// Create a list to hold the list of effects to add to the Honey Bottle
		List<Pair<StatusEffectInstance, Float>> regenerationStatusEffects = new ArrayList<>();
		listOfStatusEffectInstance.forEach(statusEffectInstance -> {
			// Create a new status effect like the current one, but with duration of 80 ticks
			StatusEffectInstance newEffectInstance = new StatusEffectInstance(statusEffectInstance.getEffectType(), 80);
			// Add the effect to the list
			regenerationStatusEffects.add(new Pair<>(newEffectInstance, 1.0f));
		});

		// Set the status effect of the regeneration for the Honey Bottle
		((FoodComponentAccessors)FoodComponents.HONEY_BOTTLE).setStatusEffects(regenerationStatusEffects);

	}
}
