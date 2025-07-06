package gabereal.amogus.eliminate_players

import com.llamalad7.mixinextras.MixinExtrasBootstrap
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.loader.api.entrypoint.PreLaunchEntrypoint

public class PreLaunchPlayerEntry : PreLaunchEntrypoint {
    override fun onPreLaunch(mod: ModContainer) {
        MixinExtrasBootstrap.init()
    }
}
