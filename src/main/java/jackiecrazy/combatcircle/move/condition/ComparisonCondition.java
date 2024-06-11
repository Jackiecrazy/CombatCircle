package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
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

    private NumberArgument first, second;
    private COMPARISON comparison;


    @Override
    public boolean evaluate(MovesetWrapper wrapper, TimerAction parent, Entity performer, Entity target) {
        double f = first.resolve(wrapper, parent, performer, target);
        double s = second.resolve(wrapper, parent, performer, target);
        return switch (comparison) {
            case EQ -> f == s;
            case NEQ -> f != s;
            case LE -> f < s;
            case GE -> f > s;
            case LEQ -> f <= s;
            case GEQ -> f >= s;
        };
    }
}
