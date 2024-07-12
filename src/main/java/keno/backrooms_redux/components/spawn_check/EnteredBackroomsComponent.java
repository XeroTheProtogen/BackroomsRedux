package keno.backrooms_redux.components.spawn_check;

import keno.backrooms_redux.components.base.BooleanComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class EnteredBackroomsComponent implements BooleanComponent {
    private boolean enteredTheBackrooms = false;
    private PlayerEntity provider;

    public EnteredBackroomsComponent(PlayerEntity provider) {
        this.provider = provider;
    }

    @Override
    public void setBool(boolean value) {
        this.enteredTheBackrooms = value;
    }

    @Override
    public boolean getBool() {
        return this.enteredTheBackrooms;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.enteredTheBackrooms = tag.getBoolean("backrooms_redux.enteredBackrooms");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("backrooms_redux.enteredBackrooms", this.enteredTheBackrooms);
    }
}
