package jackiecrazy.combatcircle;

import jackiecrazy.combatcircle.ai.StrafeGoal;
import jackiecrazy.combatcircle.ai.LookMenacingGoal;
import jackiecrazy.combatcircle.utils.CombatManager;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CombatCircle.MODID)
public class CombatCircle {
    public static final String MODID = "combatcircle";

    public static final int SHORT_DISTANCE = 1;
    public static final int CIRCLE_SIZE = 3;
    public static final int MAXIMUM_CHASE_TIME = 100;
    public static final int MOB_LIMIT = 2;//5
    public static final int ATTACK_LIMIT = 2;

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public CombatCircle() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber
    public static class TestEvents {
        @SubscribeEvent
        public static void spread(final EntityJoinWorldEvent e) {
            if (e.getEntity() instanceof CreatureEntity) {
                ((MobEntity) e.getEntity()).goalSelector.addGoal(0, new StrafeGoal((CreatureEntity) e.getEntity()));
                ((MobEntity) e.getEntity()).goalSelector.addGoal(1, new LookMenacingGoal((CreatureEntity) e.getEntity()));
            }
        }

        @SubscribeEvent
        public static void start(FMLServerStartingEvent e) {
            CombatManager.managers.clear();
        }

        @SubscribeEvent
        public static void stop(FMLServerStoppingEvent e) {
            CombatManager.managers.clear();
        }

        @SubscribeEvent
        public static void tick(LivingEvent.LivingUpdateEvent e) {
//            LivingEntity elb = e.getEntityLiving();
//            if (elb instanceof MobEntity && CombatData.getCap(elb).getStaggerTime() == 0 && ((MobEntity) elb).getTarget() != null) {
//                double safeSpace = (elb.getBbWidth()) * 3;
//                for (Entity fan : elb.level.getEntities(elb, elb.getBoundingBox().inflate(safeSpace))) {
//                    if (fan instanceof MonsterEntity && ((MobEntity) fan).getTarget() == ((MobEntity) fan).getTarget() &&
//                            GeneralUtils.getDistSqCompensated(fan, elb) < (safeSpace + 1) * safeSpace && fan != ((MobEntity) elb).getTarget()) {
//                        //mobs "avoid" clumping together
//                        Vector3d diff = elb.position().subtract(fan.position());
//                        double targDistSq = elb.distanceToSqr(((MobEntity) elb).getTarget());
//                        //targDistSq = Math.max(targDistSq, 1);
//                        //fan.addVelocity(diff.x == 0 ? 0 : -0.03 / diff.x, 0, diff.z == 0 ? 0 : -0.03 / diff.z);
//                        elb.push(diff.x == 0 ? 0 : MathHelper.clamp(0.5 / (diff.x * targDistSq), -1, 1), 0, diff.z == 0 ? 0 : MathHelper.clamp(0.5 / (diff.z * targDistSq), -1, 1));
//                        /*
//                        Mobs should move into a position that is close to the player, far from allies, and close to them.
//                         */
//                    }
//                }
//            }
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
