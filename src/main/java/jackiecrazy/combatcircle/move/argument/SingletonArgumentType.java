package jackiecrazy.combatcircle.move.argument;

import com.google.gson.JsonObject;
import org.codehaus.plexus.util.cli.Arg;

/**
 only implemented in lambdas in ActionRegistry.
 */
public interface SingletonArgumentType extends ArgumentType {
    Argument bake(JsonObject from);
}
