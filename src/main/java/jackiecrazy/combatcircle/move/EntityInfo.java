package jackiecrazy.combatcircle.move;

import net.minecraft.world.entity.ai.goal.Goal;

public class EntityInfo {
    public float mob_size;
    public double encirclement_distance;//todo add attribute modifier action
    public MovesetFactory[] moveset;
    public Class<Goal>[] to_wipe;

    public EntityInfo(float mob_size, MovesetFactory[] moveset, Class<Goal>[] to_wipe) {
        this.mob_size = mob_size;
        this.moveset = moveset;
        this.to_wipe = to_wipe;
    }
}
