/*package gabereal.amogus.eliminate_players.mixin;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gabereal.amogus.eliminate_players.EliminatePlayers;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.List;

@Mixin(ClientCommandSource.class)
public class ClientCommandSourceMixin {
	@Shadow @Final private ClientPlayNetworkHandler networkHandler;

	@ModifyReturnValue(method = "getPlayerNames", at = @At("RETURN"))
	private Collection<String> eplayers$dontGetAllPlayersArgType(Collection<String> original) {
		List<String> list = Lists.newArrayList();
		for(PlayerListEntry playerListEntry : this.networkHandler.getPlayerList()) {
			if(EliminatePlayers.bannedUuids.contains(playerListEntry.getProfile().getId()))
				list.add(playerListEntry.getProfile().getName());
		}
		original.removeAll(list);
		return original;
	}
}*/
