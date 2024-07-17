package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.SingletonArgumentType;

/**
 only implemented in lambdas in ActionRegistry.
 */
public class SingletonConditionType extends SingletonArgumentType<Boolean> {
    public SingletonConditionType(Class<?> of, Argument<Boolean> instance) {
        super(of, instance);
    }
}
