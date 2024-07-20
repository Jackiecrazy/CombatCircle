package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class ComparisonCondition extends Condition {
    public enum COMPARISON {
        EQ("=="),
        NEQ("!="),
        LE("<"),
        GE(">"),
        LEQ("<="),
        GEQ(">=");

        private final String value;

        COMPARISON(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value; //will return , or ' instead of COMMA or APOSTROPHE
        }
    }

    public static boolean compare(COMPARISON comparison, double first, double second) {
        return switch (comparison) {
            case EQ -> first == second;
            case NEQ -> first != second;
            case LE -> first < second;
            case GE -> first > second;
            case LEQ -> first <= second;
            case GEQ -> first >= second;
        };
    }

    public static class Number extends ComparisonCondition {
        private Argument<Double> first, second;
        private COMPARISON comparison;

        @Override
        public Boolean resolve(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
            double f = first.resolve(wrapper, parent, performer, target);
            double s = second.resolve(wrapper, parent, performer, target);
            return compare(comparison, f, s);
        }
    }

    public static class ResourceLoc extends ComparisonCondition {
        private Argument<ResourceLocation> first, second;


        @Override
        public Boolean resolve(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
            ResourceLocation f = first.resolve(wrapper, parent, performer, target);
            ResourceLocation s = second.resolve(wrapper, parent, performer, target);
            return s != null && s.equals(f);
        }
    }

    public static class Stack extends Condition {
        private Argument<ItemStack> stack, compare;
        private boolean compare_NBT = true;
        private boolean compare_count = true;
        private COMPARISON count_comparison = COMPARISON.GEQ;

        @Override
        public Boolean resolve(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
            ItemStack stackr = stack.resolve(wrapper, parent, performer, target), comparer = compare.resolve(wrapper, parent, performer, target);
            return stackr.getItem().equals(comparer.getItem())
                    && (!compare_count || compare(count_comparison, stackr.getCount(), comparer.getCount()))
                    && (!compare_NBT || (!comparer.hasTag() || comparer.getTag().equals(stackr.getTag())));
        }
    }
}
