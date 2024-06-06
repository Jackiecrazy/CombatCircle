package jackiecrazy.combatcircle.utils;

import com.google.gson.*;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.ActionRegistry;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.ArgumentRegistry;
import jackiecrazy.combatcircle.move.argument.SingletonArgumentType;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.RawVectorArgument;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import jackiecrazy.combatcircle.move.condition.*;
import jackiecrazy.combatcircle.move.filter.Filter;
import jackiecrazy.combatcircle.move.filter.FilterRegistry;
import jackiecrazy.combatcircle.move.filter.NoFilter;
import jackiecrazy.combatcircle.move.filter.SingletonFilterType;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Type;

public class JsonAdapters {
    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Action.class, new ActionAdapter())
            .registerTypeAdapter(TimerAction.class, new ActionAdapter())
            .registerTypeAdapter(Argument.class, new ArgumentAdapter())
            .registerTypeAdapter(NumberArgument.class, new NumberAdapter())
            .registerTypeAdapter(VectorArgument.class, new VectorAdapter())
            .registerTypeAdapter(EntityArgument.class, new ArgumentAdapter())
            .registerTypeAdapter(Condition.class, new ConditionAdapter())
            .registerTypeAdapter(Filter.class, new FilterAdapter())
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .setPrettyPrinting()
            .create();

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

    public static class ArgumentAdapter implements JsonDeserializer<Argument> {
        @Override
        public Argument deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!json.isJsonObject()) {
                ResourceLocation rl = new ResourceLocation(json.getAsString());
                if (ArgumentRegistry.SUPPLIER.get().getValue(rl) instanceof SingletonArgumentType sat) {
                    return sat.bake(null);
                }
                else throw new JsonParseException("argument is not a singleton: " + json);
            }
            JsonObject sub = json.getAsJsonObject();
            if (sub.has("ID")) {
                ResourceLocation rl = new ResourceLocation(sub.get("ID").getAsString());
                if (ArgumentRegistry.SUPPLIER.get().containsKey(rl)) {
                    return ArgumentRegistry.SUPPLIER.get().getValue(rl).bake(sub);
                }
                throw new JsonParseException("invalid ID defined for argument object: " + json);
            }
            throw new JsonParseException("no ID defined for argument object: " + json);
        }
    }

    public static class ConditionAdapter implements JsonDeserializer<Condition> {
        @Override
        public Condition deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (json.isJsonPrimitive()) {
                return json.getAsBoolean() ? TrueCondition.INSTANCE : FalseCondition.INSTANCE;
            }
            if (!json.isJsonObject()) {
                ResourceLocation rl = new ResourceLocation(json.getAsString());
                if (ConditionRegistry.SUPPLIER.get().getValue(rl) instanceof SingletonConditionType sat) {
                    return sat.bake(null);
                }
                else throw new JsonParseException("condition is not a singleton: " + json);
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
                }
                else throw new JsonParseException("filter is not a singleton: " + json);
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

    public static class VectorAdapter implements JsonDeserializer<VectorArgument> {
        @Override
        public VectorArgument deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!json.isJsonObject()) return RawVectorArgument.ZERO;
            JsonObject sub = json.getAsJsonObject();
            if (sub.has("ID")) {
                ResourceLocation rl = new ResourceLocation(sub.get("ID").getAsString());
                if (ArgumentRegistry.SUPPLIER.get().containsKey(rl)) {
                    if (ArgumentRegistry.SUPPLIER.get().getValue(rl).bake(sub) instanceof VectorArgument va)
                        return va;
                    else
                        throw new JsonParseException("ID defined for vector object must be a vector argument type: " + json);
                }
                throw new JsonParseException("invalid ID defined for vector object: " + json);
            }
            throw new JsonParseException("no ID defined for vector object: " + json);
        }
    }

    public static class NumberAdapter implements JsonDeserializer<NumberArgument> {
        @Override
        public NumberArgument deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (json.isJsonPrimitive())
                return new FixedNumberArgument(json.getAsDouble());
            if (!json.isJsonObject())
                return new FixedNumberArgument(0);
            JsonObject sub = json.getAsJsonObject();
            if (sub.has("ID")) {
                ResourceLocation rl = new ResourceLocation(sub.get("ID").getAsString());
                if (ArgumentRegistry.SUPPLIER.get().containsKey(rl)) {
                    if (ArgumentRegistry.SUPPLIER.get().getValue(rl).bake(sub) instanceof NumberArgument va)
                        return va;
                    else
                        throw new JsonParseException("ID defined for number object must be a number argument type: " + json);
                }
                throw new JsonParseException("invalid ID defined for number object: " + json);
            }
            throw new JsonParseException("number argument is unreadable: " + json);
        }
    }
}
