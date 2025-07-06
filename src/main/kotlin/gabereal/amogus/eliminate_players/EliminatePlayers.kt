package gabereal.amogus.eliminate_players

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

object EliminatePlayers : ModInitializer {
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("EliminatePlayers")

    @JvmField
    val bannedUuids: MutableList<UUID> = mutableListOf()

    override fun onInitialize(mod: ModContainer) {
        bannedUuids.add(UUID.fromString("d0815131-c51a-4831-b973-f69da01e6326"))
        LOGGER.info("Hello Quilt world from ${mod.metadata().name()}!")
    }
}

