package jackiecrazy.combatcircle.conditions;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Map;
import java.util.function.Supplier;

public abstract class Condition extends ForgeRegistryEntry<Condition> {
    public abstract boolean resolve();
}