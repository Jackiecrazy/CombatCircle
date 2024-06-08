package jackiecrazy.combatcircle.move.capability;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import net.minecraft.world.entity.Entity;

public interface IMark {
    void mark(Entity from, MovesetWrapper d);

    void update();
}
