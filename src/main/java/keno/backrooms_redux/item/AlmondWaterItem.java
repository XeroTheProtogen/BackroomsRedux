package keno.backrooms_redux.item;

import keno.backrooms_redux.components.BRComponentRegistry;
import keno.backrooms_redux.components.sanity.SanityComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AlmondWaterItem extends Item {
    public AlmondWaterItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            if (user instanceof ServerPlayerEntity player) {
                if (BRComponentRegistry.SANITY.isProvidedBy(player)) {
                    ItemStack stack = null;
                    if (player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof AlmondWaterItem) {
                        stack = player.getStackInHand(Hand.MAIN_HAND);
                    } else if (player.getStackInHand(Hand.OFF_HAND).getItem() instanceof AlmondWaterItem) {
                        stack = player.getStackInHand(Hand.OFF_HAND);
                    }
                    if (stack != null) {
                        SanityComponent component = BRComponentRegistry.SANITY.get(player);
                        component.setValue(MathHelper.clamp(component.getValue() + 3f, 1f, 100f));
                        BRComponentRegistry.SANITY.sync(user);
                        stack.decrement(1);
                        return TypedActionResult.success(stack, true);
                    }
                }
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }
}
