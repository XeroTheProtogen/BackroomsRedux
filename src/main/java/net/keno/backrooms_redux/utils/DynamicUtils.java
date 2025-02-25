package net.keno.backrooms_redux.utils;

import net.keno.backrooms_redux.listeners.HeardData;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.minecraft.util.Identifier;

public class DynamicUtils {
    public static NbtGroup getPiecePoolGroup(Identifier id) {
        return HeardData.getLevelPool(id).convertToGroup();
    }
}
