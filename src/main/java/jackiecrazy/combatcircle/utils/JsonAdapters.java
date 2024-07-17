package jackiecrazy.combatcircle.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.ActionRegistry;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.ArgumentRegistry;
import jackiecrazy.combatcircle.move.argument.SingletonArgumentType;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.RawVectorArgument;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import jackiecrazy.combatcircle.move.condition.*;
import jackiecrazy.combatcircle.move.filter.Filter;
import jackiecrazy.combatcircle.move.filter.FilterRegistry;
import jackiecrazy.combatcircle.move.filter.SingletonFilterType;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonAdapters {
    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Class.class, new ClassAdapter())
            .registerTypeAdapter(Supplier.class, new SupplierAdapter())
            .registerTypeAdapter(Action.class, new ActionAdapter())
            .registerTypeAdapter(TimerAction.class, new ActionAdapter())
            .registerTypeAdapter(Argument.class, new ArgumentAdapter())
            .registerTypeAdapter(NumberArgument.class, new NumberAdapter())
            .registerTypeAdapter(VectorArgument.class, new VectorAdapter())
            //.registerTypeAdapter(EntityArgument.class, new ArgumentAdapter())
            .registerTypeAdapter(Condition.class, new ConditionAdapter())
            .registerTypeAdapter(Filter.class, new FilterAdapter())
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(CompoundTag.class, new NBTAdapter())
            .setPrettyPrinting()
            .create();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface JsonRequired
    {
    }

    private static void testRequired(JsonElement je, Type type) throws JsonParseException
    {
        Object pojo = new Gson().fromJson(je, type);

        Field[] fields = pojo.getClass().getDeclaredFields();
        for (Field f : fields)
        {
            if (f.getAnnotation(JsonRequired.class) != null)
            {
                try
                {
                    f.setAccessible(true);
                    if (f.get(pojo) == null)
                    {
                        throw new JsonParseException("Missing field in JSON: " + f.getName());
                    }
                }
                catch (IllegalArgumentException ex)
                {
                    Logger.getLogger(JsonAdapters.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (IllegalAccessException ex)
                {
                    Logger.getLogger(JsonAdapters.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static class ClassAdapter implements JsonDeserializer<Class> {
        @Override
        public Class deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            try {
                return Class.forName(json.getAsString());
            } catch (ClassNotFoundException e) {
                throw new JsonParseException("cannot find class " + json);
            }
        }
    }

    public static class SupplierAdapter implements JsonDeserializer<Supplier<String>> {
        @Override
        public Supplier<String> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return json::getAsString;
        }
    }

    public static class ActionAdapter implements JsonDeserializer<Action> {
        @Override
        public Action deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!json.isJsonObject()) throw new JsonParseException("action must be a Json object: " + json);
            JsonObject sub = json.getAsJsonObject();
            if (sub.has("ID")) {
                ResourceLocation rl = new ResourceLocation(sub.get("ID").getAsString());
                if (ActionRegistry.SUPPLIER.get().containsKey(rl)) {
                    return ActionRegistry.SUPPLIER.get().getValue(rl).bake(sub);
                }
                throw new JsonParseException("invalid ID defined for action object: " + json);
            }
            throw new JsonParseException("no ID defined for action object: " + json);
        }
    }

    public static class ArgumentAdapter<T> implements JsonDeserializer<Argument<T>> {
        @Override
        public Argument<T> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!json.isJsonObject()) {
                //everything is awful
                Class<?> grabby= (Class<?>) ((ParameterizedType) TypeToken.of(type).getType()).getActualTypeArguments()[0];
                if(grabby== Boolean.class){
                    return json.getAsBoolean() ? (Argument<T>) TrueCondition.INSTANCE : (Argument<T>) FalseCondition.INSTANCE;
                }
                else if(grabby==Double.class){
                    if (json.isJsonPrimitive())
                        return (Argument<T>) new FixedNumberArgument(json.getAsDouble());
                    if (!json.isJsonObject())
                        return (Argument<T>) FixedNumberArgument.ZERO;
                }
                else if(grabby==Vec3.class){
                    if (!json.isJsonObject()) return (Argument<T>) RawVectorArgument.ZERO;
                }
                //object f
                ResourceLocation rl = new ResourceLocation(json.getAsString());
                if (ArgumentRegistry.SUPPLIER.get().getValue(rl) instanceof SingletonArgumentType sat) {
                    return sat.bake(null);
                } else throw new JsonParseException("argument is not a singleton: " + json);
            }
            JsonObject sub = json.getAsJsonObject();
            if (sub.has("ID")) {
                ResourceLocation rl = new ResourceLocation(sub.get("ID").getAsString());
                if (ArgumentRegistry.SUPPLIER.get().containsKey(rl)) {
                    try {
                        return (Argument<T>) ArgumentRegistry.SUPPLIER.get().getValue(rl).bake(sub);
                    }catch(ClassCastException e) {
                        throw new JsonParseException("incorrect argument type: " + json);
                    }
                }
                throw new JsonParseException("invalid ID defined for argument object: " + json);
            }
            throw new JsonParseException("no ID defined for argument object: " + json);
        }
    }
    public static class VectorAdapter implements JsonDeserializer<Argument<Vec3>> {
        @Override
        public Argument<Vec3> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!json.isJsonObject()) return RawVectorArgument.ZERO;
            JsonObject sub = json.getAsJsonObject();
            if (sub.has("ID")) {
                ResourceLocation rl = new ResourceLocation(sub.get("ID").getAsString());
                if (ArgumentRegistry.SUPPLIER.get().containsKey(rl)) {
                    try {
                        return (Argument<Vec3>) ArgumentRegistry.SUPPLIER.get().getValue(rl).bake(sub);
                    }catch(ClassCastException e) {
                        throw new JsonParseException("ID defined for vector object must be a vector argument type: " + json);
                    }
                }
                throw new JsonParseException("invalid ID defined for vector object: " + json);
            }
            throw new JsonParseException("no ID defined for vector object: " + json);
        }
    }

    public static class NumberAdapter implements JsonDeserializer<Argument<Double>> {
        @Override
        public Argument<Double> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (json.isJsonPrimitive())
                return new FixedNumberArgument(json.getAsDouble());
            if (!json.isJsonObject())
                return new FixedNumberArgument(0);
            JsonObject sub = json.getAsJsonObject();
            if (sub.has("ID")) {
                ResourceLocation rl = new ResourceLocation(sub.get("ID").getAsString());
                if (ArgumentRegistry.SUPPLIER.get().containsKey(rl)) {
                    try {
                        return (Argument<Double>) ArgumentRegistry.SUPPLIER.get().getValue(rl).bake(sub);
                    }catch(ClassCastException e) {
                        throw new JsonParseException("ID defined for number object must be a number argument type: " + json);
                    }
                }
                throw new JsonParseException("invalid ID defined for number object: " + json);
            }
            throw new JsonParseException("number argument is unreadable: " + json);
        }
    }

    public static class ConditionAdapter implements JsonDeserializer<Argument<Boolean>> {
        @Override
        public Argument<Boolean> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (json.isJsonPrimitive()) {
                return json.getAsBoolean() ? TrueCondition.INSTANCE : FalseCondition.INSTANCE;
            }
            if (!json.isJsonObject()) {
                ResourceLocation rl = new ResourceLocation(json.getAsString());
                if (ConditionRegistry.SUPPLIER.get().getValue(rl) instanceof SingletonConditionType sat) {
                    return sat.bake(null);
                } else throw new JsonParseException("condition is not a singleton: " + json);
            }
            JsonObject sub = json.getAsJsonObject();
            if (sub.has("ID")) {
                ResourceLocation rl = new ResourceLocation(sub.get("ID").getAsString());
                if (ConditionRegistry.SUPPLIER.get().containsKey(rl)) {
                    return ConditionRegistry.SUPPLIER.get().getValue(rl).bake(sub);
                }
                throw new JsonParseException("invalid ID defined for condition object: " + json);
            }
            return TrueCondition.INSTANCE;
        }
    }

    public static class FilterAdapter implements JsonDeserializer<Filter> {
        @Override
        public Filter deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!json.isJsonObject()) {
                ResourceLocation rl = new ResourceLocation(json.getAsString());
                if (FilterRegistry.SUPPLIER.get().getValue(rl) instanceof SingletonFilterType sat) {
                    return sat.bake(null);
                } else throw new JsonParseException("filter is not a singleton: " + json);
            }
            JsonObject sub = json.getAsJsonObject();
            if (sub.has("ID")) {
                ResourceLocation rl = new ResourceLocation(sub.get("ID").getAsString());
                if (FilterRegistry.SUPPLIER.get().containsKey(rl)) {
                    return FilterRegistry.SUPPLIER.get().getValue(rl).bake(sub);
                }
                throw new JsonParseException("invalid ID defined for filter object: " + json);
            }
            throw new JsonParseException("no ID defined for filter object: " + json);
        }
    }

    public static class NBTAdapter implements JsonDeserializer<CompoundTag> {

        @Override
        public CompoundTag deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!json.isJsonPrimitive()) throw new JsonParseException("not an NBT tag: " + json);
            try {
                return CompoundTagArgument.compoundTag().parse(new StringReader(json.getAsString()));
            } catch (CommandSyntaxException e) {
                throw new JsonParseException("invalid NBT definition: " + json);
            }
        }
    }
}
