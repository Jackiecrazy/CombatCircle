package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.argument.ArgumentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ConditionRegistry {
    public static ResourceLocation REGISTRY_NAME = new ResourceLocation(CombatCircle.MODID, "conditions");
    public static Supplier<IForgeRegistry<ArgumentType<Boolean>>> SUPPLIER;
    public static DeferredRegister<ArgumentType<Boolean>> CONDITIONS = DeferredRegister.create(REGISTRY_NAME, CombatCircle.MODID);

    //logical//
    public static final RegistryObject<SingletonConditionType> TRUE = CONDITIONS.register("true", () -> new SingletonConditionType(TrueCondition.class, TrueCondition.INSTANCE));
    public static final RegistryObject<SingletonConditionType> FALSE = CONDITIONS.register("false", () -> new SingletonConditionType(FalseCondition.class, FalseCondition.INSTANCE));
    public static final RegistryObject<ConditionType> AND = CONDITIONS.register("and", () -> new ConditionType(AndCondition.class));
    public static final RegistryObject<ConditionType> OR = CONDITIONS.register("or", () -> new ConditionType(OrCondition.class));
    public static final RegistryObject<ConditionType> NOT = CONDITIONS.register("not", () -> new ConditionType(NotCondition.class));
    public static final RegistryObject<ConditionType> COMPARISON = CONDITIONS.register("compare", () -> new ConditionType(ComparisonCondition.class));

    //entity//
    public static final RegistryObject<ConditionType> TIME_WINDOW = CONDITIONS.register("time_window", () -> new ConditionType(TimeWindowCondition.class));
    public static final RegistryObject<ConditionType> IS_TARGET = CONDITIONS.register("is_target", () -> new ConditionType(IsTargetCondition.class));
    public static final RegistryObject<ConditionType> CAN_SEE = CONDITIONS.register("can_see", () -> new ConditionType(CanSeeCondition.class));
    public static final RegistryObject<ConditionType> IS_ALIVE = CONDITIONS.register("is_alive", () -> new ConditionType(IsAliveCondition.class));
    public static final RegistryObject<ConditionType> HAS_EFFECT = CONDITIONS.register("has_effect", () -> new ConditionType(HasEffectCondition.class));
    public static final RegistryObject<ConditionType> IS_NAMED = CONDITIONS.register("is_named", () -> new ConditionType(IsNamedCondition.class));
    public static final RegistryObject<ConditionType> IS_TAGGED = CONDITIONS.register("is_tagged", () -> new ConditionType(IsTaggedWithCondition.class));

    //misc//
    public static final RegistryObject<ConditionType> STACK_EQUALS = CONDITIONS.register("compare_itemstack", () -> new ConditionType(StackEqualsCondition.class));
}
