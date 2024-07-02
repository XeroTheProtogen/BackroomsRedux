package keno.backrooms_redux;

import keno.backrooms_redux.datagen.BRLangGen;
import keno.backrooms_redux.datagen.BRModelGen;
import keno.backrooms_redux.datagen.BRRecipeGen;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BackroomsReduxDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(BRModelGen::new);
		pack.addProvider(BRLangGen::new);
		pack.addProvider(BRRecipeGen::new);
	}
}
