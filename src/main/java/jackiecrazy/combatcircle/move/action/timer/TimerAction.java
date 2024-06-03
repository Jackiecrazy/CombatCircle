package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.action.Action;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;


public abstract class TimerAction extends Action {
    private List<Action> children;

    public int getTimer() {
        return timer;
    }

    /**
     * on the base action
     */
    private int max_time;
    transient int timer = 0;

    /**
     * @return false if the action ends
     */
    public boolean tick(LivingEntity performer, LivingEntity target) {
        for (Action child : children) {
            if (child.canRun(this, performer, target))
                child.perform(performer, target);
        }
        return timer++ > max_time;
    }

    public void perform(LivingEntity performer, LivingEntity target){

    }

    @Override
    public void reset() {
        super.reset();
        for (Action child : children)
            child.reset();
    }
}
