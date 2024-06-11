package jackiecrazy.combatcircle.move;

import com.google.gson.JsonObject;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PathfinderMob;

import java.util.ArrayList;
import java.util.List;

public class MovesetFactory {
    int starting_weight=1;
    int weight_change=0;
    int size=1;
    JsonObject condition;
    ResourceLocation move;

    public boolean validate() {
        try {
            JsonAdapters.gson.fromJson(Moves.moves.get(move), TimerAction[].class);
            JsonAdapters.gson.fromJson(condition, Condition.class);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public MovesetWrapper generateMoveset() {
        ArrayList<TimerAction> timers = new ArrayList<>(List.of(JsonAdapters.gson.fromJson(Moves.moves.get(move), TimerAction[].class)));
        return new MovesetWrapper(size, starting_weight, weight_change, timers, generateCondition());
    }

    public Condition generateCondition() {
        return JsonAdapters.gson.fromJson(condition, Condition.class);
    }
}
