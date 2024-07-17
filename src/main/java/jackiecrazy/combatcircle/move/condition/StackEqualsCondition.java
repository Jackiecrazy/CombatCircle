package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class StackEqualsCondition extends Condition {
    private Argument<ItemStack> stack, compare;
    private boolean compare_NBT = true;
    private boolean compare_count = true;
    private ComparisonCondition.COMPARISON count_comparison = ComparisonCondition.COMPARISON.GEQ;

    @Override
    public Boolean resolve(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        ItemStack stackr = stack.resolve(wrapper, parent, performer, target), comparer = compare.resolve(wrapper, parent, performer, target);
        return stackr.getItem().equals(comparer.getItem())
                && (!compare_count || ComparisonCondition.compare(count_comparison, stackr.getCount(), comparer.getCount()))
                && (!compare_NBT || (!comparer.hasTag() || comparer.getTag().equals(stackr.getTag())));
    }
}
