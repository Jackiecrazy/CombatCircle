package jackiecrazy.combatcircle.move.argument;

import com.google.gson.JsonObject;
import jackiecrazy.combatcircle.utils.JsonAdapters;

public abstract class Argument {
    private String ID="(default)";

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
