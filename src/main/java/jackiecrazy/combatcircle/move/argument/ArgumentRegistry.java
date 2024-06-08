package jackiecrazy.combatcircle.move.argument;

import com.google.gson.JsonObject;
import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.SelectorEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import jackiecrazy.combatcircle.move.argument.number.*;
import jackiecrazy.combatcircle.move.argument.vector.*;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ArgumentRegistry {
    public static ResourceLocation REGISTRY_NAME = new ResourceLocation(CombatCircle.MODID, "arguments");
    public static Supplier<IForgeRegistry<ArgumentType>> SUPPLIER;
    public static DeferredRegister<ArgumentType> ARGUMENTS = DeferredRegister.create(REGISTRY_NAME, CombatCircle.MODID);

    //entity//
    public static final RegistryObject<ArgumentType> CASTER = ARGUMENTS.register("caster", () -> (SingletonArgumentType) a -> CasterEntityArgument.INSTANCE);
    public static final RegistryObject<ArgumentType> TARGET = ARGUMENTS.register("target", () -> (SingletonArgumentType) a -> TargetEntityArgument.INSTANCE);
    public static final RegistryObject<ArgumentType> ENTITY = ARGUMENTS.register("random_entity", () -> (a) -> JsonAdapters.gson.fromJson(a, SelectorEntityArgument.class));

    //numbers//
    public static final RegistryObject<ArgumentType> NUMBER = ARGUMENTS.register("number", () -> (a) -> JsonAdapters.gson.fromJson(a, FixedNumberArgument.class));
    public static final RegistryObject<ArgumentType> CURRENT_HEALTH = ARGUMENTS.register("current_health", () -> (a) -> JsonAdapters.gson.fromJson(a, CurrentHealthArgument.class));
    public static final RegistryObject<ArgumentType> OPERATION = ARGUMENTS.register("operate", () -> (a) -> JsonAdapters.gson.fromJson(a, OperateArgument.class));
    public static final RegistryObject<ArgumentType> ATTRIBUTE_VALUE = ARGUMENTS.register("attribute_value", () -> (a) -> JsonAdapters.gson.fromJson(a, AttributeValueArgument.class));
    public static final RegistryObject<ArgumentType> DOT_PRODUCT = ARGUMENTS.register("dot_product", () -> (a) -> JsonAdapters.gson.fromJson(a, DotProductArgument.class));
    public static final RegistryObject<ArgumentType> DISTANCE = ARGUMENTS.register("distance", () -> (a) -> JsonAdapters.gson.fromJson(a, DistanceArgument.class));

    //vectors//
    public static final RegistryObject<ArgumentType> RAW = ARGUMENTS.register("vector", () -> (a) -> JsonAdapters.gson.fromJson(a, RawVectorArgument.class));
    public static final RegistryObject<ArgumentType> SUM = ARGUMENTS.register("vector_sum", () -> (a) -> JsonAdapters.gson.fromJson(a, SumVectorArgument.class));
    public static final RegistryObject<ArgumentType> ZERO = ARGUMENTS.register("zero", () -> (SingletonArgumentType) a -> RawVectorArgument.ZERO);
    public static final RegistryObject<ArgumentType> EYE_HEIGHT = ARGUMENTS.register("eye_height", () -> (a) -> JsonAdapters.gson.fromJson(a, EyeHeightVectorArgument.class));
    public static final RegistryObject<ArgumentType> EYE_POSITION = ARGUMENTS.register("eye_position", () -> (a) -> JsonAdapters.gson.fromJson(a, EyePositionVectorArgument.class));
    public static final RegistryObject<ArgumentType> LOOK = ARGUMENTS.register("look", () -> (a) -> JsonAdapters.gson.fromJson(a, LookVectorArgument.class));
    public static final RegistryObject<ArgumentType> MULTIPLY = ARGUMENTS.register("multiply", () -> (a) -> JsonAdapters.gson.fromJson(a, MultiplyVectorArgument.class));
    public static final RegistryObject<ArgumentType> CROSS_PRODUCT = ARGUMENTS.register("cross_product", () -> (a) -> JsonAdapters.gson.fromJson(a, CrossProductArgument.class));
    public static final RegistryObject<ArgumentType> POSITION = ARGUMENTS.register("foot_position", () -> (a) -> JsonAdapters.gson.fromJson(a, PositionVectorArgument.class));

    //misc//
    public static final RegistryObject<ArgumentType> DAMAGE = ARGUMENTS.register("damage", () -> (a) -> JsonAdapters.gson.fromJson(a, DamageArgument.class));
    public static final RegistryObject<ArgumentType> EQUIPMENT = ARGUMENTS.register("equipment", () -> (a) -> JsonAdapters.gson.fromJson(a, EquipmentArgument.class));
    public static final RegistryObject<ArgumentType> SELECTOR = ARGUMENTS.register("selector", () -> (a) -> JsonAdapters.gson.fromJson(a, SelectorArgument.class));
}
