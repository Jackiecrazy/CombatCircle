package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.action.timer.AddVelocityAction;
import jackiecrazy.combatcircle.move.action.timer.ProjectHitboxAction;
import jackiecrazy.combatcircle.move.action.timer.WaitAction;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ActionRegistry {
    public static ResourceLocation REGISTRY_NAME = new ResourceLocation(CombatCircle.MODID, "actions");
    public static Supplier<IForgeRegistry<ActionType>> SUPPLIER;
    public static DeferredRegister<ActionType> ACTIONS = DeferredRegister.create(REGISTRY_NAME, CombatCircle.MODID);

    //Timer Actions//
    public static final RegistryObject<ActionType> WAIT = ACTIONS.register("wait", () -> (a) -> JsonAdapters.gson.fromJson(a, WaitAction.class));
    public static final RegistryObject<ActionType> ADD_VELOCITY = ACTIONS.register("add_velocity", () -> (a) -> JsonAdapters.gson.fromJson(a, AddVelocityAction.class));
    public static final RegistryObject<ActionType> PROJECT_HITBOX = ACTIONS.register("project_hitbox", () -> (a) -> JsonAdapters.gson.fromJson(a, ProjectHitboxAction.class));

    //Simple Actions//
    public static final RegistryObject<ActionType> DEBUG = ACTIONS.register("debug", () -> (a) -> JsonAdapters.gson.fromJson(a, DebugAction.class));
    public static final RegistryObject<ActionType> DEAL_DAMAGE = ACTIONS.register("deal_damage", () -> (a) -> JsonAdapters.gson.fromJson(a, DealDamageAction.class));
    public static final RegistryObject<ActionType> ADD_EFFECT = ACTIONS.register("add_effect", () -> (a) -> JsonAdapters.gson.fromJson(a, AddEffectAction.class));
    public static final RegistryObject<ActionType> EXPLODE = ACTIONS.register("explode", () -> (a) -> JsonAdapters.gson.fromJson(a, ExplodeAction.class));
    public static final RegistryObject<ActionType> LOOK_AT = ACTIONS.register("look_at", () -> (a) -> JsonAdapters.gson.fromJson(a, LookAtAction.class));
    public static final RegistryObject<ActionType> SET_AGGRESSIVE = ACTIONS.register("set_aggressive", () -> (a) -> JsonAdapters.gson.fromJson(a, SetAggressiveAction.class));
}
