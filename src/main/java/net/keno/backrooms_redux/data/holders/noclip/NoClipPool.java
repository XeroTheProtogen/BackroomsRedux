package net.keno.backrooms_redux.data.holders.noclip;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.keno.backrooms_redux.data.holders.pos_determ.PosDeterminer;
import net.keno.backrooms_redux.utils.WeightedIdentifierList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import java.util.HashMap;
import java.util.Map;

public record NoClipPool(Identifier poolId, WeightedIdentifierList list, HashMap<Identifier, PosDeterminer> determiners, boolean override) {
    public static Codec<NoClipPool> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Identifier.CODEC.stable().fieldOf("pool_id").forGetter(NoClipPool::poolId),
        WeightedIdentifierList.CODEC.stable().fieldOf("possible_destinations").forGetter(NoClipPool::list),
        Codec.unboundedMap(Identifier.CODEC, PosDeterminer.DETERMINER_CODEC)
                .fieldOf("pos_determiners").forGetter(pool -> Map.copyOf(pool.determiners)),
        Codec.BOOL.stable().optionalFieldOf("override", false).forGetter(NoClipPool::override)
    ).apply(instance, instance.stable((id, weightedList, determinerMap, shouldOverride) -> new NoClipPool(id, weightedList, new HashMap<>(determinerMap), shouldOverride))));

    public Pair<Identifier, PosDeterminer> getRandomId(Random random) {
        Identifier id = list.getRandomIdentifier(random).left().get();
        PosDeterminer determiner = determiners.get(id);
        return new Pair<>(id, determiner);
    }

    public void addIdsAndWeights(Map<Identifier, Float> idsAndWeights) {
        for (Identifier key : idsAndWeights.keySet()) {
            if (list.idsAndWeights().contains(idsAndWeights.get(key))) continue;
            list.addToList(key, idsAndWeights.get(key));
        }
    }
}
