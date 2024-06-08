package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class JumpToAction extends Action {
    private NumberArgument instruction;

    @Override
    public int perform(@Nullable TimerAction parent, Entity performer, Entity target) {
        return (int) instruction.resolve(performer, target);
    }
}
