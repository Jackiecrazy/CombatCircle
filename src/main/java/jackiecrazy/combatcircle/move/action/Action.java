package jackiecrazy.combatcircle.move.action;

import com.google.gson.JsonObject;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.utils.MoveUtils;
import jackiecrazy.footwork.move.Move;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public abstract class Action extends Move {
    private List<Condition> conditions;
    private String ID;

    public abstract void perform(LivingEntity performer, LivingEntity target);

    public String serializeToJson(JsonObject to) {
        return MoveUtils.gson.toJson(this);
    }

    public void readFromJson(JsonObject from) {
        //chat is this real//
        MoveUtils.gson.fromJson(from, this.getClass());
    }

    public boolean canRun(TimerAction parent, LivingEntity performer, LivingEntity target) {
        //if (finished) return false;
        for (Condition c : conditions) {
            if (!c.evaluate(parent, performer, target)) return false;
        }
        //finished = true;
        return true;

    }

    public void reset() {
        //finished = false;
    }

    public String toString() {
        return ID;
    }
}
