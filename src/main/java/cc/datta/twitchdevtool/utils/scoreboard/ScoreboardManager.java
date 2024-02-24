

/* Decompiler 11ms, total 152ms, lines 84 */
package cc.datta.twitchdevtool.utils.scoreboard;

import cc.datta.twitchdevtool.TwitchDevTool;
import cc.datta.twitchdevtool.utils.configuration.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;

public class ScoreboardManager implements Listener {
   private Configuration configuration = TwitchDevTool.getInstance().getConfig();

   public void load() {
      this.createAll();
      Bukkit.getPluginManager().registerEvents(this, TwitchDevTool.getInstance());
      long ticks = this.configuration.getLong("Options.update-ticks");
      (new BukkitRunnable() {
         public void run() {
            ScoreboardManager.this.updateAll();
         }
      }).runTaskTimer(TwitchDevTool.getInstance(), ticks, ticks);
   }

   @EventHandler
   private void onPlayerJoin(PlayerJoinEvent event) {
      this.create(event.getPlayer());
   }

   @EventHandler
   private void onPlayerQuit(PlayerQuitEvent event) {
      ScoreHelper.remove(event.getPlayer());
   }

   @EventHandler
   private void onChangeWorld(PlayerChangedWorldEvent event) {
      this.update(event.getPlayer());
   }

   public CustomScoreboard getScoreboard() {
      String name = this.configuration.getString("scoreboards.actual", "default");
      String title = this.configuration.getString("scoreboards." + name + ".title");
      List<String> lines = this.configuration.getStringList("scoreboards." + name + ".lines");
      return new CustomScoreboard(title, lines);
   }

   private void create(Player player) {
      ScoreHelper.create(player);
      this.update(player);
   }

   private void createAll() {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         this.create(player);
      }

   }

   private void update(Player player) {
      ScoreHelper helper = ScoreHelper.get(player);
      CustomScoreboard scoreboard = this.getScoreboard();
      helper.setTitle(scoreboard.getTitle());
      helper.setSlotsFromList(scoreboard.getLines());
   }

   private void updateAll() {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         this.update(player);
      }

   }
}

