package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.vector.PositionVectorArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class PlaySoundAction extends Action {
    private Argument<ResourceLocation> sound;
    private transient SoundEvent play;
    private SoundSource source = SoundSource.HOSTILE;
    private Argument<Vec3> position = PositionVectorArgument.CASTER;
    private Argument<Double> volume = FixedNumberArgument.ONE;
    private Argument<Double> pitch = FixedNumberArgument.ONE;

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        if (play == null)
            play = ForgeRegistries.SOUND_EVENTS.getValue(sound.resolve(wrapper, parent, performer, target));
        if (play == null) return 0;
        //type/data, force, pos xyz, quantity, vel xyz, max speed
        Vec3 pos = position.resolve(wrapper, parent, performer, target);
        performer.level().playSound(null, pos.x, pos.y, pos.z, play, source, volume.resolve(wrapper, parent, performer, target).floatValue(), pitch.resolve(wrapper, parent, performer, target).floatValue());
        return 0;
    }
    //position, type, direction if any
}
