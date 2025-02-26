package net.keno.backrooms_redux.utils;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

import java.util.List;

public record WeightedIdentifierList(List<Pair<Identifier, Float>> idsAndWeights) {
    public static final Codec<WeightedIdentifierList> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.pair(Identifier.CODEC, Codec.FLOAT).listOf().fieldOf("ids_and_weights")
                            .forGetter(WeightedIdentifierList::idsAndWeights)
            ).apply(instance, instance.stable(WeightedIdentifierList::new)));

    public WeightedIdentifierList {
        for (Pair<Identifier, Float> pair : idsAndWeights) {
            float weight = pair.getSecond();
            if (weight < 0f || weight > 1f) {
                throw new RuntimeException("Invalid weight; must be within the range [0:1]");
            }
        }
    }

    public float calculateTotalWeight() {
        float weight = 0;
        for (Pair<Identifier, Float> pair : idsAndWeights) {
            weight += pair.getSecond() * 10;
        }
        return weight;
    }

    public Either<Identifier, Boolean> getRandomIdentifier(Random random) {
        float weight = calculateTotalWeight();
        float rolledWeight = MathHelper.nextFloat(random, 0.1f, weight);
        weight = 0f;
        for (Pair<Identifier, Float> pair : idsAndWeights) {
            weight += pair.getSecond() * 10;

            if (weight >= rolledWeight) {
                return Either.left(pair.getFirst());
            }
        }
        return Either.right(false);
    }
}
