package jackiecrazy.combatcircle.utils;

import com.google.gson.*;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.action.ActionRegistry;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.ArgumentRegistry;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.ConditionRegistry;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Type;

public class JsonAdapters {
    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Action.class, new ActionAdapter())
            .registerTypeAdapter(TimerAction.class, new ActionAdapter())
            .registerTypeAdapter(Argument.class, new ArgumentAdapter())
            .registerTypeAdapter(NumberArgument.class, new NumberAdapter())
            .registerTypeAdapter(Condition.class, new ConditionAdapter())
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .setPrettyPrinting()
            .create();

    public static class ActionAdapter implements JsonDeserializer<Action> {
        @Override
        public Action deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!json.isJsonObject()) return null;
            JsonObject sub = json.getAsJsonObject();
            if (sub.has("ID")) {
                ResourceLocation rl = new ResourceLocation(sub.get("ID").getAsString());
                if (ActionRegistry.SUPPLIER.get().containsKey(rl)) {
                    return ActionRegistry.SUPPLIER.get().getValue(rl).bake(sub);
                }
            }
            return null;
        }
    }

    public static class ArgumentAdapter implements JsonDeserializer<Argument> {
        @Override
        public Argument deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!json.isJsonObject()) return null;
            JsonObject sub = json.getAsJsonObject();
            if (sub.has("ID")) {
                ResourceLocation rl = new ResourceLocation(sub.get("ID").getAsString());
                if (ArgumentRegistry.SUPPLIER.get().containsKey(rl)) {
                    return ArgumentRegistry.SUPPLIER.get().getValue(rl).bake(sub);
                }
            }
            return null;
        }
    }

    public static class ConditionAdapter implements JsonDeserializer<Condition> {
        @Override
        public Condition deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!json.isJsonObject()) return null;
            JsonObject sub = json.getAsJsonObject();
            if (sub.has("ID")) {
                ResourceLocation rl = new ResourceLocation(sub.get("ID").getAsString());
                if (ConditionRegistry.SUPPLIER.get().containsKey(rl)) {
                    return ConditionRegistry.SUPPLIER.get().getValue(rl).bake(sub);
                }
            }
            return new TrueCondition();
        }
    }

    public static class NumberAdapter implements JsonDeserializer<NumberArgument> {
        @Override
        public NumberArgument deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (json.isJsonPrimitive()) return new FixedNumberArgument(json.getAsDouble());
            if (!json.isJsonObject()) return new FixedNumberArgument(0);
            JsonObject sub = json.getAsJsonObject();
            if (sub.has("ID")) {
                ResourceLocation rl = new ResourceLocation(sub.get("ID").getAsString());
                if (ConditionRegistry.SUPPLIER.get().containsKey(rl)) {
                    return (NumberArgument) ArgumentRegistry.SUPPLIER.get().getValue(rl).bake(sub);
                }
            }
            return new FixedNumberArgument(0);
        }
    }
}
