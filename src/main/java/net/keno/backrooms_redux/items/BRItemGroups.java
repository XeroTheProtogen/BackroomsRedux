package net.keno.backrooms_redux.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.keno.backrooms_redux.BackroomsRedux;
import net.keno.backrooms_redux.registry.BRCommon;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import java.util.Arrays;

public class BRItemGroups {
    public static final ItemGroup WAVE_1 = addGroup("br_wave_1", new ItemStack(BRCommon.YELLOW_WALLPAPER),
            FabricItemGroup.builder().entries((ctx, entries) -> entries(entries,
                    BRCommon.YELLOW_WALLPAPER, BRCommon.MOIST_CARPET, BRCommon.ROOF_TILE, BRCommon.ROOF_LIGHT)));

    private static ItemGroup addGroup(String name, ItemStack icon, ItemGroup.Builder builder) {
        return Registry.register(Registries.ITEM_GROUP, BackroomsRedux.modLoc(name),
                builder.displayName(Text.translatable("itemGroup." + name))
                        .icon(() -> icon).build());
    }

    private static void entries(ItemGroup.Entries entries, ItemConvertible... items) {
        Arrays.stream(items).distinct().forEach(entries::add);
    }

    public static void init() {

    }
}
