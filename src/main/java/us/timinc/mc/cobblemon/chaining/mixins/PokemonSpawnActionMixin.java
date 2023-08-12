package us.timinc.mc.cobblemon.chaining.mixins;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.spawning.context.SpawningContext;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnAction;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import us.timinc.mc.cobblemon.chaining.Chaining;

import java.util.List;

@Mixin(value = PokemonSpawnAction.class, remap = false)
public abstract class PokemonSpawnActionMixin {
    @Shadow
    private PokemonProperties props;

    @Inject(method = "createEntity()Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;", at = @At("HEAD"))
    private void modifyShinyRate(CallbackInfoReturnable<Entity> cir) {
        SpawningContext ctx = ((PokemonSpawnAction) (Object) this).getCtx();
        Chaining.INSTANCE.possiblyMakeShiny(ctx, props);
    }

    @Inject(method = "createEntity()Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void possiblyModifyIvs(CallbackInfoReturnable<PokemonEntity> cir, List<ItemStack> list, ItemStack itemStack, PokemonEntity entity) {
        SpawningContext ctx = ((PokemonSpawnAction) (Object) this).getCtx();
        Chaining.INSTANCE.possiblyModifyIvs(entity, ctx, props);
        Chaining.INSTANCE.possiblyAddHiddenAbility(entity, ctx, props);
    }
}