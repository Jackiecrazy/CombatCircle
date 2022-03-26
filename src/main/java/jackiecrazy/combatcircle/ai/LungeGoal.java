package jackiecrazy.combatcircle.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.vector.Vector3d;

public class LungeGoal extends DelayedAttackGoal{
    public LungeGoal(MobEntity host) {
        super(host, new DelayedAttack(20, 3, 10)
                .setInitiationRadius(5)
                .setAttackRadius(1)
                .setCanTurn(true)
                .setTaskType(TaskType.RANGEDATTACK)
                .setWindupVec(new Vector3d(0, 0.3, 2)));
    }
}
