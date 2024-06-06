package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;


public abstract class TimerAction extends Action {

    public int getTimer() {
        return timer;
    }

    /**
     * on the base action TODO privatize
     */
    public NumberArgument max_time;
    transient int timer = 0;

    @Override
    public boolean canRun(TimerAction parent, Entity performer, Entity target) {
        if (triggered && !isFinished(performer, target)) return true;
        return super.canRun(parent, performer, target);
    }

    /**
     * @return false if the action ends
     */
    public boolean isFinished(Entity performer, Entity target) {
        return timer > max_time.resolve(performer, target);
    }

    public int tick(Entity performer, Entity target) {
        timer++;
        return isFinished(performer, target) ? -1 : 0;
    }

    public int perform(@Nullable TimerAction parent, Entity performer, Entity target) {
        if (!triggered) start(performer, target);
        return tick(performer, target);
    }

    @Override
    public void reset() {
        super.reset();
        timer = 0;
    }
}
