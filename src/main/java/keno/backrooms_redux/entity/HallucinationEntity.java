package keno.backrooms_redux.entity;

import keno.backrooms_redux.components.BRComponentRegistry;
import keno.backrooms_redux.components.sanity.SanityComponent;
import keno.backrooms_redux.utils.TickHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HallucinationEntity extends PassiveEntity implements GeoEntity, TickHelper {
    protected static final RawAnimation IDLE_ANIM =
            RawAnimation.begin().thenLoop("animation.hallucination.idle");
    protected static final RawAnimation WALK_ANIM =
            RawAnimation.begin().thenLoop("animation.hallucination.walk");
    protected static final RawAnimation PHASE_ANIM =
            RawAnimation.begin().thenPlayAndHold("animation.hallucination.phase");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int ticks = 1200;

    public HallucinationEntity(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return PassiveEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10f)
                .add(EntityAttributes.GENERIC_ARMOR, 0.3f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16f);
    }

    @Override
    public boolean canTakeDamage() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        RegistryKey<DamageType> genericKill = this.getDamageSources().genericKill().getTypeRegistryEntry().getKey().get();
        return !damageSource.isOf(genericKill);
    }

    @Override
    public void tick() {
        if (--this.ticks <= 1) {
            this.kill();
        }
        super.tick();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 8));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.goalSelector.add(2, new WanderAroundGoal(this, 1f, 4));
    }

    @Override
    public boolean shouldRenderName() {
        return true;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (!this.getWorld().isClient())
            if (this.getWorld().getClosestPlayer(this, 8d) instanceof ServerPlayerEntity player) {
                SanityComponent sanity = BRComponentRegistry.SANITY.get(player);
                sanity.setValue(sanity.getValue() - 5f);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 200));
            }
        super.onDeath(damageSource);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 10, this::getMainController));
    }

    protected <H extends HallucinationEntity> PlayState getMainController(final AnimationState<H> state) {
        state.getController().setOverrideEasingType(EasingType.EASE_IN_CIRC);
        if (state.getAnimatable().isDead()) {
            return PlayState.STOP;
        } else if (state.getAnimatable().getTimer() <= 60) {
            state.setAnimation(HallucinationEntity.PHASE_ANIM);
            return PlayState.CONTINUE;
        } else if (state.isMoving()) {
            return state.setAndContinue(HallucinationEntity.WALK_ANIM);
        }

        return state.setAndContinue(HallucinationEntity.IDLE_ANIM);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void setTimer(int ticks) {
        this.ticks = ticks;
    }

    @Override
    public int getTimer() {
        return this.ticks;
    }
}
