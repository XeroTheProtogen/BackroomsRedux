package keno.backrooms_redux;

import keno.backrooms_redux.commands.BRCommands;
import keno.backrooms_redux.entity.BREntities;
import keno.backrooms_redux.entity.HallucinationEntity;
import keno.backrooms_redux.item.BRItemGroup;
import keno.backrooms_redux.networking.BRPackets;
import keno.backrooms_redux.networking.TeleportPlayer;
import keno.backrooms_redux.registry.BRCommonRegistry;
import keno.backrooms_redux.registry.BRSoundEvents;
import keno.backrooms_redux.server.events.AddVillageBuildingsCallback;
import keno.backrooms_redux.worldgen.biome.BRBiomes;
import keno.backrooms_redux.worldgen.chunk.BRChunkGenerators;
import keno.backrooms_redux.worldgen.piece_pools.PoolArraysSingleton;
import keno.backrooms_redux.worldgen.piece_pools.constants.BRPieceManagers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackroomsRedux implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String ID = "backrooms_redux";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final PoolArraysSingleton singleton = PoolArraysSingleton.getInstance();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		BRCommonRegistry.init();
		BRItemGroup.registerItemGroups();
		BRChunkGenerators.init();
		BRBiomes.registerBiomes();
		BRSoundEvents.init();
		BRPieceManagers.registerPieceManagers(PoolArraysSingleton.getInstance());
		BRCommands.init();
		BREntities.init();
		registerEntityAttributes();
		ServerLifecycleEvents.SERVER_STARTING.register(new AddVillageBuildingsCallback());
		ServerPlayNetworking.registerGlobalReceiver(BRPackets.TELEPORT_PLAYER_TO_BACKROOMS,
				((server, player, handler, buf, responseSender) ->
						server.execute(() -> TeleportPlayer.teleportPlayerToBackrooms(server, player))));
	}

	private void registerEntityAttributes() {
		FabricDefaultAttributeRegistry.register(BREntities.HALLUCINATION, HallucinationEntity.setAttributes());
	}


	// When retrieving things from redux, use this static method
	public static Identifier modLoc(String location) {
		return new Identifier(ID, location);
	}
	/**If retrieving an identifier for something from minecraft, use this method
	 * @param location a {@link String} that gives the identifier path
	 * @return A {@link Identifier} within the minecraft namespace
	 */
	public static Identifier mcLoc(String location) {
		return new Identifier("minecraft", location);
	}

	/**Used for debugging purposes */
	public static boolean isDevelopmentEnv() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
}