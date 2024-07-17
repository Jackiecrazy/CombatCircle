package jackiecrazy.combatcircle.move.argument;

import com.google.gson.JsonObject;

/**
 only implemented in lambdas in ActionRegistry.
 */
public class SingletonArgumentType<T> extends ArgumentType<T> {
    Argument<T> inst;
    public SingletonArgumentType(Class<?> of, Argument<T> instance) {
        super(of);
        inst=instance;
    }
    public Argument<T> bake(JsonObject from){
        return inst;
    }
}
