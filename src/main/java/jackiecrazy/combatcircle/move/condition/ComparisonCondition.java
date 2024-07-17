package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;

public class ComparisonCondition extends Condition {
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

    private Argument<Double> first, second;
    private COMPARISON comparison;


    @Override
    public Boolean resolve(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
        double f = first.resolve(wrapper, parent, performer, target);
        double s = second.resolve(wrapper, parent, performer, target);
        return compare(comparison, f, s);
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
}
