package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class ParentTimerArgument extends NumberArgument{
public static final ParentTimerArgument INSTANCE=new ParentTimerArgument();
    @Override
    public Double resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        if(!(parent instanceof TimerAction ta))return 0.0;
        return (double) wrapper.getTimer(ta);
    }
}
