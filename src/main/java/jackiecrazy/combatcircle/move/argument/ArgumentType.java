package jackiecrazy.combatcircle.move.argument;

import com.google.gson.JsonObject;

/**
 only implemented in lambdas in ActionRegistry.
 */
public interface ArgumentType {
    public Argument bake(JsonObject from);
}
