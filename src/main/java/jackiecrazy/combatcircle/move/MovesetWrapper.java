package jackiecrazy.combatcircle.move;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovesetWrapper {
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
        activeTimers.forEach((action, time) -> {
            activeTimers.put(action, time + 1);
            //prioritize later gotos
            int tickResult = action.tick(this, performer, target);
            atomicTemp = Math.max(tickResult, atomicTemp);
        });
        activeTimers.entrySet().removeIf((entry) -> {
            if (entry.getKey().isFinished(this, performer, target)) {
                graveyard.add(entry.getKey());
                entry.getKey().stop(this, performer, target, false);
                return true;
            }
            return false;
        });
        if (currentMove.isFinished(this, performer, target)) {
            //natural progression//
            index++;
            currentMove = actions.get(index % actions.size());
            trigger(currentMove, null, performer, target);
        }
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

    public int trigger(Action action, TimerAction parent, Entity performer, Entity target) {
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
