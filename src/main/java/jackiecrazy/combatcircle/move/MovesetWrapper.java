package jackiecrazy.combatcircle.move;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class MovesetWrapper {
    public final Stack<DataWrapper<?>> stack = new Stack<>();
    private final List<Tuple<TimerAction, Integer>> activeTimers = new ArrayList<>();
    private final HashMap<Action, Object> extraData = new HashMap<>();
    private final List<Action> graveyard = new ArrayList<>();
    private final List<TimerAction> actions;
    private final Condition canRun;
    private final int power;
    private final int changePer;
    private TimerAction currentMove;
    private int index = 0;
    private int currentWeight;
    //TODO block actions (get/set/blockstate compare/velocity block collision),
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

    public List<Tuple<TimerAction, Integer>> getActiveTimers() {
        return activeTimers;
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
        currentMove.canRun(this, null, performer, target);
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
        int jumpCode = 0;
        for (Tuple<TimerAction, Integer> tuple : activeTimers) {
            tuple.setB(tuple.getB() + 1);
            int tickResult = tuple.getA().tick(this, performer, target);
            if (tickResult > 0) {
                jumpCode = tickResult;
                break;
            }
        }
        if (currentMove.isFinished(this, performer, target)) {
            //natural progression//
            index++;
            currentMove = actions.get(index % actions.size());
            currentMove.canRun(this, null, performer, target);
            trigger(currentMove, null, performer, target);
        }
        //clearing happens after natural progression to prevent clears breaking timers
        activeTimers.removeIf((entry) -> {
            if (entry.getA().isFinished(this, performer, target)) {
                graveyard.add(entry.getA());
                entry.getA().stop(this, performer, target, false);
                return true;
            }
            return false;
        });
        if (jumpCode > 0) {
            jumpTo(jumpCode,performer, target);
        }
    }

    public void jumpTo(int jumpCode, Entity performer, Entity target){
        //goto, reset everything//
        reset();
        index = jumpCode - 1;
        currentMove = actions.get(index % actions.size());
        currentMove.canRun(this, null, performer, target);
        trigger(currentMove, null, performer, target);
    }

    public int getPower() {
        return power;
    }

    public int trigger(Action action, Action parent, Entity performer, Entity target) {
        //if action and not in graveyard, execute. If not repeatable, put in graveyard.
        //if timer action and not in graveyard, if not active, place and start, then if not repeatable, put in graveyard.
        if (graveyard.contains(action)) return 0;
        if (action instanceof TimerAction ta) {
            if (activeTimers.stream().noneMatch(a -> a.getA() == ta)) {
                activeTimers.add(new Tuple<>(ta, 0));
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
        return activeTimers.stream().filter(a -> a.getA() == action).findFirst().map(Tuple::getB).orElse(-1);
    }

    public void immediatelyExpire(TimerAction action) {
        activeTimers.stream().filter(a -> a.getA() == action).findFirst().ifPresent(a -> a.setB(99999));
    }

    public <T> T getData(Action a) {
        return (T) (extraData.get(a));
    }

    public void setData(Action a, Object b) {
        extraData.put(a, b);
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
