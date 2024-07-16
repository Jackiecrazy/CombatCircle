package jackiecrazy.combatcircle.move.argument;

import com.google.gson.JsonObject;
import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.SelectorEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import jackiecrazy.combatcircle.move.argument.number.*;
import jackiecrazy.combatcircle.move.argument.stack.EquippedItemArgument;
import jackiecrazy.combatcircle.move.argument.stack.RawItemStackArgument;
import jackiecrazy.combatcircle.move.argument.vector.*;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.Random;
import java.util.function.Supplier;

public class ArgumentRegistry {
    public static ResourceLocation REGISTRY_NAME = new ResourceLocation(CombatCircle.MODID, "arguments");
    public static Supplier<IForgeRegistry<ArgumentType>> SUPPLIER;
    public static DeferredRegister<ArgumentType> ARGUMENTS = DeferredRegister.create(REGISTRY_NAME, CombatCircle.MODID);

    //entity//
    public static final RegistryObject<ArgumentType> CASTER = ARGUMENTS.register("caster", () -> (SingletonArgumentType) a -> CasterEntityArgument.INSTANCE);
    public static final RegistryObject<ArgumentType> TARGET = ARGUMENTS.register("target", () -> (SingletonArgumentType) a -> TargetEntityArgument.INSTANCE);
    public static final RegistryObject<ArgumentType> ENTITY = ARGUMENTS.register("random_entity", () -> (a) -> JsonAdapters.gson.fromJson(a, SelectorEntityArgument.class));
    public static final RegistryObject<ArgumentType> GET_ENTITY = ARGUMENTS.register("get_entity", () -> (a) -> JsonAdapters.gson.fromJson(a, EntityArgument.Get.class));

    //numbers//
    public static final RegistryObject<ArgumentType> NUMBER = ARGUMENTS.register("number", () -> (a) -> JsonAdapters.gson.fromJson(a, FixedNumberArgument.class));
    public static final RegistryObject<ArgumentType> CURRENT_HEALTH = ARGUMENTS.register("current_health", () -> (a) -> JsonAdapters.gson.fromJson(a, CurrentHealthArgument.class));
    public static final RegistryObject<ArgumentType> OPERATION = ARGUMENTS.register("operate", () -> (a) -> JsonAdapters.gson.fromJson(a, OperateArgument.class));
    public static final RegistryObject<ArgumentType> ATTRIBUTE_VALUE = ARGUMENTS.register("attribute_value", () -> (a) -> JsonAdapters.gson.fromJson(a, AttributeValueArgument.class));
    public static final RegistryObject<ArgumentType> DOT_PRODUCT = ARGUMENTS.register("dot_product", () -> (a) -> JsonAdapters.gson.fromJson(a, DotProductArgument.class));
    public static final RegistryObject<ArgumentType> DISTANCE = ARGUMENTS.register("distance", () -> (a) -> JsonAdapters.gson.fromJson(a, DistanceArgument.class));
    public static final RegistryObject<ArgumentType> GET_NUMBER = ARGUMENTS.register("get_number", () -> (a) -> JsonAdapters.gson.fromJson(a, NumberArgument.Get.class));
    public static final RegistryObject<ArgumentType> RANDOM = ARGUMENTS.register("random", () -> (a) -> JsonAdapters.gson.fromJson(a, RandomNumberArgument.class));
    public static final RegistryObject<ArgumentType> PARENT_TIMER = ARGUMENTS.register("parent_timer", () -> (SingletonArgumentType) a -> ParentTimerArgument.INSTANCE);

    //vectors//
    public static final RegistryObject<ArgumentType> RAW = ARGUMENTS.register("vector", () -> (a) -> JsonAdapters.gson.fromJson(a, RawVectorArgument.class));
    public static final RegistryObject<ArgumentType> SUM = ARGUMENTS.register("sum_vector", () -> (a) -> JsonAdapters.gson.fromJson(a, SumVectorArgument.class));
    public static final RegistryObject<ArgumentType> ZERO = ARGUMENTS.register("zero", () -> (SingletonArgumentType) a -> RawVectorArgument.ZERO);
    public static final RegistryObject<ArgumentType> EYE_HEIGHT = ARGUMENTS.register("eye_height", () -> (a) -> JsonAdapters.gson.fromJson(a, EyeHeightVectorArgument.class));
    public static final RegistryObject<ArgumentType> EYE_POSITION = ARGUMENTS.register("eye_position", () -> (a) -> JsonAdapters.gson.fromJson(a, EyePositionVectorArgument.class));
    public static final RegistryObject<ArgumentType> LOOK = ARGUMENTS.register("look", () -> (a) -> JsonAdapters.gson.fromJson(a, LookVectorArgument.class));
    public static final RegistryObject<ArgumentType> MULTIPLY = ARGUMENTS.register("multiply", () -> (a) -> JsonAdapters.gson.fromJson(a, MultiplyVectorArgument.class));
    public static final RegistryObject<ArgumentType> CROSS_PRODUCT = ARGUMENTS.register("cross_product", () -> (a) -> JsonAdapters.gson.fromJson(a, CrossProductArgument.class));
    public static final RegistryObject<ArgumentType> POSITION = ARGUMENTS.register("foot_position", () -> (a) -> JsonAdapters.gson.fromJson(a, PositionVectorArgument.class));
    public static final RegistryObject<ArgumentType> GET_VECTOR = ARGUMENTS.register("get_vector", () -> (a) -> JsonAdapters.gson.fromJson(a, VectorArgument.Get.class));
    public static final RegistryObject<ArgumentType> RAY_TRACE = ARGUMENTS.register("ray_trace", () -> (a) -> JsonAdapters.gson.fromJson(a, RayTraceVectorArgument.class));
    public static final RegistryObject<ArgumentType> CLAMP = ARGUMENTS.register("clamp_vector", () -> (a) -> JsonAdapters.gson.fromJson(a, ClampedVectorArgument.class));

    //itemstack//
    public static final RegistryObject<ArgumentType> RAW_ITEM = ARGUMENTS.register("itemstack", () -> (a) -> JsonAdapters.gson.fromJson(a, RawItemStackArgument.class));
    public static final RegistryObject<ArgumentType> EQUIPPED_ITEM = ARGUMENTS.register("equipped_item", () -> (a) -> JsonAdapters.gson.fromJson(a, EquippedItemArgument.class));

    //misc//
    public static final RegistryObject<ArgumentType> DAMAGE = ARGUMENTS.register("damage", () -> (a) -> JsonAdapters.gson.fromJson(a, DamageArgument.class));
    public static final RegistryObject<ArgumentType> SELECTOR = ARGUMENTS.register("selector", () -> (a) -> JsonAdapters.gson.fromJson(a, SelectorArgument.class));
}
