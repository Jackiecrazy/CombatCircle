package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.LookVectorArgument;
import jackiecrazy.combatcircle.move.argument.vector.PositionVectorArgument;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpawnEntityAction extends Action {
    private EntityArgument summoner = CasterEntityArgument.INSTANCE;
    private ResourceLocation entity;
    private CompoundTag tag;
    private NumberArgument quantity = FixedNumberArgument.ONE;
    private NumberArgument spread = FixedNumberArgument.ZERO;
    private VectorArgument position = PositionVectorArgument.CASTER;
    private VectorArgument facing = new LookVectorArgument();
    private List<Action> on_spawn = new ArrayList<>();

    @Override
    public int perform(MovesetWrapper wrapper, @Nullable TimerAction parent, Entity performer, Entity target) {
        Entity summoner = this.summoner.resolveAsEntity(performer, target);
        int toSpawn = (int) quantity.resolve(performer, target);
        double deviation = spread.resolve(performer, target);
        Vec3 vec = position.resolve(performer, target);
        Level level = performer.level();
        if (level instanceof ServerLevel serverlevel) {
            for (int x = 0; x < toSpawn; x++) {
                Vec3 rand = new Vec3((CombatCircle.rand.nextDouble()) * deviation, (CombatCircle.rand.nextDouble()) * deviation, (CombatCircle.rand.nextDouble()) * deviation);
                final Vec3 pos = vec.add(rand);
                CompoundTag compoundtag = tag == null ? new CompoundTag() : tag.copy();
                compoundtag.putString("id", entity.toString());
                Vec3 look = facing.resolve(performer, target);
                Entity summon = EntityType.loadEntityRecursive(compoundtag, serverlevel, (toSummon) -> {
                    double flatDist = Math.sqrt(look.x * look.x + look.z * look.z);
                    toSummon.moveTo(pos.x, pos.y, pos.z, (float) Mth.wrapDegrees(GeneralUtils.deg((float) Mth.atan2(look.z, look.x)) - 90.0F), Mth.wrapDegrees(-GeneralUtils.deg((float) Mth.atan2(look.y, flatDist))));
                    return toSummon;
                });
                if (summon != null) {
                    if (tag == null && summon instanceof Mob m) {
                        m.finalizeSpawn(serverlevel, serverlevel.getCurrentDifficultyAt(summon.blockPosition()), MobSpawnType.COMMAND, null, null);
                    }

                    serverlevel.tryAddFreshEntityWithPassengers(summon);
                    for (Action a : on_spawn) {
                        a.perform(wrapper, parent, summoner, summon);
                    }

                }
            }
        }
        return 0;
    }
}
