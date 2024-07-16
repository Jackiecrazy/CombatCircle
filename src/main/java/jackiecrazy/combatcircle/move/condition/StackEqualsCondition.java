package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.stack.ItemStackArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class StackEqualsCondition extends Condition {
    private ItemStackArgument stack, compare;
    private ComparisonCondition.COMPARISON count_comparison = ComparisonCondition.COMPARISON.GEQ;

    @Override
    public boolean resolve(MovesetWrapper wrapper, @Nullable Entity performer, Entity target) {
        ItemStack stackr = stack.resolve(wrapper, performer, target), comparer = compare.resolve(wrapper, performer, target);
        return stackr.getItem().equals(comparer.getItem())
                && ComparisonCondition.compare(count_comparison, stackr.getCount(), comparer.getCount())
                && (!comparer.hasTag() || comparer.getTag().equals(stackr.getTag()));
    }
}
