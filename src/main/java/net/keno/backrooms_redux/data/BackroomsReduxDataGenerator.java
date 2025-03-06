package net.keno.backrooms_redux.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BackroomsReduxDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(BRModelProvider::new);
		pack.addProvider(BRBlockTagProvider::new);
		pack.addProvider(BRLangGen::new);
		pack.addProvider(BRLootTableGenerator::new);
	}
}
