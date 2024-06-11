package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class ParentTimerArgument extends NumberArgument{
public static final ParentTimerArgument INSTANCE=new ParentTimerArgument();
    @Override
    public double resolve(MovesetWrapper wrapper, Entity caster, Entity target) {
        return wrapper.getCurrentMove().getTimer();
    }
}
