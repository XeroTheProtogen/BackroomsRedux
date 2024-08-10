package keno.backrooms_redux.entity.goals;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.PounceAtTargetGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;

import java.util.EnumSet;

public class LungeAttackGoal extends PounceAtTargetGoal {
    protected MobEntity mob;
    protected LivingEntity target;
    private final float damage;

    public LungeAttackGoal(MobEntity mob, float velocity, float damage) {
        super(mob, velocity);
        this.mob = mob;
        this.damage = damage;
        this.setControls(EnumSet.of(Control.JUMP, Control.TARGET, Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (mob.getTarget() != null) {
            this.target = mob.getTarget();
            if (!this.target.isInvulnerable()) {
                if (this.mob.canSee(this.target)) {
                    return super.canStart();
                }
            }
        }
        return false;
    }

    @Override
    public void start() {
        super.start();
        attackTarget(this.mob, this.target);
    }

    public void attackTarget(MobEntity mob, LivingEntity target) {
        if (target != null) {
            if (mob.isInAttackRange(target)) {
                if (target.isBlocking()) {
                    mob.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 2));
                    mob.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 5, 1));
                    mob.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 5, 0));
                    mob.setTarget(null);
                } else {
                    this.mob.setAttacking(true);
                    this.target.damage(target.getDamageSources().mobAttack(mob), this.damage);
                }
            }
        }
    }
}
