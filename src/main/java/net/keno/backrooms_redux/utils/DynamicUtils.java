package net.keno.backrooms_redux.utils;

import net.keno.backrooms_redux.data.holders.noclip.NoClipPool;
import net.keno.backrooms_redux.data.listeners.HeardData;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.minecraft.util.Identifier;

public class DynamicUtils {
    public static NoClipPool getNoclipPool(Identifier id) {
        return HeardData.getNoclipPool(id);
    }

    public static NbtGroup getPiecePoolGroup(Identifier id) {
        return HeardData.getLevelPool(id).convertToGroup();
    }
}
