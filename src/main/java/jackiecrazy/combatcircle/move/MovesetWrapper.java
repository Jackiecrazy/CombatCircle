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
    private TimerAction currentMove;
    private final Condition canRun;
    private int index = 0;
    private final int power;
    private final int changePer;
    private int currentWeight;
    public static class DataWrapper<T>{
        public T getInstance() {
            return instance;
        }

        public void setInstance(T instance) {
            this.instance = instance;
        }
        private T instance;
        public DataWrapper(T item){
            instance=item;
        }
    }
    //TODO triggers, some actions record parameters, block action,
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
        activeTimers.forEach((action, time) -> {
            activeTimers.put(action, time + 1);
        });
        int ret = currentMove.perform(this, currentMove, performer, target);
        activeTimers.entrySet().removeIf((entry)-> {
            if(entry.getKey().isFinished(this, performer, target)){
                graveyard.add(entry.getKey());
                return true;
            }
            return false;
        });
        if (ret < 0) {
            //natural progression//
            index++;
            currentMove = actions.get(index % actions.size());
            trigger(currentMove, null, performer, target);
        }
        if (ret > 0) {
            //jump, reset everything//
            reset();
            index = ret - 1;
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
            }
        }
        else if (!action.repeatable(this, parent, performer, target)) graveyard.add(action);
        return action.perform(this, parent, performer, target);
    }

    public int getTimer(TimerAction action) {
        return activeTimers.getOrDefault(action, -1);
    }

    public <T> T getData(Action a){
        return (T) (extraData.get(a).instance);
    }

    public void setData(Action a, Object b){
        extraData.put(a, new DataWrapper<>(b));
    }
}
