package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class DebugAction extends Action{
    @Override
    public int perform(@Nullable TimerAction parent, Entity performer, Entity target) {
        System.out.println("here are the performer and target:");
        System.out.println(performer);
        System.out.println(target);
        return 0;
    }
}
