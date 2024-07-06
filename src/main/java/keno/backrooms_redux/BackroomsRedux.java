package keno.backrooms_redux;

import keno.backrooms_redux.item.BRItemGroup;
import keno.backrooms_redux.networking.BRPackets;
import keno.backrooms_redux.networking.TeleportPlayer;
import keno.backrooms_redux.registry.BRCommonRegistry;
import keno.backrooms_redux.registry.BRSoundEvents;
import keno.backrooms_redux.world.biome.BRBiomes;
import keno.backrooms_redux.world.chunk.BRChunkGenerators;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
		ServerPlayNetworking.registerGlobalReceiver(BRPackets.TELEPORT_PLAYER_TO_BACKROOMS,
				((server, player, handler, buf, responseSender) ->
						server.execute(() -> TeleportPlayer.teleportPlayerToBackrooms(server, player))));
	}

	// When retrieving things from redux, use this static method
	public static Identifier modLoc(String location) {
		return new Identifier(ID, location);
	}
}