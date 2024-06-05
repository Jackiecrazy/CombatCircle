package jackiecrazy.combatcircle.move.filter;

import com.google.gson.JsonObject;

/**
 only implemented in lambdas in ActionRegistry.
 */
public interface FilterType {
    public Filter bake(JsonObject from);
}
