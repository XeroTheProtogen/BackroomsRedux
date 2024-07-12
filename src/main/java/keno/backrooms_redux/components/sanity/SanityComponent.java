package keno.backrooms_redux.components.sanity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.components.BRComponentRegistry;
import keno.backrooms_redux.components.base.FloatComponent;
import keno.backrooms_redux.entity.BREntities;
import keno.backrooms_redux.entity.HallucinationEntity;
import keno.backrooms_redux.worldgen.biome.BRBiomes;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom;

public class SanityComponent implements FloatComponent, CommonTickingComponent, AutoSyncedComponent {
    private float sanity = 100.0f;
    private PlayerEntity player;

    private final Random random;

    public SanityComponent(PlayerEntity player) {
        this.player = player;
        this.random = new Xoroshiro128PlusPlusRandom((LimlibHelper.blockSeed(this.player.getBlockPos()) ^ 3));
    }

    @Override
    public float getValue() {
        return this.sanity;
    }

    @Override
    public void setValue(float value) {

        this.sanity = MathHelper.clamp(value, 0f, 100f);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.sanity = tag.getFloat("backrooms_redux.sanity");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("backrooms_redux.sanity", this.sanity);
    }

    @Override
    public void tick() {
        if (!player.isCreative() && !player.isSpectator()) {
            if (player.getWorld().getBiome(player.getBlockPos()).isIn(BRBiomes.BACKROOMS_BIOMES)) {
                if (this.sanity > 0f) {
                    setValue(getValue() - 0.000028f);
                }
                BRComponentRegistry.SANITY.sync(player);
            }
        }
        BackroomsRedux.LOGGER.info("Sanity: {}", getValue());
    }

    @Override
    public void serverTick() {
        CommonTickingComponent.super.serverTick();
        if (!this.player.getWorld().isClient()) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                if (!serverPlayer.isCreative() && !serverPlayer.isSpectator()) {
                    if (getValue() == 50 || getValue() == 30 || getValue() == 10) {
                        ServerWorld world = serverPlayer.getServerWorld();
                        HallucinationEntity hallucination = BREntities.HALLUCINATION.create(world);
                        world.spawnEntity(hallucination);
                        BlockPos pos = serverPlayer.getBlockPos().add(random.nextBetween(-10, 10),
                                0, random.nextBetween(-10, 10));
                        hallucination.setPos(pos.getX(), pos.getY() + 1, pos.getZ());
                    } else if (getValue() <= 0f) {
                        serverPlayer.kill();
                    }
                }
            }
        }
    }

    @Override
    public void clientTick() {
        CommonTickingComponent.super.clientTick();
        if (this.player.getWorld().isClient()) {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            ClientWorld world = client.world;
            assert player != null;
            if (this.sanity == 90.0f) {
                player.sendMessage(Text.translatable("backrooms_redux.sanity.beginning"), true);
            }

            if (this.sanity == 50.0f) {
                player.sendMessage(Text.of("As you lose hope, you begin to hallucinate..."), true);
            }

            if (this.sanity == 1.0f) {
                player.sendMessage(Text.of("The Wretch Cycle shall claim another..."), true);
            }

            if (this.sanity <= 5f) {
                if (this.random.nextFloat() <= 0.05f) {
                    world.playSoundAtBlockCenter(player.getBlockPos(), SoundEvents.BLOCK_STONE_BREAK,
                            SoundCategory.NEUTRAL, 1f, 0.8f, false);
                }
            }
        }
    }
}
