package cc.datta.twitchdevtool.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static cc.datta.twitchdevtool.utils.ColorClass.format;


public class Players {

    public static void sendTitle(String title, String subtitle){
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(format(title), format(subtitle));
        }
        sendSound(Sound.ENTITY_ITEM_PICKUP,2,1);
    }

    public static void sendSound(Sound sound, float pitch, float volume){
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(),sound,volume,pitch);
        }
    }


    public static void telepor(Location location){
        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.teleport(location.toCenterLocation());
        }
    }
    public static void sendMessage(String string){
        Bukkit.broadcastMessage(format(string));
    }
}
