package keno.backrooms_redux.item;

import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.registry.BRCommonRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class BRItemGroup {
    public static final ItemGroup LEVEL_0_ITEMS = registerItemGroup("level_0",
            () -> new ItemStack(BRCommonRegistry.MONOYELLOW_WALLPAPER), FabricItemGroup.builder().entries((displayContext, entries) ->
                            add(entries, BRCommonRegistry.MOIST_CARPET, BRCommonRegistry.MOIST_CARPET_STAIRS,
                                    BRCommonRegistry.ROOF_TILE, BRCommonRegistry.MONOYELLOW_WALLPAPER)));

    public static final RegistryKey<ItemGroup> LEVEL_0_GROUP_KEY = getItemGroupKey(BRItemGroup.LEVEL_0_ITEMS);

    private static ItemGroup registerItemGroup(String name, Supplier<ItemStack> icon, ItemGroup.Builder builder) {
        return Registry.register(Registries.ITEM_GROUP, BackroomsRedux.modLoc(name),
                builder.displayName(Text.translatable("itemGroup." + name)).icon(icon).build());
    }

    private static void add(ItemGroup.Entries entries, ItemConvertible... items) {
        for (ItemConvertible item : items) {
            entries.add(item);
        }
    }

    public static RegistryKey<ItemGroup> getItemGroupKey(ItemGroup group) {
        return Registries.ITEM_GROUP.getKey(group).get();
    }

    public static void registerItemGroups() {
        BackroomsRedux.LOGGER.info("Registering item groups");
    }
}
