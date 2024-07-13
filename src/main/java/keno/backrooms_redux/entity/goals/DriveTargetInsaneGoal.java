package keno.backrooms_redux.entity.goals;

import keno.backrooms_redux.components.BRComponentRegistry;
import keno.backrooms_redux.components.sanity.SanityComponent;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.EnumSet;

public class DriveTargetInsaneGoal extends Goal {
    public MobEntity mob;
    public float insanityDrained;
    public float range;
    public int cooldown;

    public DriveTargetInsaneGoal(MobEntity mob, float insanityDrained, float range,int cooldownTicks) {
        this.mob = mob;
        this.insanityDrained = insanityDrained;
        this.range = range;
        this.cooldown = cooldownTicks;
        this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return this.mob.getTarget() != null && --this.cooldown <= 0;
    }

    @Override
    public void tick() {
        if (!this.mob.getWorld().isClient()) {
            if (this.mob.getTarget() instanceof ServerPlayerEntity player) {
                if (this.mob.distanceTo(player) <= this.range) {
                    if (BRComponentRegistry.SANITY.isProvidedBy(player)) {
                        SanityComponent sanity = BRComponentRegistry.SANITY.get(player);
                        sanity.setValue(sanity.getValue() - this.insanityDrained);
                        BRComponentRegistry.SANITY.sync(player);
                    }
                }
            }
        }
        super.tick();
    }
}
