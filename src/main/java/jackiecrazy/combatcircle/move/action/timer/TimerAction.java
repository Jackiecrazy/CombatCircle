package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;


public abstract class TimerAction extends Action {

    public int getTimer() {
        return timer;
    }

    /**
     * on the base action
     */
    private NumberArgument max_time;
    transient int timer = 0;

    @Override
    public boolean canRun(MovesetWrapper wrapper, TimerAction parent, Entity performer, Entity target) {
        if (triggered && !isFinished(wrapper, performer, target)) return true;
        return super.canRun(wrapper, parent, performer, target);
    }

    /**
     * @return false if the action ends
     */
    public boolean isFinished(MovesetWrapper wrapper, Entity performer, Entity target) {
        return timer > max_time.resolve(performer, target);
    }

    public int tick(MovesetWrapper wrapper, Entity performer, Entity target) {
        timer++;
        return isFinished(wrapper, performer, target) ? -1 : 0;
    }

    public int perform(MovesetWrapper wrapper, @Nullable TimerAction parent, Entity performer, Entity target) {
        if (!triggered) start(wrapper, performer, target);
        return tick(wrapper, performer, target);
    }

    @Override
    public void reset() {
        super.reset();
        timer = 0;
    }
}
