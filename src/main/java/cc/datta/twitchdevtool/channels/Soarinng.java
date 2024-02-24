package cc.datta.twitchdevtool.channels;

import cc.datta.twitchdevtool.TwitchChannel;
import cc.datta.twitchdevtool.TwitchDevTool;
import cc.datta.twitchdevtool.games.TNTGameConst;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Soarinng implements @NotNull Listener {

    private Plugin plugin;
    public TwitchChannel twitchChannel = new TwitchChannel(
            "oauth:admaqe2e2v1p4ys7by86pariibjytp",
            "n184xf9mxokw787uhiuqxr0m1iejxd",
            "ua5clfcu7jpt4l7oemnvi5rs3ujczp",
            "Soarinng", "Soarinng", "&#ffda21&l[Soarinng]", true);


    public Soarinng(Plugin plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, TwitchDevTool.getInstance());
        twitchChannel.getClient().getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(this);
    }

    @EventSubscriber
    public void bits(CheerEvent event) {
        String username = event.getUser().getName();
        Integer bits = event.getBits();

        Bukkit.getScheduler().runTask(TwitchDevTool.getInstance(), () -> {
            TNTGameConst.sendCheers(twitchChannel, TwitchDevTool.getInstance().soarinngList, username, bits);
        });
    }
}