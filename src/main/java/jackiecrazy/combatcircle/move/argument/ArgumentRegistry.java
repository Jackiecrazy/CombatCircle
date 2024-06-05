package jackiecrazy.combatcircle.move.argument;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.SelectorEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.*;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ArgumentRegistry {
    public static ResourceLocation REGISTRY_NAME = new ResourceLocation(CombatCircle.MODID, "arguments");
    public static Supplier<IForgeRegistry<ArgumentType>> SUPPLIER;
    public static DeferredRegister<ArgumentType> ARGUMENTS = DeferredRegister.create(REGISTRY_NAME, CombatCircle.MODID);

    //entity//
    public static final RegistryObject<ArgumentType> CASTER = ARGUMENTS.register("caster", () -> (a) -> CasterEntityArgument.INSTANCE);
    public static final RegistryObject<ArgumentType> TARGET = ARGUMENTS.register("target", () -> (a) -> TargetEntityArgument.INSTANCE);
    public static final RegistryObject<ArgumentType> ENTITY = ARGUMENTS.register("random_entity", () -> (a) -> JsonAdapters.gson.fromJson(a, SelectorEntityArgument.class));

    //numbers//
    public static final RegistryObject<ArgumentType> NUMBER = ARGUMENTS.register("number", () -> (a) -> JsonAdapters.gson.fromJson(a, FixedNumberArgument.class));

    //vectors//
    public static final RegistryObject<ArgumentType> EYE_HEIGHT = ARGUMENTS.register("eye_height", () -> (a) -> JsonAdapters.gson.fromJson(a, EyeHeightVectorArgument.class));
    public static final RegistryObject<ArgumentType> EYE_POSITION = ARGUMENTS.register("eye_position", () -> (a) -> JsonAdapters.gson.fromJson(a, EyePositionVectorArgument.class));
    public static final RegistryObject<ArgumentType> LOOK = ARGUMENTS.register("look", () -> (a) -> JsonAdapters.gson.fromJson(a, LookVectorArgument.class));
    public static final RegistryObject<ArgumentType> MULTIPLY = ARGUMENTS.register("multiply", () -> (a) -> JsonAdapters.gson.fromJson(a, MultiplyVectorArgument.class));
    public static final RegistryObject<ArgumentType> POSITION = ARGUMENTS.register("position", () -> (a) -> JsonAdapters.gson.fromJson(a, PositionVectorArgument.class));
    public static final RegistryObject<ArgumentType> RAW = ARGUMENTS.register("vector", () -> (a) -> JsonAdapters.gson.fromJson(a, RawVectorArgument.class));
    public static final RegistryObject<ArgumentType> SUM = ARGUMENTS.register("sum", () -> (a) -> JsonAdapters.gson.fromJson(a, SumVectorArgument.class));

    //misc//
    public static final RegistryObject<ArgumentType> DAMAGE = ARGUMENTS.register("damage", () -> (a) -> JsonAdapters.gson.fromJson(a, DamageArgument.class));
    public static final RegistryObject<ArgumentType> EQUIPMENT = ARGUMENTS.register("equipment", () -> (a) -> JsonAdapters.gson.fromJson(a, EquipmentArgument.class));
    public static final RegistryObject<ArgumentType> SELECTOR = ARGUMENTS.register("selector", () -> (a) -> JsonAdapters.gson.fromJson(a, SelectorArgument.class));
}
