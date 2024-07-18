package jackiecrazy.combatcircle.move;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import jackiecrazy.footwork.event.ConsumePostureEvent;
import jackiecrazy.footwork.event.StunEvent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class MovesetWrapper {
    public final Stack<DataWrapper<?>> stack = new Stack<>();
    private final HashMap<TimerAction, Integer> activeTimers = new HashMap<>();
    private final HashMap<Action, DataWrapper<?>> extraData = new HashMap<>();
    private final List<Action> graveyard = new ArrayList<>();
    private final List<TimerAction> actions;
    private final Condition canRun;
    private final int power;
    private final int changePer;
    int atomicTemp;
    private TimerAction currentMove;
    private int index = 0;
    private int currentWeight;

    //TODO triggers, some actions record parameters, block actions (get/set/blockstate compare/velocity block collision),
    // global cooldown, commonly used action components (how parameter?),
    // put moveset execution responsibility into capability?
    // terrain sensitivity for wolf pack, encircle/attack multiple targets with merge/split group mechanics?
    public MovesetWrapper(int power, int initialWeight, int weightChange, List<TimerAction> actions, Condition toRun) {
        this.actions = actions;
        canRun = toRun;
        currentWeight = initialWeight;
        changePer = weightChange;
        this.power = power;
    }

    public MovesetWrapper(List<TimerAction> actions) {
        this(0, 0, 0, actions, TrueCondition.INSTANCE);
    }

    public List<Action> getGraveyard() {
        return graveyard;
    }

    public TimerAction getCurrentMove() {
        return currentMove;
    }

    public int getChangePer() {
        return changePer;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void changeCurrentWeight(int by) {
        currentWeight += Math.abs(by);
    }

    public boolean executing() {
        return index < actions.size() && index >= 0;
    }

    public void start(Entity performer, Entity target) {
        currentMove = actions.get(0);
        trigger(currentMove, null, performer, target);
    }

    public void reset() {
        index = 0;
        extraData.clear();
        activeTimers.clear();
        graveyard.clear();
    }

    public boolean canRun(Entity performer, Entity target) {
        return canRun.resolve(this, null, performer, target);
    }

    public void tick(Entity performer, Entity target) {
        atomicTemp = 0;
        activeTimers.forEach((timer, integer) -> {
            //tuple.setB(tuple.getB()+1);
            activeTimers.put(timer, integer+1);
            //prioritize later gotos
            //fixme can no longer interrupt by jumping because hashmap has undefined ordering what do
            int tickResult = timer.tick(this, performer, target);
            atomicTemp = Math.max(tickResult, atomicTemp);
        });
        if (currentMove.isFinished(this, performer, target)) {
            //natural progression//
            index++;
            currentMove = actions.get(index % actions.size());
            trigger(currentMove, null, performer, target);
        }
        //clearing happens after natural progression to prevent clears breaking timers
        activeTimers.entrySet().removeIf((entry) -> {
            if (entry.getKey().isFinished(this, performer, target)) {
                graveyard.add(entry.getKey());
                entry.getKey().stop(this, performer, target, false);
                return true;
            }
            return false;
        });
        if (atomicTemp > 0) {
            //goto, reset everything//
            reset();
            index = atomicTemp - 1;
            currentMove = actions.get(index % actions.size());
            trigger(currentMove, null, performer, target);
        }
    }

    public int getPower() {
        return power;
    }

    public int trigger(Action action, Action parent, Entity performer, Entity target) {
        //if action and not in graveyard, execute. If not repeatable, put in graveyard.
        //if timer action and not in graveyard, if not active, place and start, then if not repeatable, put in graveyard.
        if (graveyard.contains(action)) return 0;
        if (action instanceof TimerAction ta) {
            if (!activeTimers.containsKey(ta)) {
                activeTimers.put(ta, 0);
                ta.start(this, performer, target);
                ta.tick(this, performer, target);
            }
            return 0;
        }
        if (!action.repeatable(this, parent, performer, target))
            graveyard.add(action);//continuous tasks are handled by active timers
        return action.perform(this, parent, performer, target);
    }

    public int getTimer(TimerAction action) {
        return activeTimers.getOrDefault(action, -1);
    }

    public void immediatelyExpire(TimerAction action) {
        activeTimers.put(action, 99999);
    }

    public <T> T getData(Action a) {
        return (T) (extraData.get(a).instance);
    }

    public void setData(Action a, Object b) {
        extraData.put(a, new DataWrapper<>(b));
    }

    public void devilTrigger(Entity performer, Event e) {
        //fixme can't jump from trigger
        if (e instanceof LivingAttackEvent lae) {
            final Entity vec = lae.getSource().getDirectEntity();
            if (vec != null)
                lae.getEntity().getPersistentData().putInt("damageSourceDirectEntity", vec.getId());
            lae.getEntity().getPersistentData().putDouble("damageAmount", lae.getAmount());
            String test = "combatcircle:trigger_on_hit";
            for (TimerAction ta : activeTimers.keySet()) {
                ta.getTriggers().forEach(a -> {
                    if (a.toString().equals(test) && a.canRun(this, ta, performer, lae.getSource().getEntity())) {
                        setData(a, lae.getSource());
                        a.perform(this, a, performer, lae.getSource().getEntity());
                    }
                });
            }
            if (lae.getEntity().getPersistentData().getDouble("damageAmount") <= 0) {
                lae.setCanceled(true);
            }
        }
        else if (e instanceof LivingHurtEvent lae) {
            final Entity vec = lae.getSource().getDirectEntity();
            if (vec != null)
                lae.getEntity().getPersistentData().putInt("damageSourceDirectEntity", vec.getId());
            lae.getEntity().getPersistentData().putDouble("damageAmount", lae.getAmount());
            String test = "combatcircle:trigger_on_hurt";
            for (TimerAction ta : activeTimers.keySet()) {
                ta.getTriggers().forEach(a -> {
                    if (a.toString().equals(test) && a.canRun(this, ta, performer, lae.getSource().getEntity())) {
                        setData(a, lae.getSource());
                        a.perform(this, a, performer, lae.getSource().getEntity());
                    }
                });
            }
            lae.setAmount((float) lae.getEntity().getPersistentData().getDouble("damageAmount"));
            if (lae.getAmount() <= 0) {
                lae.setCanceled(true);
            }
        }
        else if (e instanceof LivingDeathEvent lae) {
            final Entity vec = lae.getSource().getDirectEntity();
            if (vec != null)
                lae.getEntity().getPersistentData().putInt("damageSourceDirectEntity", vec.getId());
            String test = "combatcircle:trigger_on_death";
            for (TimerAction ta : activeTimers.keySet()) {
                ta.getTriggers().forEach(a -> {
                    if (a.toString().equals(test) && a.canRun(this, ta, performer, lae.getSource().getEntity())) {
                        setData(a, lae.getSource());
                        a.perform(this, a, performer, lae.getSource().getEntity());
                    }
                });
            }
            if ( lae.getEntity().getPersistentData().getDouble("deathDenied") != 0) {
                lae.setCanceled(true);
            }
        }
        else if (e instanceof ConsumePostureEvent lae) {
            lae.getEntity().getPersistentData().putDouble("damageAmount", lae.getAmount());
            String test = "combatcircle:trigger_on_posture_damage";
            for (TimerAction ta : activeTimers.keySet()) {
                ta.getTriggers().forEach(a -> {
                    if (a.toString().equals(test) && a.canRun(this, ta, performer, lae.getAttacker())) {
                        a.perform(this, ta, performer, lae.getAttacker());
                    }
                });
            }
            lae.setAmount((float) lae.getEntity().getPersistentData().getDouble("damageAmount"));
            if (lae.getAmount() <= 0) {
                lae.setCanceled(true);
            }
        }
        else if (e instanceof StunEvent lae) {
            lae.getEntity().getPersistentData().putDouble("stunTime", lae.getLength());
            lae.getEntity().getPersistentData().putDouble("knockdown", lae.isKnockdown() ? 1 : 0);
            String test = "combatcircle:trigger_on_stunned";
            for (TimerAction ta : activeTimers.keySet()) {
                ta.getTriggers().forEach(a -> {
                    if (a.toString().equals(test) && a.canRun(this, ta, performer, lae.getAttacker())) {
                        a.perform(this, ta, performer, lae.getAttacker());
                    }
                });
            }
            lae.setKnockdown(lae.getEntity().getPersistentData().getDouble("knockdown") != 0);
            lae.setLength((int) lae.getEntity().getPersistentData().getDouble("stunTime"));
            if (lae.getLength() <= 0) {
                lae.setCanceled(true);
            }
        }
        else if (e instanceof MobEffectEvent.Applicable lae) {
            lae.getEntity().getPersistentData().putDouble("effectLength", lae.getEffectInstance().getDuration());
            lae.getEntity().getPersistentData().putDouble("effectPotency", lae.getEffectInstance().getAmplifier());
            lae.getEntity().getPersistentData().putDouble("effectApplied", lae.getResult() == Event.Result.DENY ? -1 : lae.getResult() == Event.Result.ALLOW ? 1 : 0);
            String test = "combatcircle:trigger_on_effect_applicable";
            for (TimerAction ta : activeTimers.keySet()) {
                ta.getTriggers().forEach(a -> {
                    if (a.toString().equals(test) && a.canRun(this, ta, performer, null)) {
                        a.perform(this, ta, performer, null);
                    }
                });
            }
            switch ((int) lae.getEntity().getPersistentData().getDouble("effectApplied")){
                case 1->lae.setResult(Event.Result.ALLOW);
                case -1->lae.setResult(Event.Result.DENY);
                default->lae.setResult(Event.Result.DEFAULT);
            }
        }
        else if (e instanceof MobEffectEvent.Added lae) {
            lae.getEntity().getPersistentData().putDouble("effectLength", lae.getEffectInstance().getDuration());
            lae.getEntity().getPersistentData().putDouble("effectPotency", lae.getEffectInstance().getAmplifier());
            String test = "combatcircle:trigger_on_effect_applied";
            for (TimerAction ta : activeTimers.keySet()) {
                ta.getTriggers().forEach(a -> {
                    if (a.toString().equals(test) && a.canRun(this, ta, performer, lae.getEffectSource())) {
                        a.perform(this, ta, performer, lae.getEffectSource());
                    }
                });
            }
        }
    }

    public static class DataWrapper<T> {
        private T instance;

        public DataWrapper(T item) {
            instance = item;
        }

        public T getInstance() {
            return instance;
        }

        public void setInstance(T instance) {
            this.instance = instance;
        }
    }
}
