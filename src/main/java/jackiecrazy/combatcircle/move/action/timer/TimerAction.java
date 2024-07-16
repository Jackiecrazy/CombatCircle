package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;


public abstract class TimerAction extends Action {
    /**
     * on the base action
     */
    private NumberArgument max_time;

    @Override
    public boolean canRun(MovesetWrapper wrapper, TimerAction parent, Entity performer, Entity target) {
        if (!isFinished(wrapper, performer, target) && wrapper.getTimer(this) >= 0)//isn't done, but has already started
            return true;
        return super.canRun(wrapper, parent, performer, target);
    }

    /**
     * @return false if the action is still running
     */
    public boolean isFinished(MovesetWrapper wrapper, Entity performer, Entity target) {
        return wrapper.getTimer(this) > max_time.resolve(wrapper, this, performer, target);
    }

    public int tick(MovesetWrapper wrapper, Entity performer, Entity target) {
        return isFinished(wrapper, performer, target) ? -1 : 0;
    }

    public void start(MovesetWrapper wrapper, Entity performer, Entity target) {
    }

    public int perform(MovesetWrapper wrapper, TimerAction parent, @Nullable Entity performer, Entity target) {
        return tick(wrapper, performer, target);
    }
}
