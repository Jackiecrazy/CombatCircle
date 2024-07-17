package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class GotoAction extends Action {
    private Argument<Double> instruction;

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        return (int) instruction.resolve(wrapper, parent, performer, target).intValue();
    }
}
