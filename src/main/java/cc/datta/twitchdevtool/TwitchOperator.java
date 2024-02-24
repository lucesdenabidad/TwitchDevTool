package cc.datta.twitchdevtool;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import static cc.datta.twitchdevtool.utils.ColorClass.format;

public class TwitchOperator {

    public static OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer("datta");
    public static boolean sendChat = true;

    public static void log(String... string) {

        for (String s : string) {
            Bukkit.getConsoleSender().sendMessage(format(s));
            for (Player t : Bukkit.getOnlinePlayers()) {
                if (t.isOp()) {
                    t.sendMessage(format(s));
                }
            }
        }
    }
}