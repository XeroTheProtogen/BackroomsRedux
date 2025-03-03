package net.keno.backrooms_redux;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.keno.backrooms_redux.listeners.LevelPoolListener;
import net.keno.backrooms_redux.registry.BRCommon;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
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

		BRCommon.init();
	}

	public static Identifier modLoc(String loc) {
		return Identifier.of(MOD_ID, loc);
	}
}