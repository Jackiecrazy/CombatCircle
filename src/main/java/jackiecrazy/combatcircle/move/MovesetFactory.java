package jackiecrazy.combatcircle.move;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class MovesetFactory {
    int starting_weight = 1;
    int weight_change = 0;
    int size = 1;
    JsonObject condition;
    ResourceLocation move;
    transient ArrayList<TimerAction> finalized;
    transient Condition finalizedCondition;

    public boolean validateAndBake() {
        try {
            finalized = new ArrayList<>(List.of(JsonAdapters.gson.fromJson(Moves.moves.get(move), TimerAction[].class)));
            finalizedCondition = JsonAdapters.gson.fromJson(condition, Condition.class);
        } catch (Exception e) {
            throw new JsonParseException("invalid moveset " + move + " or condition " + condition);
        }
        return true;
    }

    public MovesetWrapper generateMoveset() {
        if (finalized == null) validateAndBake();
        return new MovesetWrapper(size, starting_weight, weight_change, finalized, finalizedCondition);
    }
}
