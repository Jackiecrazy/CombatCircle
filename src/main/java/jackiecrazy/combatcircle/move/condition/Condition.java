package jackiecrazy.combatcircle.move.condition;

import com.google.gson.JsonObject;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.utils.MoveUtils;
import net.minecraft.world.entity.LivingEntity;

public abstract class Condition {
    private String ID;

    public abstract boolean evaluate(TimerAction parent, LivingEntity performer, LivingEntity target);

    public String serializeToJson(JsonObject to) {
        return MoveUtils.gson.toJson(this);
    }

    public void readFromJson(JsonObject from){
        //chat is this real//
        MoveUtils.gson.fromJson(from, this.getClass());
    }

    public String toString() {
        return ID;
    }
}
