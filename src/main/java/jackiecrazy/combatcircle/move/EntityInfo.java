package jackiecrazy.combatcircle.move;

import com.google.gson.JsonParseException;
import net.minecraft.world.entity.ai.goal.Goal;

public class EntityInfo {
    public float mob_size;
    public double encirclement_distance;
    public double pursue_walk_speed = 1.0, pursue_sprint_speed = 1.3;
    public MovesetFactory[] moveset;
    public Class<Goal>[] to_wipe;

    public EntityInfo(float mob_size, MovesetFactory[] moveset, Class<Goal>[] to_wipe) {
        this.mob_size = mob_size;
        this.moveset = moveset;
        this.to_wipe = to_wipe;
    }

    public void bake() throws JsonParseException{
        for(MovesetFactory mf:moveset){
            mf.validateAndBake();
        }
    }
}
