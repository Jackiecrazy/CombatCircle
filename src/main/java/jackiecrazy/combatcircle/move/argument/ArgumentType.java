package jackiecrazy.combatcircle.move.argument;

import com.google.gson.JsonObject;
import jackiecrazy.combatcircle.utils.JsonAdapters;

/**
 only implemented in lambdas in ActionRegistry.
 */
public class ArgumentType<T> {
    Class<?> cl;
    public ArgumentType(Class<?> of){
        cl=of;
    }
    public Class<?> getWrappedClass(){
        return cl;
    }
    public Argument<T> bake(JsonObject from){
        return (Argument<T>) JsonAdapters.gson.fromJson(from, cl);
    }
}
