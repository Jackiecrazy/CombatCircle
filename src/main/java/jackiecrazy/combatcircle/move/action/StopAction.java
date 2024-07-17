package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class StopAction extends Action {
    boolean recursive=true;
    @Override
    public int perform(MovesetWrapper wrapper, TimerAction parent, @Nullable Entity performer, Entity target) {
        parent.stop(wrapper, performer, target, recursive);
        return -1;
    }
}
