package jackiecrazy.combatcircle.move.action;

import com.google.gson.JsonObject;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import jackiecrazy.footwork.move.Move;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.List;

public abstract class Action extends Move {
    protected Condition condition = TrueCondition.INSTANCE;
    protected transient boolean triggered = false;
    protected String ID = "(default)";

    /**
     * Runs the list of actions, aborting and returning a jump code if the child returns a jump code.
     */
    protected int runActions(@Nullable TimerAction parent, List<Action> actions, Entity performer, Entity target){
        int returnCode = 0;
        for (Action child : actions) {
            if (child.canRun(parent, performer, target)) {
                returnCode = child.perform(parent, performer, target);
                if (returnCode > 0) return returnCode;
            }
        }
        return returnCode;
    }

    /**
     * @return 0 for normal execution. -1 is reserved for expiry of timer actions, and any positive integer is taken to be a jump code.
     */
    public abstract int perform(@Nullable TimerAction parent, Entity performer, Entity target);

    public String serializeToJson() {
        return JsonAdapters.gson.toJson(this);
    }

    public boolean isFinished(Entity performer, Entity target) {
        return triggered;
    }

    public Action readFromJson(JsonObject from) {
        //chat is this real//
        return JsonAdapters.gson.fromJson(from, this.getClass());
    }

    public boolean canRun(TimerAction parent, Entity performer, Entity target) {
        if (triggered) return false;
        return condition.evaluate(parent, performer, target);

    }

    public void start(Entity performer, Entity target) {
        triggered = true;
    }

    public void reset() {
        triggered = false;
    }

    public String toString() {
        return ID;
    }
}
