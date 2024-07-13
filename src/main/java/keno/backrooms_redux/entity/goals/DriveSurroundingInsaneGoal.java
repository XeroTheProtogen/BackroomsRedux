package keno.backrooms_redux.entity.goals;

import keno.backrooms_redux.components.BRComponentRegistry;
import keno.backrooms_redux.components.sanity.SanityComponent;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.List;

public class DriveSurroundingInsaneGoal extends Goal {
    public MobEntity mob;
    public int range;
    private final float chance;
    public int cooldown;
    public float sanityLoss;

    public DriveSurroundingInsaneGoal(MobEntity mob, int range, float chance, float sanityDrain, int cooldownTicks) {
        this.mob = mob;
        this.range = range;
        this.chance = chance;
        this.cooldown = cooldownTicks;
        this.sanityLoss = sanityDrain;
        this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (this.mob.getTarget() != null) {
            return this.mob.isInRange(this.mob.getTarget(), this.range);
        }
        return false;
    }

    @Override
    public void tick() {
        if (--this.cooldown <= 0) {
            Vec3d backCorner = this.mob.getPos().subtract(this.range, this.range, this.range);
            Vec3d frontCorner = this.mob.getPos().add(this.range, this.range, this.range);
            Box surroundingArea = new Box(backCorner, frontCorner);
            if (!this.mob.getWorld().getEntitiesByClass(ServerPlayerEntity.class, surroundingArea, (entity) -> true).isEmpty()) {
                List<ServerPlayerEntity> players = this.mob.getWorld()
                        .getEntitiesByClass(ServerPlayerEntity.class, surroundingArea, (player -> true));
                if (this.mob.getRandom().nextFloat() < chance) {
                    for (ServerPlayerEntity player : players) {
                        SanityComponent sanity = BRComponentRegistry.SANITY.get(player);
                        sanity.setValue(sanity.getValue() - this.sanityLoss);
                        BRComponentRegistry.SANITY.sync(player);
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 200));
                    }
                }
            }
        }
        super.tick();
    }
}
