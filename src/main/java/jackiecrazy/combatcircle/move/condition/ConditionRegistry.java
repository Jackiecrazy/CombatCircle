package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ConditionRegistry {
    public static ResourceLocation REGISTRY_NAME = new ResourceLocation(CombatCircle.MODID, "conditions");
    public static Supplier<IForgeRegistry<ConditionType>> SUPPLIER;
    public static DeferredRegister<ConditionType> CONDITIONS = DeferredRegister.create(REGISTRY_NAME, CombatCircle.MODID);

    //logical//
    public static final RegistryObject<ConditionType> TRUE = CONDITIONS.register("true", () -> (a) -> JsonAdapters.gson.fromJson(a, TrueCondition.class));
    public static final RegistryObject<ConditionType> FALSE = CONDITIONS.register("false", () -> (a) -> JsonAdapters.gson.fromJson(a, FalseCondition.class));
    public static final RegistryObject<ConditionType> AND = CONDITIONS.register("and", () -> (a) -> JsonAdapters.gson.fromJson(a, AndCondition.class));
    public static final RegistryObject<ConditionType> OR = CONDITIONS.register("or", () -> (a) -> JsonAdapters.gson.fromJson(a, OrCondition.class));
    public static final RegistryObject<ConditionType> NOT = CONDITIONS.register("or", () -> (a) -> JsonAdapters.gson.fromJson(a, NotCondition.class));

    //entity//
    public static final RegistryObject<ConditionType> START_AT = CONDITIONS.register("start_at", () -> (a) -> JsonAdapters.gson.fromJson(a, StartAtCondition.class));
    public static final RegistryObject<ConditionType> IS_TARGET = CONDITIONS.register("is_target", () -> (a) -> JsonAdapters.gson.fromJson(a, IsTargetCondition.class));
    public static final RegistryObject<ConditionType> CAN_SEE = CONDITIONS.register("can_see", () -> (a) -> JsonAdapters.gson.fromJson(a, CanSeeCondition.class));
}
