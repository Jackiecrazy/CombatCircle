package jackiecrazy.combatcircle.move.argument;

import com.google.gson.JsonObject;
import jackiecrazy.combatcircle.utils.MoveUtils;

public abstract class Argument {
    private String ID;

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
