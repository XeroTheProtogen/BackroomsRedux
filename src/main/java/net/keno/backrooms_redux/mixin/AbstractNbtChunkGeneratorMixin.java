package net.keno.backrooms_redux.mixin;

import net.keno.backrooms_redux.worldgen.chunk.DynamicNbtUpdater;
import net.keno.backrooms_redux.worldgen.chunk.UpdatedNbtContainer;
import net.ludocrypt.limlib.api.world.FunctionMap;
import net.ludocrypt.limlib.api.world.Manipulation;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.NbtPlacerUtil;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.ludocrypt.limlib.api.world.chunk.LiminalChunkGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = AbstractNbtChunkGenerator.class, remap = false)
public abstract class AbstractNbtChunkGeneratorMixin extends LiminalChunkGenerator implements UpdatedNbtContainer {
    @Shadow protected abstract void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state, Optional<NbtCompound> blockEntityNbt);

    @Unique
    private NbtGroup backrooms_redux$updatedGroup;
    @Unique
    private FunctionMap<Identifier, NbtPlacerUtil, ResourceManager> backrooms_redux$updatedStructures;


    public AbstractNbtChunkGeneratorMixin(BiomeSource biomeSource) {
        super(biomeSource);
    }

    @Override
    public NbtGroup getGroup() {
        return backrooms_redux$updatedGroup;
    }

    @Override
    public FunctionMap<Identifier, NbtPlacerUtil, ResourceManager> getStructures() {
        return backrooms_redux$updatedStructures;
    }

    @Override
    public void setGroup(NbtGroup nbtGroup) {
        this.backrooms_redux$updatedGroup = nbtGroup;
    }

    @Override
    public void setStructures(FunctionMap<Identifier, NbtPlacerUtil, ResourceManager> structures) {
        this.backrooms_redux$updatedStructures = structures;
    }

    /** We ensure that, if opted into, dynamic nbtgroup info is used instead.
     * This allows us to preserve the normal behavior of generators. */
    @Inject(method = "generateNbt(Lnet/minecraft/world/ChunkRegion;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Identifier;Lnet/ludocrypt/limlib/api/world/Manipulation;)V",
    at = @At("HEAD"), cancellable = true)
    public void generateNbt(ChunkRegion region, BlockPos at, Identifier id, Manipulation manipulation, CallbackInfo ci) {
        if (((AbstractNbtChunkGenerator)(Object)this) instanceof DynamicNbtUpdater) {
            try {
                backrooms_redux$updatedStructures
                        .eval(id, region.getServer().getResourceManager())
                        .manipulate(manipulation)
                        .generateNbt(region, at, (pos, state, nbt) -> this.modifyStructure(region, pos, state, nbt))
                        .spawnEntities(region, at, manipulation);
            } catch (Exception e) {
                e.printStackTrace();
                throw new NullPointerException("Attempted to load undefined structure \'" + id + "\'");
            } finally {
                ci.cancel();
            }
        }
    }

    /** We ensure that, if opted into, dynamic nbtgroup info is used instead.
     * This allows us to preserve the normal behavior of generators. */
    @Inject(method = "generateNbt(Lnet/minecraft/world/ChunkRegion;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Identifier;Lnet/ludocrypt/limlib/api/world/Manipulation;)V",
    at = @At("HEAD"), cancellable = true)
    public void generateNbt(ChunkRegion region, BlockPos offset, BlockPos from, BlockPos to,
                            Identifier id, Manipulation manipulation, CallbackInfo ci) {
        if (((AbstractNbtChunkGenerator)(Object)this) instanceof DynamicNbtUpdater) {
            try {
                backrooms_redux$updatedStructures
                        .eval(id, region.getServer().getResourceManager())
                        .manipulate(manipulation)
                        .generateNbt(region, offset, from, to, (pos, state, nbt) -> this.modifyStructure(region, pos, state, nbt))
                        .spawnEntities(region, offset, from, to, manipulation);
            } catch (Exception e) {
                e.printStackTrace();
                throw new NullPointerException("Attempted to load undefined structure \'" + id + "\'");
            } finally {
                ci.cancel();
            }
        }
    }

    // For safety purposes, we pre-emptively call fill from our dynamic group on initialization
    @Inject(method = "<init>(Lnet/minecraft/world/biome/source/BiomeSource;Lnet/ludocrypt/limlib/api/world/NbtGroup;Lnet/ludocrypt/limlib/api/world/FunctionMap;)V",
    at = @At("TAIL"))
    public void backrooms_redux$init(BiomeSource biomeSource, NbtGroup nbtGroup, FunctionMap<Identifier, NbtPlacerUtil, ResourceManager> structures, CallbackInfo ci) {
        backrooms_redux$updatedGroup = nbtGroup;
        backrooms_redux$updatedStructures = structures;
        backrooms_redux$updatedGroup.fill(backrooms_redux$updatedStructures);
    }
}
