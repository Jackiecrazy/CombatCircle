package jackiecrazy.combatcircle.move.argument.resourcelocation;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public abstract class ResourceLocationArgument implements Argument<ResourceLocation> {
    public static class Raw extends ResourceLocationArgument{
        public Raw(){

        }
        public Raw(String of){
            value=ResourceLocation.tryParse(of);
        }
        private ResourceLocation value;
        @Override
        public ResourceLocation resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
            return value;
        }
    }
    public static class Get extends ResourceLocationArgument{
        private String from;
        @Override
        public ResourceLocation resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
            return ResourceLocation.tryParse(caster.getPersistentData().getString(from));
        }
    }
    public static class Store extends Action {
        private Argument<ResourceLocation> value;
        private String into;

        @Override
        public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
            final ResourceLocation vec = value.resolve(wrapper, parent, performer, target);
            performer.getPersistentData().putString(into, vec.toString());
            return 0;
        }
    }
}
