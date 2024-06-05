package jackiecrazy.combatcircle.move.filter;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.action.DebugAction;
import jackiecrazy.combatcircle.move.action.timer.AddVelocityAction;
import jackiecrazy.combatcircle.move.action.timer.WaitAction;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FilterRegistry {
    public static ResourceLocation REGISTRY_NAME = new ResourceLocation(CombatCircle.MODID, "filters");
    public static Supplier<IForgeRegistry<FilterType>> SUPPLIER;
    public static DeferredRegister<FilterType> FILTERS = DeferredRegister.create(REGISTRY_NAME, CombatCircle.MODID);

    public static final RegistryObject<FilterType> NONE = FILTERS.register("none", () -> (a) -> NoFilter.INSTANCE);
    public static final RegistryObject<FilterType> RANDOM = FILTERS.register("random", () -> (a) -> JsonAdapters.gson.fromJson(a, RandomFilter.class));
    public static final RegistryObject<FilterType> CONDITION = FILTERS.register("condition", () -> (a) -> JsonAdapters.gson.fromJson(a, ConditionFilter.class));
}
