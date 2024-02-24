package cc.datta.twitchdevtool.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static cc.datta.twitchdevtool.utils.ColorClass.format;

public class Messages implements Listener {

    String join = "&a{0} se unió al servidor.";
    String quit = "&c{0} abandonó el servidor.";

    @EventHandler
    public void join(PlayerJoinEvent event){
        event.setJoinMessage(format(join, event.getPlayer().getName()));
    }

    @EventHandler
    public void quit(PlayerQuitEvent event){
        event.setQuitMessage(format(quit, event.getPlayer().getName()));
    }
}
