package cc.datta.twitchdevtool;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class TwitchPlaceholderExtension extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "twdevtool";
    }

    @Override
    public @NotNull String getAuthor() {
        return "datta";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.1.1";
    }

    @Override
    public @NotNull String onRequest(OfflinePlayer offlinePlayer, String param) {

        if (param.equalsIgnoreCase("soarinng_percentage")){
            return TwitchDevTool.getInstance().soarinngtntgame.percentage();
        }
        if (param.equalsIgnoreCase("aquino_percentage")){
            return TwitchDevTool.getInstance().aquinotntgame.percentage();
        }

        return "";
    }

}
