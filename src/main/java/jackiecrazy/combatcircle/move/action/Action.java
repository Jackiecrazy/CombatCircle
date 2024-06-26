package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.FalseCondition;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import jackiecrazy.footwork.move.Move;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.List;

public abstract class Action extends Move {
    protected Condition condition = TrueCondition.INSTANCE;
    protected transient boolean triggered = false;
    protected Condition repeatable = FalseCondition.INSTANCE;
    protected String ID = "(default)";

    /**
     * Runs the list of actions, aborting and returning a jump code if the child returns a jump code.
     */
    protected int runActions(MovesetWrapper wrapper, @Nullable List<Action> actions, Entity performer, Entity target) {
        int returnCode = 0;
        for (Action child : actions) {
            if (child.canRun(wrapper, performer, target)) {
                if (!child.triggered)
                    child.start(wrapper, performer, target);//kinda an ugly fix tbh
                returnCode = child.perform(wrapper, performer, target);
                if (returnCode > 0) return returnCode;
            }
        }
        return returnCode;
    }

    /**
     * @return 0 for normal execution. -1 is reserved for expiry of timer actions, and any positive integer is taken to be a jump code.
     */
    public abstract int perform(MovesetWrapper wrapper, @Nullable Entity performer, Entity target);

    public String serializeToJson() {
        return JsonAdapters.gson.toJson(this);
    }

    public boolean isFinished(MovesetWrapper wrapper, Entity performer, Entity target) {
        return triggered;
    }

    public boolean canRun(MovesetWrapper wrapper, Entity performer, Entity target) {
        if (triggered && !repeatable.evaluate(wrapper, performer, target)) return false;
        return condition.evaluate(wrapper, performer, target);
    }

    public void start(MovesetWrapper wrapper, Entity performer, Entity target) {
        //fixme instant actions do not run start()
        triggered = true;
    }

    public void reset() {
        triggered = false;
    }

    public String toString() {
        return ID;
    }
}
