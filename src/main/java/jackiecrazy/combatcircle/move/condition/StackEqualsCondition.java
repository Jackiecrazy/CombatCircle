package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.stack.ItemStackArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class StackEqualsCondition extends Condition {
    private ItemStackArgument stack, compare;
    private ComparisonCondition.COMPARISON count_comparison = ComparisonCondition.COMPARISON.GEQ;

    @Override
    public boolean resolve(MovesetWrapper wrapper, TimerAction parent, @Nullable Entity performer, Entity target) {
        ItemStack stackr = stack.resolve(wrapper, parent, performer, target), comparer = compare.resolve(wrapper, parent, performer, target);
        return stackr.getItem().equals(comparer.getItem())
                && ComparisonCondition.compare(count_comparison, stackr.getCount(), comparer.getCount())
                && (!comparer.hasTag() || comparer.getTag().equals(stackr.getTag()));
    }
}
