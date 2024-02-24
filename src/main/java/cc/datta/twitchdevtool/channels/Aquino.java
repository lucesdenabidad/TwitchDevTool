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

public class Aquino implements @NotNull Listener {

    private Plugin plugin;
    public TwitchChannel twitchChannel = new TwitchChannel(
            "oauth:wcznz05s54kaqxpik1n1vvbjaqrk3y",
            "2mo44t53hukxuktmxfx99q6ob3lw2b",
            "q6mmu0orhmxvr12wbmd6hp8za17t79",
            "aquino", "AQUINOby02", "&#ad03fc&l[Aquino]", true);

    public Aquino(Plugin plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, TwitchDevTool.getInstance());
        twitchChannel.getClient().getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(this);
    }

    @EventSubscriber
    public void bits(CheerEvent event) {
        String username = event.getUser().getName();
        Integer bits = event.getBits();


        Bukkit.getScheduler().runTask(TwitchDevTool.getInstance(), () -> {
            TNTGameConst.sendCheers(twitchChannel, TwitchDevTool.getInstance().aquinoList, username, bits);
        });
    }
}