package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.action.timer.AddVelocityAction;
import jackiecrazy.combatcircle.move.action.timer.MoveToAction;
import jackiecrazy.combatcircle.move.action.timer.ProjectHitboxAction;
import jackiecrazy.combatcircle.move.action.timer.WaitAction;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
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
    public static final RegistryObject<ActionType> MOVE_TO = ACTIONS.register("move_to", () -> (a) -> JsonAdapters.gson.fromJson(a, MoveToAction.class));
    public static final RegistryObject<ActionType> PROJECT_HITBOX = ACTIONS.register("project_hitbox", () -> (a) -> JsonAdapters.gson.fromJson(a, ProjectHitboxAction.class));

    //Simple Actions//
    public static final RegistryObject<ActionType> STORE_NUMBER = ACTIONS.register("store_number", () -> (a) -> JsonAdapters.gson.fromJson(a, NumberArgument.Store.class));
    public static final RegistryObject<ActionType> STORE_VECTOR = ACTIONS.register("store_vector", () -> (a) -> JsonAdapters.gson.fromJson(a, VectorArgument.Store.class));
    public static final RegistryObject<ActionType> STORE_ENTITY = ACTIONS.register("store_entity", () -> (a) -> JsonAdapters.gson.fromJson(a, EntityArgument.Store.class));

    public static final RegistryObject<ActionType> DEBUG = ACTIONS.register("debug", () -> (a) -> JsonAdapters.gson.fromJson(a, DebugAction.class));
    public static final RegistryObject<ActionType> DEAL_DAMAGE = ACTIONS.register("deal_damage", () -> (a) -> JsonAdapters.gson.fromJson(a, DealDamageAction.class));
    public static final RegistryObject<ActionType> ADD_EFFECT = ACTIONS.register("add_effect", () -> (a) -> JsonAdapters.gson.fromJson(a, AddEffectAction.class));
    public static final RegistryObject<ActionType> EXPLODE = ACTIONS.register("explode", () -> (a) -> JsonAdapters.gson.fromJson(a, ExplodeAction.class));
    public static final RegistryObject<ActionType> LOOK_AT = ACTIONS.register("look_at", () -> (a) -> JsonAdapters.gson.fromJson(a, LookAtAction.class));
    public static final RegistryObject<ActionType> SET_AGGRESSIVE = ACTIONS.register("set_aggressive", () -> (a) -> JsonAdapters.gson.fromJson(a, SetAggressiveAction.class));
    public static final RegistryObject<ActionType> ATTACH_ACTION = ACTIONS.register("attach_action", () -> (a) -> JsonAdapters.gson.fromJson(a, AttachActionAction.class));
    public static final RegistryObject<ActionType> SPAWN_ENTITY = ACTIONS.register("spawn_entity", () -> (a) -> JsonAdapters.gson.fromJson(a, SpawnEntityAction.class));
    public static final RegistryObject<ActionType> JUMP_TO = ACTIONS.register("jump_to", () -> (a) -> JsonAdapters.gson.fromJson(a, JumpToAction.class));
    public static final RegistryObject<ActionType> ADD_ATTRIBUTE = ACTIONS.register("add_attribute", () -> (a) -> JsonAdapters.gson.fromJson(a, AddAttributeModifierAction.class));
    public static final RegistryObject<ActionType> PLAY_PARTICLE = ACTIONS.register("play_particle", () -> (a) -> JsonAdapters.gson.fromJson(a, PlayParticleAction.class));
    public static final RegistryObject<ActionType> PLAY_SOUND = ACTIONS.register("play_sound", () -> (a) -> JsonAdapters.gson.fromJson(a, PlaySoundAction.class));
    public static final RegistryObject<ActionType> TELEPORT = ACTIONS.register("teleport", () -> (a) -> JsonAdapters.gson.fromJson(a, TeleportAction.class));
}
