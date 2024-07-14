package jackiecrazy.combatcircle.move;

import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class MovesetWrapper {
    private List<TimerAction> actions;
    private TimerAction currentMove;//todo does this need to be a list of currently executing actions?
    private Condition canRun;
    private int index = 0;
    private int power;
    private int changePer, currentWeight;
    //TODO triggers, some actions record parameters, block action,
    // global cooldown, commonly used action components (how parameter?),
    // deduplicate all movesets, put moveset execution responsibility into capability?
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

    public void start() {
        currentMove = actions.get(0);
    }

    public void reset() {
        actions.forEach(TimerAction::reset);
        index = 0;
        currentMove = null;
    }

    public boolean canRun(Entity performer, Entity target) {
        return canRun.evaluate(this, performer, target);
    }

    public void tick(Entity performer, Entity target) {
        int ret = currentMove.perform(this, performer, target);
        if (ret < 0) {
            //natural progression//
            index++;
            currentMove = actions.get(index % actions.size());
        }
        if (ret > 0) {
            //jump, reset everything//
            actions.forEach(TimerAction::reset);
            index = ret - 1;
            currentMove = actions.get(index % actions.size());
        }
    }

    public int getPower() {
        return power;
    }
}
