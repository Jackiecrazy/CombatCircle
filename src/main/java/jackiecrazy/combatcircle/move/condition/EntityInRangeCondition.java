package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.SelectorArgument;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class EntityInRangeCondition extends Condition{
    private SelectorArgument select;

    @Override
    public Boolean resolve(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        return !select.resolve(wrapper, parent, performer, target).isEmpty();
    }
}
