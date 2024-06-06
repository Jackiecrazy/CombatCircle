package jackiecrazy.combatcircle.move.condition;

import com.google.gson.JsonObject;

/**
 only implemented in lambdas in ActionRegistry.
 */
public interface SingletonConditionType<T extends Condition> extends ConditionType<T> {
}
