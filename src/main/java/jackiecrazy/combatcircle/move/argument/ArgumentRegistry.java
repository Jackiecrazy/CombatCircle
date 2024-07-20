package jackiecrazy.combatcircle.move.argument;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.SelectorEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.TargetEntityArgument;
import jackiecrazy.combatcircle.move.argument.number.*;
import jackiecrazy.combatcircle.move.argument.resourcelocation.RegistryNameArgument;
import jackiecrazy.combatcircle.move.argument.resourcelocation.ResourceLocationArgument;
import jackiecrazy.combatcircle.move.argument.stack.EquippedItemArgument;
import jackiecrazy.combatcircle.move.argument.stack.RawItemStackArgument;
import jackiecrazy.combatcircle.move.argument.vector.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class ArgumentRegistry {
    public static ResourceLocation REGISTRY_NAME = new ResourceLocation(CombatCircle.MODID, "arguments");
    public static Supplier<IForgeRegistry<ArgumentType<?>>> SUPPLIER;
    public static DeferredRegister<ArgumentType<?>> ARGUMENTS = DeferredRegister.create(REGISTRY_NAME, CombatCircle.MODID);

    //entity//
    public static final RegistryObject<ArgumentType<Entity>> CASTER = ARGUMENTS.register("caster", () -> new SingletonArgumentType<>(CasterEntityArgument.class, CasterEntityArgument.INSTANCE));
    public static final RegistryObject<ArgumentType<Entity>> TARGET = ARGUMENTS.register("target", () -> new SingletonArgumentType<>(TargetEntityArgument.class, TargetEntityArgument.INSTANCE));
    public static final RegistryObject<ArgumentType<Entity>> ENTITY = ARGUMENTS.register("random_entity", () -> new ArgumentType<>(SelectorEntityArgument.class));
    public static final RegistryObject<ArgumentType<Entity>> GET_ENTITY = ARGUMENTS.register("get_entity", () -> new ArgumentType<>(EntityArgument.Get.class));

    //numbers//
    public static final RegistryObject<ArgumentType<Double>> NUMBER = ARGUMENTS.register("number", () -> new ArgumentType<>(FixedNumberArgument.class));
    public static final RegistryObject<ArgumentType<Double>> CURRENT_HEALTH = ARGUMENTS.register("current_health", () -> new ArgumentType<>(CurrentHealthArgument.class));
    public static final RegistryObject<ArgumentType<Double>> HEALTH_PERCENTAGE = ARGUMENTS.register("health_percentage", ()  -> new ArgumentType<>(HealthPercArgument.class));
    public static final RegistryObject<ArgumentType<Double>> OPERATION = ARGUMENTS.register("operate", ()  -> new ArgumentType<>(OperateArgument.class));
    public static final RegistryObject<ArgumentType<Double>> ATTRIBUTE_VALUE = ARGUMENTS.register("attribute_value", ()  -> new ArgumentType<>(AttributeValueArgument.class));
    public static final RegistryObject<ArgumentType<Double>> DOT_PRODUCT = ARGUMENTS.register("dot_product", ()  -> new ArgumentType<>(DotProductArgument.class));
    public static final RegistryObject<ArgumentType<Double>> DISTANCE = ARGUMENTS.register("distance", ()  -> new ArgumentType<>(DistanceArgument.class));
    public static final RegistryObject<ArgumentType<Double>> GET_NUMBER = ARGUMENTS.register("get_number", ()  -> new ArgumentType<>(NumberArgument.Get.class));
    public static final RegistryObject<ArgumentType<Double>> RANDOM = ARGUMENTS.register("random", ()  -> new ArgumentType<>(RandomNumberArgument.class));
    public static final RegistryObject<ArgumentType<Double>> PARENT_TIMER = ARGUMENTS.register("parent_timer", () -> new SingletonArgumentType<>(ParentTimerArgument.class, ParentTimerArgument.INSTANCE));

    //vectors//
    public static final RegistryObject<ArgumentType<Vec3>> RAW_VEC = ARGUMENTS.register("vector", ()  -> new ArgumentType<>(RawVectorArgument.class));
    public static final RegistryObject<ArgumentType<Vec3>> SUM = ARGUMENTS.register("sum_vector", ()  -> new ArgumentType<>(SumVectorArgument.class));
    public static final RegistryObject<ArgumentType<Vec3>> ZERO = ARGUMENTS.register("zero", ()  -> new SingletonArgumentType<>(RawVectorArgument.class, RawVectorArgument.ZERO));
    public static final RegistryObject<ArgumentType<Vec3>> EYE_HEIGHT = ARGUMENTS.register("eye_height", ()  -> new ArgumentType<>(EyeHeightVectorArgument.class));
    public static final RegistryObject<ArgumentType<Vec3>> EYE_POSITION = ARGUMENTS.register("eye_position", ()  -> new ArgumentType<>(EyePositionVectorArgument.class));
    public static final RegistryObject<ArgumentType<Vec3>> LOOK = ARGUMENTS.register("look", ()  -> new ArgumentType<>(LookVectorArgument.class));
    public static final RegistryObject<ArgumentType<Vec3>> MULTIPLY = ARGUMENTS.register("multiply", ()  -> new ArgumentType<>(MultiplyVectorArgument.class));
    public static final RegistryObject<ArgumentType<Vec3>> CROSS_PRODUCT = ARGUMENTS.register("cross_product", ()  -> new ArgumentType<>(CrossProductArgument.class));
    public static final RegistryObject<ArgumentType<Vec3>> POSITION = ARGUMENTS.register("foot_position", ()  -> new ArgumentType<>(PositionVectorArgument.class));
    public static final RegistryObject<ArgumentType<Vec3>> GET_VECTOR = ARGUMENTS.register("get_vector", ()  -> new ArgumentType<>(VectorArgument.Get.class));
    public static final RegistryObject<ArgumentType<Vec3>> RAY_TRACE = ARGUMENTS.register("ray_trace", ()  -> new ArgumentType<>(RayTraceVectorArgument.class));
    public static final RegistryObject<ArgumentType<Vec3>> CLAMP = ARGUMENTS.register("clamp_vector", ()  -> new ArgumentType<>(ClampedVectorArgument.class));

    //resource location//
    public static final RegistryObject<ArgumentType<ResourceLocation>> RAW_RLOC = ARGUMENTS.register("resource_location", ()  -> new ArgumentType<>(ResourceLocationArgument.Raw.class));
    public static final RegistryObject<ArgumentType<ResourceLocation>> GET_RLOC = ARGUMENTS.register("get_resource_location", ()  -> new ArgumentType<>(ResourceLocationArgument.Get.class));
    public static final RegistryObject<ArgumentType<ResourceLocation>> REGISTRY = ARGUMENTS.register("registry_name", ()  -> new ArgumentType<>(RegistryNameArgument.class));

    //itemstack//
    public static final RegistryObject<ArgumentType<ItemStack>> RAW_ITEM = ARGUMENTS.register("itemstack", ()  -> new ArgumentType<>(RawItemStackArgument.class));
    public static final RegistryObject<ArgumentType<ItemStack>> EQUIPPED_ITEM = ARGUMENTS.register("equipped_item", ()  -> new ArgumentType<>(EquippedItemArgument.class));

    //misc//
    public static final RegistryObject<ArgumentType<DamageSource>> DAMAGE = ARGUMENTS.register("damage", ()  -> new ArgumentType<>(DamageArgument.class));
    public static final RegistryObject<ArgumentType<List<Entity>>> SELECTOR = ARGUMENTS.register("selector", ()  -> new ArgumentType<>(SelectorArgument.class));
    public static final RegistryObject<ArgumentType<?>> PARENT = ARGUMENTS.register("parent_data", ()  -> new SingletonArgumentType<>(ParentDataArgument.class, ParentDataArgument.INSTANCE));
}
