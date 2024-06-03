package jackiecrazy.combatcircle.move.condition;

import com.google.gson.JsonObject;

/**
 only implemented in lambdas in ActionRegistry.
 */
public interface ConditionType {
    public Condition bake(JsonObject from);
}
