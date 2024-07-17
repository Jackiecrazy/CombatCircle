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
    protected Condition repeatable = FalseCondition.INSTANCE;
    protected String ID = "(default)";

    /**
     * Runs the list of actions, aborting and returning a jump code if the child returns a jump code.
     */
    protected int runActions(MovesetWrapper wrapper, TimerAction parent, @Nullable List<Action> actions, Entity performer, Entity target) {
        if (actions == null) return 0;
        int returnCode = 0;
        for (Action child : actions) {
            if (child.canRun(wrapper, parent, performer, target)) {
                returnCode = wrapper.trigger(child, parent, performer, target);
                if (returnCode > 0) return returnCode;
            }
        }
        return returnCode;
    }

    /**
     * @return 0 for normal execution. -1 is reserved for expiry of timer actions, and any positive integer is taken to be a jump code.
     */
    public abstract int perform(MovesetWrapper wrapper, TimerAction parent, @Nullable Entity performer, Entity target);

    public String serializeToJson() {
        return JsonAdapters.gson.toJson(this);
    }

    public boolean canRun(MovesetWrapper wrapper, TimerAction parent, Entity performer, Entity target) {
        if (wrapper.getGraveyard().contains(this)) return false;
        return condition.resolve(wrapper, parent, performer, target);
    }

    public boolean repeatable(MovesetWrapper wrapper, TimerAction parent, Entity performer, Entity target) {
        return repeatable.resolve(wrapper, parent, performer, target);
    }


    public void stop(MovesetWrapper wrapper, Entity performer, Entity target, boolean recursive) {
    }

    public String toString() {
        return ID;
    }
}
