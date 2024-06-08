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
    int weight;
    int priority;
    JsonObject condition;
    ResourceLocation move;

    public MovesetFactory(int weight, int priority, JsonObject condition, ResourceLocation move) {
        this.weight = weight;
        this.priority = priority;
        this.condition = condition;
        this.move = move;
    }

    public boolean validate() {
        try {
            JsonAdapters.gson.fromJson(Moves.moves.get(move), TimerAction[].class);
            JsonAdapters.gson.fromJson(condition, Condition.class);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public MovesetWrapper generateMoveset() {
        ArrayList<TimerAction> timers = new ArrayList<>(List.of(JsonAdapters.gson.fromJson(Moves.moves.get(move), TimerAction[].class)));
        return new MovesetWrapper(timers);
    }

    public void attachToMob(PathfinderMob mob) {
        mob.goalSelector.addGoal(priority, new MovesetGoal(mob, weight, generateMoveset(), JsonAdapters.gson.fromJson(condition, Condition.class)));
    }
}
