package jackiecrazy.combatcircle.move.action;

import com.google.gson.JsonObject;

/**
 only implemented in lambdas in ActionRegistry.
 */
public interface ActionType {
    public Action bake(JsonObject from);
}
