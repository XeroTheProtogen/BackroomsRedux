package keno.backrooms_redux;

import keno.backrooms_redux.commands.BRCommands;
import keno.backrooms_redux.item.BRItemGroup;
import keno.backrooms_redux.networking.BRPackets;
import keno.backrooms_redux.networking.TeleportPlayer;
import keno.backrooms_redux.registry.BRCommonRegistry;
import keno.backrooms_redux.registry.BRSoundEvents;
import keno.backrooms_redux.worldgen.biome.BRBiomes;
import keno.backrooms_redux.worldgen.chunk.BRChunkGenerators;
import keno.backrooms_redux.worldgen.piece_pools.BRPiecePools;
import keno.backrooms_redux.worldgen.piece_pools.PieceManager;
import keno.backrooms_redux.worldgen.piece_pools.PoolArraysSingleton;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
		registerPieces(PoolArraysSingleton.getInstance());
		BRCommands.init();
		ServerPlayNetworking.registerGlobalReceiver(BRPackets.TELEPORT_PLAYER_TO_BACKROOMS,
				((server, player, handler, buf, responseSender) ->
						server.execute(() -> TeleportPlayer.teleportPlayerToBackrooms(server, player))));
	}

	private void registerPieces(PoolArraysSingleton registry) {
		PieceManager level0Manager = new PieceManager();
		level0Manager.registerPiecesToPool(BRPiecePools.LEVEL_0_COMMON,
				"level_0_common_1", "level_0_common_2",
				"level_0_common_3", "level_0_common_4");
		level0Manager.registerPiecesToPool(BRPiecePools.LEVEL_0_UNCOMMON,
				"level_0_uncommon_1", "level_0_uncommon_2",
				"level_0_uncommon_3", "level_0_uncommon_4");
		level0Manager.registerPiecesToPool(BRPiecePools.LEVEL_0_RARE,
				"level_0_tent", "level_0_chasm");
		registry.addManagerToPool(BRPiecePools.LEVEL_0_MANAGER, level0Manager);
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