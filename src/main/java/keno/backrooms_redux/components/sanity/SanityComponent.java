package keno.backrooms_redux.components.sanity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import keno.backrooms_redux.components.BRComponentRegistry;
import keno.backrooms_redux.components.base.FloatComponent;
import keno.backrooms_redux.worldgen.biome.BRBiomes;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom;

public class SanityComponent implements FloatComponent, CommonTickingComponent, AutoSyncedComponent {
    private float sanity = 100f;
    private final PlayerEntity player;
    private int ticks = 0;
    private boolean sanity_trickling = false;
    Random random;

    public SanityComponent(PlayerEntity player) {
        this.player = player;
        this.random = new Xoroshiro128PlusPlusRandom((LimlibHelper.blockSeed(this.player.getBlockPos()) ^ 3));
    }

    @Override
    public float getValue() {
        return sanity;
    }

    @Override
    public void setValue(float value) {
        this.sanity = value;
        BRComponentRegistry.SANITY.sync(player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        tag.getFloat("backrooms_redux.sanity");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("backrooms_redux.sanity", this.sanity);
    }

    @Override
    public void tick() {
        if (!this.player.getWorld().isClient()) {
            if (this.player instanceof ServerPlayerEntity serverPlayer) {
                if (serverPlayer.getWorld().getBiome(serverPlayer.getBlockPos()).isIn(BRBiomes.BACKROOMS_BIOMES)) {
                    if (this.sanity <= 80f && --this.ticks <= 0) {
                        this.ticks = random.nextBetween(60, 240);
                    }
                    if (this.sanity > 0f) {
                        this.sanity -= 0.000028f;
                    }
                    BRComponentRegistry.SANITY.sync(player);
                }
            }
        }
    }

    @Override
    public void serverTick() {
        CommonTickingComponent.super.serverTick();
        if (this.sanity <= 0) {
            if (!this.player.getWorld().isClient()) {
                if (this.player instanceof ServerPlayerEntity serverPlayer) serverPlayer.kill();
            }
        }
    }

    @Override
    public void clientTick() {
        CommonTickingComponent.super.clientTick();
        if (this.player.getWorld().isClient()) {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            assert player != null;
                if (this.sanity <= 90f && !this.sanity_trickling) {
                    player.sendMessage(Text.translatable("backrooms_redux.sanity.beginning"), true);
                    this.sanity_trickling = true;
                    BRComponentRegistry.SANITY.sync(player);
                }
                if (this.sanity <= 75f) {
                    ClientWorld world = player.clientWorld;
                    BlockPos pos = player.getBlockPos().add(this.random.nextBetween(-5, 5),
                            this.random.nextBetween(-5, 5),
                            this.random.nextBetween(-5, 5));
                    world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.NEUTRAL, 1f, 0.8f, true);}
        }
    }
}
