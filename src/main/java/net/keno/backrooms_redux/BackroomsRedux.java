package net.keno.backrooms_redux;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.keno.backrooms_redux.data.holders.noclip.NoClipPool;
import net.keno.backrooms_redux.data.holders.pos_determ.*;
import net.keno.backrooms_redux.data.listeners.LevelPoolListener;
import net.keno.backrooms_redux.data.listeners.NoclipPoolListener;
import net.keno.backrooms_redux.events.BREvents;
import net.keno.backrooms_redux.networking.BRNetworkingIds;
import net.keno.backrooms_redux.networking.NoClipPayload;
import net.keno.backrooms_redux.registry.BRCommon;
import net.keno.backrooms_redux.utils.DynamicUtils;
import net.ludocrypt.limlib.api.LimlibTravelling;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackroomsRedux implements ModInitializer {
	public static final String MOD_ID = "backrooms_redux";
	public static final Gson GSON = new GsonBuilder().create();

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final boolean isDevEnvironment = FabricLoader.getInstance().isDevelopmentEnvironment();

	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new LevelPoolListener());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new NoclipPoolListener());

		BRCommon.init();
		handleNetworking();
	}

	public static void handleNetworking() {
		PayloadTypeRegistry.playC2S().register(BRNetworkingIds.NO_CLIP_ID,
				NoClipPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(BRNetworkingIds.NO_CLIP_ID, (payload, context) -> {
			ServerPlayerEntity player = context.player();
			ServerWorld world = player.getServerWorld();

			RegistryKey<World> NO_CLIP_FROM = world.getRegistryKey();
			try {
				NoClipPool noclipPool = DynamicUtils.getNoclipPool(NO_CLIP_FROM.getValue());

                Pair<Identifier, PosDeterminer> pair = noclipPool.getRandomId(Random.create(LimlibHelper.blockSeed(player.getBlockPos())));
                Identifier id = pair.getFirst();

                if (id.equals(BackroomsRedux.modLoc("fail"))) {
                    player.damage(world, player.getDamageSources().magic(), 0.5f);
					player.sendMessage(Text.translatable("backrooms_redux.fail_noclip"), false);
                    return;
                }

                // Determine the position the player should teleport to
                PosDeterminer determiner = pair.getSecond();

                determiner.processPos(player);
                BlockPos determinedPos = determiner.getPos();

                RegistryKey<World> NO_CLIPPING_TO = RegistryKey.of(RegistryKeys.WORLD, id);
                ServerWorld destinationWorld = context.server().getWorld(NO_CLIPPING_TO);

                if (BREvents.CAN_NO_CLIP_EVENT.invoker().test(player, NO_CLIP_FROM, NO_CLIPPING_TO)) {
                    int x = determinedPos.getX();
                    int y = determinedPos.getY();
                    int z = determinedPos.getZ();

                    // Ensure the chunk the player spawns in is generated
                    world.setChunkForced(x, z, true);
                    LimlibTravelling.travelTo(player, destinationWorld,
                            new TeleportTarget(destinationWorld, new Vec3d(x, y, z),
                                    player.getVelocity(), player.getYaw(), player.getPitch(), TeleportTarget.NO_OP),
                            SoundEvents.BLOCK_PORTAL_TRAVEL, 0.8f, 1f);
                    BREvents.ON_NO_CLIP_EVENT.invoker().onNoClipCallback(player, NO_CLIP_FROM, NO_CLIPPING_TO);

                    // Avoid long-term server lag by notifying that the player has loaded into the dimension
                    world.setChunkForced(x, z, false);
                }
            } catch (Exception e) {
                player.sendMessageToClient(Text.translatable("backrooms_redux.cannot_noclip"), false);
            }
        });
	}

	public static Identifier modLoc(String loc) {
		return Identifier.of(MOD_ID, loc);
	}
}