package jackiecrazy.combatcircle.move.condition;

import com.google.gson.JsonObject;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public abstract class Condition {
    private String ID = "(default)";

    public abstract boolean evaluate(MovesetWrapper wrapper, @Nullable TimerAction parent, Entity performer, Entity target);

    public String serializeToJson(JsonObject to) {
        return JsonAdapters.gson.toJson(this);
    }

    public void readFromJson(JsonObject from){
        //chat is this real//
        JsonAdapters.gson.fromJson(from, this.getClass());
    }

    public String toString() {
        return ID;
    }
}
