package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.trigger.Trigger;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.List;


public abstract class TimerAction extends Action {
    public List<Trigger> getTriggers() {
        return triggers;
    }

    private List<Trigger> triggers;
    /**
     * on the base action
     */
    private Argument<Double> max_time;

    @Override
    public boolean canRun(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
        if (!isFinished(wrapper, performer, target) && wrapper.getTimer(this) >= 0)//isn't done, but has already started
            return true;
        return super.canRun(wrapper, parent, performer, target);
    }

    /**
     * @return false if the action is still running
     */
    public boolean isFinished(MovesetWrapper wrapper, Entity performer, Entity target) {
        return wrapper.getTimer(this) > max_time.resolve(wrapper, this, performer, target) || wrapper.getTimer(this) < 0;
    }

    public int tick(MovesetWrapper wrapper, Entity performer, Entity target) {
        return isFinished(wrapper, performer, target) ? -1 : 0;
    }

    public void start(MovesetWrapper wrapper, Entity performer, Entity target) {
    }

    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        return tick(wrapper, performer, target);
    }

    public void stop(MovesetWrapper wrapper, Entity performer, Entity target, boolean recursive) {
        wrapper.immediatelyExpire(this);
    }


}
