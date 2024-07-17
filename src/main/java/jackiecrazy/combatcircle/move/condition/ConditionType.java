package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.argument.ArgumentType;

/**
 only implemented in lambdas in ActionRegistry.
 */
public class ConditionType extends ArgumentType<Boolean> {
    public ConditionType(Class<?> of) {
        super(of);
    }
}
