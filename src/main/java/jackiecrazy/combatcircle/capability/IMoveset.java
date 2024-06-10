package jackiecrazy.combatcircle.capability;

import jackiecrazy.combatcircle.move.MovesetManager;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import net.minecraft.world.entity.Entity;

public interface IMoveset {
    void setMovesetManager(MovesetManager mm);
    MovesetManager getMovesetManager();
    void mark(Entity from, MovesetWrapper d);

    void update();
}
