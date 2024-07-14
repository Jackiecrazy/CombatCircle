package jackiecrazy.combatcircle;

import jackiecrazy.combatcircle.ai.*;
import jackiecrazy.combatcircle.move.*;
import jackiecrazy.combatcircle.move.action.ActionRegistry;
import jackiecrazy.combatcircle.move.argument.ArgumentRegistry;
import jackiecrazy.combatcircle.capability.Moveset;
import jackiecrazy.combatcircle.capability.MovesetData;
import jackiecrazy.combatcircle.move.condition.ConditionRegistry;
import jackiecrazy.combatcircle.move.filter.FilterRegistry;
import jackiecrazy.combatcircle.utils.CombatManager;
import jackiecrazy.footwork.api.FootworkAttributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CombatCircle.MODID)
public class CombatCircle {
    public static final String MODID = "combatcircle";
    public static final Random rand = new Random();

    public static final int SHORT_DISTANCE = 1;
    public static final int SPREAD_DISTANCE = 7;
    public static final int CIRCLE_SIZE = 4;
    public static final int MAXIMUM_CHASE_TIME = 100;
    public static final int MOB_LIMIT = 10;//5
    public static final int ATTACK_LIMIT = 3;
    public static final TagKey<EntityType<?>> IGNORE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MODID, "ignore_combat_circle"));

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public CombatCircle() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ArgumentRegistry.SUPPLIER = ArgumentRegistry.ARGUMENTS.makeRegistry(RegistryBuilder::new);
        ArgumentRegistry.ARGUMENTS.register(bus);
        ConditionRegistry.SUPPLIER = ConditionRegistry.CONDITIONS.makeRegistry(RegistryBuilder::new);
        ConditionRegistry.CONDITIONS.register(bus);
        ActionRegistry.SUPPLIER = ActionRegistry.ACTIONS.makeRegistry(RegistryBuilder::new);
        ActionRegistry.ACTIONS.register(bus);
        FilterRegistry.SUPPLIER = FilterRegistry.FILTERS.makeRegistry(RegistryBuilder::new);
        FilterRegistry.FILTERS.register(bus);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public void onJsonListener(AddReloadListenerEvent event) {
        Moves.register(event);
        Movesets.register(event);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber
    public static class TestEvents {
        @SubscribeEvent
        public static void caps(AttachCapabilitiesEvent<Entity> e) {
            if (e.getObject() instanceof LivingEntity lb) {
                e.addCapability(new ResourceLocation("combatcircle:delayed_actions"), new MovesetData(new Moveset(lb)));
            }
        }

        @SubscribeEvent
        public static void spread(final EntityJoinLevelEvent e) {
            if (e.getEntity() instanceof PathfinderMob mob && !mob.getType().is(IGNORE)) {
                if (Movesets.moves.containsKey(mob.getType())) {
                    mob.getAttribute(FootworkAttributes.ENCIRCLEMENT_DISTANCE.get()).setBaseValue(Movesets.moves.get(mob.getType()).encirclement_distance);
                } else if (mob instanceof RangedAttackMob)
                    mob.getAttribute(FootworkAttributes.ENCIRCLEMENT_DISTANCE.get()).setBaseValue(CombatCircle.CIRCLE_SIZE + 3);
                else
                    mob.getAttribute(FootworkAttributes.ENCIRCLEMENT_DISTANCE.get()).setBaseValue(CombatCircle.CIRCLE_SIZE);
                mob.goalSelector.addGoal(0, new WolfPackGoal((PathfinderMob) e.getEntity()));//theoretically as long as it continues to wolfpack it won't attack
                mob.goalSelector.addGoal(1, new LookMenacingGoal((PathfinderMob) e.getEntity()));
                EntityInfo sets = Movesets.moves.get(mob.getType());
                if (sets != null) {
                    for (WrappedGoal wg : new HashSet<>(mob.goalSelector.getAvailableGoals())) {
                        for (Class<Goal> goal : sets.to_wipe)
                            if (goal.isAssignableFrom(wg.getGoal().getClass()))
                                mob.goalSelector.removeGoal(wg.getGoal());
                    }
                    for (WrappedGoal wg : new HashSet<>(mob.targetSelector.getAvailableGoals())) {
                        for (Class<Goal> goal : sets.to_wipe)
                            if (goal.isAssignableFrom(wg.getGoal().getClass()))
                                mob.targetSelector.removeGoal(wg.getGoal());
                    }
                    mob.targetSelector.addGoal(0, new MovesetGoal(mob, new MovesetManager(mob, sets.moveset)));
                } else mob.targetSelector.addGoal(0, new RequestSlotGoal(mob));

                /*
                several types of AI can be made:
                pathfinders weighting the same path less and less, or assigning random angles in pathing
                    - would likely rely on using mixins to tweak path generators
                highly structured circles like those in the image above that effectively surround a standing target
                    - outer circle is unformed and conglomerates around the inner circle to crowd escape options and give illusion of horde combat
                    - inner circle is filled on an opportunistic basis if the circle breaks due to the player moving too far, all inner circle slots are reclaimed
                    - manager lists all the mobs that have requested an attack, then picks the closest to a point who hasn't recently attacked and instructs that mob to fill in that spot (pathing around the player if needed)
                    - manager randomly fires signals at mobs in the inner circle to attack
                    - after attack, manager removes privileges from mob and mob is ordered to retreat
                loose spreading that has decent behavior in both pursuit and encirclement
                    - jank af
                separation+alignment+cohesion of a boid flock to model swarming behavior
                    - entities path away from other entities
                    - entities move in the same direction as the average of those nearby
                    - entities path towards the average position of those nearby
                the simple two-rule encirclement of real life wolf packs
                    - get as close to the target as it is safe
                    - get away from other members of the pack

                regardless of flanking behavior, the flank and look AI are interrupted after the mob successfully becomes an attacker,
                  then its collison restrictions are removed and its slot vacated for other horde members, enforcing the lack of single-target combos
                    eventually a mob with very high weight (such as a commander mob) may be able to be weighted heavily enough to strike very often
                after the mob has or is attacked, it is removed from the attacker list, at which point a retreat goal should push it back into the horde
                extended collision should only take place during the flank phase, a mob that is attacking or retreating should not be treated as having extended collision

                boid notes: follow, avoid, conform
                vector:
                have several vectors that average each other out as enemies get closer to the player.
                Potential vector ideas: move towards, move sideways, move away
                Choose the best axis for movement based on several vector lengths projected around mob.
                Reduce vector against terrain, other mobs (or not)
                Move away from nearest enemy at an angle to prevent endless shoving

                targeting:
                bit masking to determine valid targets
                revenge list (don't include itself)
                retargeting revenge based on total damage dealt, damage dealt to it, and player weighting
                 */
            }
        }

        @SubscribeEvent
        public static void start(ServerStartingEvent e) {
            CombatManager.managers.clear();
        }

        @SubscribeEvent
        public static void stop(ServerStoppingEvent e) {
            CombatManager.managers.clear();
        }

        @SubscribeEvent
        public static void tick(LivingEvent.LivingTickEvent e) {
            MovesetData.getCap(e.getEntity()).update();
        }

        @SubscribeEvent
        public static void tick(TickEvent.ServerTickEvent e) {
            CombatManager.managers.forEach((a, b) -> {
                if (a.isDeadOrDying()) CombatManager.managers.remove(a);
                b.tick();
            });
        }
    }
}
