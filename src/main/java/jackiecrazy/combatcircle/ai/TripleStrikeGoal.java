package jackiecrazy.combatcircle.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.vector.Vector3d;

public class TripleStrikeGoal extends DelayedAttackGoal {
    public TripleStrikeGoal(MobEntity host) {
        super(host, new DelayedAttack(20, 3, 4)
                        .setInitiationRadius(1)
                        .setAttackRadius(1)
                        .setCanTurn(true)
                        .setTaskType(TaskType.MELEEATTACK),
                new DelayedAttack(1, 3, 3)
                        .setInitiationRadius(1)
                        .setAttackRadius(1)
                        .setCanTurn(true)
                        .setTaskType(TaskType.MELEEATTACK),
                new DelayedAttack(1, 3, 40)
                        .setInitiationRadius(1)
                        .setAttackRadius(1)
                        .setWindupVec(new Vector3d(0, 0.3, 2))
                        .setTaskType(TaskType.MELEEATTACK));
    }
}
