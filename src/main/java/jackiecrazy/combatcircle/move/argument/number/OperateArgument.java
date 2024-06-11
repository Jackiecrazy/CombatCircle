package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class OperateArgument extends NumberArgument {
    public enum OPERATOR {
        ADD("+"),
        SUB("-"),
        MUL("*"),
        DIV("/"),
        MOD("%"),
        POW("^");

        private final String value;

        OPERATOR(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value; //will return , or ' instead of COMMA or APOSTROPHE
        }
    }

    private NumberArgument first, second;
    private OPERATOR comparison;


    @Override
    public double resolve(MovesetWrapper wrapper, Entity performer, Entity target) {
        double f = first.resolve(wrapper, performer, target);
        double s = second.resolve(wrapper, performer, target);
        return switch (comparison) {
            case ADD -> f + s;
            case SUB -> f - s;
            case MUL -> f * s;
            case DIV -> f / s;
            case MOD -> f % s;
            case POW -> Math.pow(f, s);
        };
    }
}
