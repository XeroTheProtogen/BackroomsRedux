package keno.backrooms_redux.datagen;

import keno.backrooms_redux.registry.BRCommonRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class BRRecipeGen extends FabricRecipeProvider {
    public BRRecipeGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        offerStairsRecipe(exporter, BRCommonRegistry.MOIST_CARPET_STAIRS, BRCommonRegistry.MOIST_CARPET);
    }

    private void offerStairsRecipe(RecipeExporter exporter, @NotNull ItemConvertible output, @NotNull ItemConvertible input) {
        createStairsRecipe(output, Ingredient.ofItems(input)).criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, new Identifier(getRecipeName(input)));
    }
}
