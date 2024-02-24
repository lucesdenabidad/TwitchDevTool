package cc.datta.twitchdevtool.games;

import cc.datta.twitchdevtool.TwitchChannel;
import cc.datta.twitchdevtool.TwitchDevTool;
import cc.datta.twitchdevtool.utils.Players;
import cc.datta.twitchdevtool.utils.ToolUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

import static cc.datta.twitchdevtool.utils.ColorClass.format;
import static cc.datta.twitchdevtool.utils.ColorClass.stringToLocation;
import static cc.datta.twitchdevtool.utils.tools.WorldEditTool.fill;

public class TNTGame implements Listener {

    private TwitchChannel channel;
    public Location pos1;
    public Location pos2;
    public BukkitTask task;
    public List<TntGameDonations> donationsList;
    public OfflinePlayer player;

    private int contador = 0;
    private boolean contadorEnEjecucion = false;


    public TNTGame(TwitchChannel channel, Location[] locations, List<TntGameDonations> donationsList) {
        this.channel = channel;
        this.pos1 = locations[0];
        this.pos2 = locations[1];
        this.donationsList = donationsList;
        this.player = channel.getOfflinePlayer();
    }

    public void startTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checker();
                TNTGameConst.replaceBlocksInCube(pos1, pos2);
            }
        }.runTaskTimer(TwitchDevTool.getInstance(), 0, 10L);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        TNTGameConst.onEntityExplode(event);
    }

    public boolean isFull() {
        return TNTGameConst.isFull(pos1, pos2);
    }

    public void checker() {

        if (isFull() && !contadorEnEjecucion) {
            startTimer();
        } else if (contadorEnEjecucion && !isFull()) {
            stopTimer();
        }
    }

    public void startTimer() {
        contador = 5;
        contadorEnEjecucion = true;
        Players.sendTitle("&c&lCUIDADO!", "&fContador del contricante iniciado!");
        Players.sendSound(Sound.ENTITY_VILLAGER_NO, 1, 1);
        Bukkit.broadcastMessage(format(channel.getPrefix()+ "&r El temporizador de &a5 segundos&f empezo para el."));

        task = new BukkitRunnable() {
            public void run() {
                if (contador != 0) {
                    sendTime(contador);
                    contador--;
                } else {
                    endTimer(channel);
                }
            }
        }.runTaskTimer(TwitchDevTool.getInstance(), 0L, 20L);
    }


    private void sendTime(int i) {
        OfflinePlayer offlinePlayer = channel.getOfflinePlayer();
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            player.sendTitle(format("&#ffc021&l" + contador), "");
            player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 1.0F, 1.0F);
        }
    }

    private void endTimer(TwitchChannel channel) {
        fill(stringToLocation("0 23 1"), stringToLocation("-15 12 -15"), Material.AIR);
        fill(stringToLocation("27 12 -15"), stringToLocation("12 23 1"), Material.AIR);
        Bukkit.broadcastMessage(format(channel.getPrefix() + "&r Completo y finalizo su cubo con éxito."));
        Players.sendTitle("&c&lPERDISTE!", "Tu contricante gano!");
        Players.sendSound(Sound.ENTITY_VILLAGER_NO, 1, 1);

        OfflinePlayer offlinePlayer = channel.getOfflinePlayer();
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            player.sendTitle(format("&a&l¡GANASTE!"), format("&8> &f¡Tu contricante perdio! &8<"));
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F);
            for (int i = 0; i < 15; ++i) {
                ToolUtil.wFirework(player.getLocation());
            }
        }

        contadorEnEjecucion = false;
        task.cancel();
    }

    public void stopTimer() {

        if (contadorEnEjecucion) {
            task.cancel();
            contadorEnEjecucion = false;
            Bukkit.broadcastMessage(format(channel.getPrefix() + "&r Se le cancelo el temporizador por que su cubo no esta completo."));
            Players.sendTitle(format("&#ff0000&lWRONG WRONG!"), format("&fTemporizador cancelado de &a"+channel.getChannels().get(0)));
            Players.sendSound(Sound.ENTITY_VILLAGER_NO, 1, 1);

        }
    }



    public String percentage() {
        return TNTGameConst.percentage(pos1, pos2);
    }
}
