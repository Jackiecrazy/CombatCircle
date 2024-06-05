package jackiecrazy.combatcircle.move;

public class CircleEnums {
    public enum REFERENCE_POINT {
        PERFORMER,
        TARGET,
        PO
    }

    public enum SWEEPTYPE {
        //need length and width stats
        NONE,//resolves to true against the target only
        CONE,//horizontal fan area in front of the entity
        CLEAVE,//cone but vertical
        LINE,//1 block wide line up to max range
        CIRCLE//splash with entity as center, ignores range, base and scale add radius
    }
}
