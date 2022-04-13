package com.github.levoment.regenerativehoney.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(HoneyBottleItem.class)
public class HoneyBottleMixin {
	@Inject(at = @At("RETURN"), method = "finishUsing", cancellable = true)
	private void finishUsingCallback(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
		if (user instanceof ServerPlayerEntity serverPlayerEntity) {
			// Create a list to store the status effects of the regeneration potion
			List<StatusEffectInstance> listOfStatusEffectInstance = new ArrayList<>();
			// Get the list of status effects for the long regeneration potion
			listOfStatusEffectInstance = PotionUtil.getPotionEffects(Potion.byId("minecraft:regeneration"), listOfStatusEffectInstance);
			// Get an iterator for the potion effects list
			Iterator potionEffectsIterator = listOfStatusEffectInstance.iterator();

			while(potionEffectsIterator.hasNext()) {
				// Get an instance of the current status effect in the listOfStatusEffectInstance
				StatusEffectInstance statusEffectInstance = (StatusEffectInstance)potionEffectsIterator.next();
				if (statusEffectInstance.getEffectType().isInstant()) {
					statusEffectInstance.getEffectType().applyInstantEffect(user, user, user, statusEffectInstance.getAmplifier(), 1.0D);
				} else {
					user.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
				}
			}
		}
		// If the stack is 0, return an empty stack
		if (stack.getCount() == 0) cir.setReturnValue(ItemStack.EMPTY);
	}

	@Redirect(method = "finishUsing",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;finishUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;"))
	public ItemStack redirectFinishUsing(Item instance, ItemStack stack, World world, LivingEntity user) {
		if (user instanceof ServerPlayerEntity serverPlayerEntity) {
			// Make the player eat a chorus fruit to get the chorus fruit hunger and saturation effect
			serverPlayerEntity.getHungerManager().eat(Items.CHORUS_FRUIT, ItemStack.EMPTY);
			// If the player is not in creative, reduce the stack number of honey bottle items
			if (!serverPlayerEntity.getAbilities().creativeMode) stack.decrement(1);
		}
		return stack;
	}
}
