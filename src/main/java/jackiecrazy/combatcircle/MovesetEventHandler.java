package jackiecrazy.combatcircle;

import jackiecrazy.combatcircle.capability.MovesetData;
import jackiecrazy.combatcircle.move.MovesetManager;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.action.trigger.Trigger;
import jackiecrazy.footwork.event.ConsumePostureEvent;
import jackiecrazy.footwork.event.StunEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CombatCircle.MODID)
public class MovesetEventHandler {
    @SubscribeEvent
    public static void attacked(LivingAttackEvent lae) {
        //grab everything, find and feed them to the currently active moveset wrapper
        if (lae.getEntity().level().isClientSide() || lae.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return;
        MovesetManager movesetManager = MovesetData.getCap(lae.getEntity()).getMovesetManager();
        MovesetWrapper mw = movesetManager.getCurrentMove();
        Entity performer = lae.getEntity();
        if (mw != null) {
            final Entity vec = lae.getSource().getDirectEntity();
            if (vec != null)
                lae.getEntity().getPersistentData().putInt("damageSourceDirectEntity", vec.getId());
            lae.getEntity().getPersistentData().putDouble("damageAmount", lae.getAmount());
            String test = "combatcircle:trigger_on_hit";
            for (Tuple<TimerAction, Integer> tup : mw.getActiveTimers()) {
                TimerAction ta = tup.getA();
                for (Trigger a : ta.getTriggers()) {
                    if (a.toString().equals(test) && a.canRun(mw, ta, performer, lae.getSource().getEntity())) {
                        mw.setData(a, lae.getSource());
                        int ret = a.perform(mw, a, performer, lae.getSource().getEntity());
                        if (ret > 0) {
                            mw.jumpTo(ret, lae.getEntity(), vec);
                        }
                    }
                }
            }
            if (lae.getEntity().getPersistentData().getDouble("damageAmount") <= 0) {
                lae.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void die(LivingDeathEvent lae) {
        //grab everything, find and feed them to the currently active moveset wrapper
        if (lae.getEntity().level().isClientSide() || lae.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return;
        MovesetWrapper mw = MovesetData.getCap(lae.getEntity()).getMovesetManager().getCurrentMove();
        Entity performer = lae.getEntity();
        if (mw != null) {
            final Entity vec = lae.getSource().getDirectEntity();
            if (vec != null)
                lae.getEntity().getPersistentData().putInt("damageSourceDirectEntity", vec.getId());
            String test = "combatcircle:trigger_on_death";
            for (Tuple<TimerAction, Integer> tup : mw.getActiveTimers()) {
                TimerAction ta = tup.getA();
                for (Trigger a : ta.getTriggers()) {
                    if (a.toString().equals(test) && a.canRun(mw, ta, performer, lae.getSource().getEntity())) {
                        mw.setData(a, lae.getSource());
                        int ret = a.perform(mw, a, performer, lae.getSource().getEntity());
                        if (ret > 0) {
                            mw.jumpTo(ret, lae.getEntity(), vec);
                        }
                    }
                }
            }
            if (lae.getEntity().getPersistentData().getDouble("deathDenied") != 0) {
                lae.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void effectAdded(MobEffectEvent.Added lae) {
        //grab everything, find and feed them to the currently active moveset wrapper
        if (lae.getEntity().level().isClientSide()) return;
        MovesetWrapper mw = MovesetData.getCap(lae.getEntity()).getMovesetManager().getCurrentMove();
        Entity performer = lae.getEntity();
        if (mw != null) {
            lae.getEntity().getPersistentData().putDouble("effectLength", lae.getEffectInstance().getDuration());
            lae.getEntity().getPersistentData().putDouble("effectPotency", lae.getEffectInstance().getAmplifier());
            String test = "combatcircle:trigger_on_effect_applied";
            for (Tuple<TimerAction, Integer> tup : mw.getActiveTimers()) {
                TimerAction ta = tup.getA();
                for (Trigger a : ta.getTriggers()) {
                    if (a.toString().equals(test) && a.canRun(mw, ta, performer, lae.getEffectSource())) {
                        int ret = a.perform(mw, a, performer, lae.getEffectSource());
                        if (ret > 0) {
                            mw.jumpTo(ret, lae.getEntity(), lae.getEffectSource());
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void effectApplicable(MobEffectEvent.Applicable lae) {
        //grab everything, find and feed them to the currently active moveset wrapper
        if (lae.getEntity().level().isClientSide()) return;
        MovesetWrapper mw = MovesetData.getCap(lae.getEntity()).getMovesetManager().getCurrentMove();
        if (mw != null) {
            lae.getEntity().getPersistentData().putDouble("effectLength", lae.getEffectInstance().getDuration());
            lae.getEntity().getPersistentData().putDouble("effectPotency", lae.getEffectInstance().getAmplifier());
            lae.getEntity().getPersistentData().putDouble("effectApplied", lae.getResult() == Event.Result.DENY ? -1 : lae.getResult() == Event.Result.ALLOW ? 1 : 0);
            String test = "combatcircle:trigger_on_effect_applicable";
            for (Tuple<TimerAction, Integer> tup : mw.getActiveTimers()) {
                TimerAction ta = tup.getA();
                for (Trigger a : ta.getTriggers()) {
                    if (a.toString().equals(test) && a.canRun(mw, ta, lae.getEntity(), null)) {
                        mw.setData(a, lae.getEffectInstance());
                        int ret = a.perform(mw, a, lae.getEntity(), null);
                        if (ret > 0) {
                            mw.jumpTo(ret, lae.getEntity(), null);
                        }
                    }
                }
            }
            switch ((int) lae.getEntity().getPersistentData().getDouble("effectApplied")) {
                case 1 -> lae.setResult(Event.Result.ALLOW);
                case -1 -> lae.setResult(Event.Result.DENY);
                default -> lae.setResult(Event.Result.DEFAULT);
            }
        }
    }

    @SubscribeEvent
    public static void postured(ConsumePostureEvent lae) {
        //grab everything, find and feed them to the currently active moveset wrapper
        if (lae.getEntity().level().isClientSide()) return;
        MovesetWrapper mw = MovesetData.getCap(lae.getEntity()).getMovesetManager().getCurrentMove();
        Entity performer = lae.getEntity();
        if (mw != null) {
            lae.getEntity().getPersistentData().putDouble("damageAmount", lae.getAmount());
            String test = "combatcircle:trigger_on_posture_damage";
            for (Tuple<TimerAction, Integer> tup : mw.getActiveTimers()) {
                TimerAction ta = tup.getA();
                for (Trigger a : ta.getTriggers()) {
                    if (a.toString().equals(test) && a.canRun(mw, ta, performer, lae.getAttacker())) {
                        int ret = a.perform(mw, a, performer, lae.getAttacker());
                        if (ret > 0) {
                            mw.jumpTo(ret, lae.getEntity(), lae.getAttacker());
                        }
                    }
                }
            }
            lae.setAmount((float) lae.getEntity().getPersistentData().getDouble("damageAmount"));
            if (lae.getAmount() <= 0) {
                lae.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void stunned(StunEvent lae) {
        //grab everything, find and feed them to the currently active moveset wrapper
        if (lae.getEntity().level().isClientSide()) return;
        MovesetWrapper mw = MovesetData.getCap(lae.getEntity()).getMovesetManager().getCurrentMove();
        Entity performer = lae.getEntity();
        if (mw != null) {
            lae.getEntity().getPersistentData().putDouble("stunTime", lae.getLength());
            lae.getEntity().getPersistentData().putDouble("knockdown", lae.isKnockdown() ? 1 : 0);
            String test = "combatcircle:trigger_on_stunned";
            for (Tuple<TimerAction, Integer> tup : mw.getActiveTimers()) {
                TimerAction ta = tup.getA();
                for (Trigger a : ta.getTriggers()) {
                    if (a.toString().equals(test) && a.canRun(mw, ta, performer, lae.getAttacker())) {
                        int ret = a.perform(mw, a, performer, lae.getAttacker());
                        if (ret > 0) {
                            mw.jumpTo(ret, lae.getEntity(), lae.getAttacker());
                        }
                    }
                }
            }
            lae.setKnockdown(lae.getEntity().getPersistentData().getDouble("knockdown") != 0);
            lae.setLength((int) lae.getEntity().getPersistentData().getDouble("stunTime"));
            if (lae.getLength() <= 0) {
                lae.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void takeDamage(LivingHurtEvent lae) {
        //grab everything, find and feed them to the currently active moveset wrapper
        if (lae.getEntity().level().isClientSide() || lae.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return;
        MovesetWrapper mw = MovesetData.getCap(lae.getEntity()).getMovesetManager().getCurrentMove();
        Entity performer = lae.getEntity();
        if (mw != null) {
            final Entity vec = lae.getSource().getDirectEntity();
            if (vec != null)
                lae.getEntity().getPersistentData().putInt("damageSourceDirectEntity", vec.getId());
            lae.getEntity().getPersistentData().putDouble("damageAmount", lae.getAmount());
            String test = "combatcircle:trigger_on_hurt";
            for (Tuple<TimerAction, Integer> tup : mw.getActiveTimers()) {
                TimerAction ta = tup.getA();
                for (Trigger a : ta.getTriggers()) {
                    if (a.toString().equals(test) && a.canRun(mw, ta, performer, lae.getSource().getEntity())) {
                        mw.setData(a, lae.getSource());
                        int ret = a.perform(mw, a, performer, lae.getSource().getEntity());
                        if (ret > 0) {
                            mw.jumpTo(ret, lae.getEntity(), vec);
                        }
                    }
                }
            }
            lae.setAmount((float) lae.getEntity().getPersistentData().getDouble("damageAmount"));
            if (lae.getAmount() <= 0) {
                lae.setCanceled(true);
            }
        }
    }
}
