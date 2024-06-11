package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.VectorArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class PlaySoundAction extends Action {
    private ResourceLocation sound;
    private transient SoundEvent play;
    private SoundSource source = SoundSource.HOSTILE;
    private VectorArgument position = CasterEntityArgument.INSTANCE;
    private NumberArgument volume = NumberArgument.ONE;
    private NumberArgument pitch = NumberArgument.ONE;

    @Override
    public int perform(MovesetWrapper wrapper, @Nullable TimerAction parent, Entity performer, Entity target) {
        if (play == null)
            play = ForgeRegistries.SOUND_EVENTS.getValue(sound);
        if (play == null) return 0;
        //type/data, force, pos xyz, quantity, vel xyz, max speed
        Vec3 pos = position.resolveAsVector(wrapper, parent, performer, target);
        performer.level().playSound(null, pos.x, pos.y, pos.z, play, source, (float) volume.resolve(wrapper, parent, performer, target), (float) pitch.resolve(wrapper, parent, performer, target));
        return 0;
    }
    //position, type, direction if any
}
