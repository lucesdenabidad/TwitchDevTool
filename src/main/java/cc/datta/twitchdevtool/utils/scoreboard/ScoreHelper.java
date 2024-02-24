

/* Decompiler 14ms, total 194ms, lines 121 */
package cc.datta.twitchdevtool.utils.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static cc.datta.twitchdevtool.utils.ColorClass.format;

public class ScoreHelper {
   private static HashMap<UUID, ScoreHelper> players = new HashMap();
   private Player player;
   private Scoreboard scoreboard;
   private Objective sidebar;

   public static ScoreHelper create(Player player) {
      return new ScoreHelper(player);
   }

   public static ScoreHelper get(Player player) {
      return (ScoreHelper)players.get(player.getUniqueId());
   }

   public static void remove(Player player) {
      players.remove(player.getUniqueId());
   }

   private ScoreHelper(Player player) {
      this.player = player;
      this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
      this.sidebar = this.scoreboard.registerNewObjective("sidebar", "dummy");
      this.sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
      player.setScoreboard(this.scoreboard);

      for(int i = 1; i <= 15; ++i) {
         Team team = this.scoreboard.registerNewTeam("SLOT_" + i);
         team.addEntry(this.genEntry(i));
      }

      players.put(player.getUniqueId(), this);
   }

   public void setTitle(String title) {
      title = format(this.player, title, new Object[0]);
      if (title.length() > 32) {
         title = title.substring(0, 32);
      }

      if (!this.sidebar.getDisplayName().equals(title)) {
         this.sidebar.setDisplayName(title);
      }

   }

   public void setSlot(int slot, String text) {
      Team team = this.scoreboard.getTeam("SLOT_" + slot);
      String entry = this.genEntry(slot);
      if (!this.scoreboard.getEntries().contains(entry)) {
         this.sidebar.getScore(entry).setScore(slot);
      }

      text = format(this.player, text, new Object[0]);
      String pre = this.getFirstSplit(text);
      String var10001 = ChatColor.getLastColors(pre);
      String suf = this.getFirstSplit(var10001 + this.getSecondSplit(text));
      if (!team.getPrefix().equals(pre)) {
         team.setPrefix(pre);
      }

      if (!team.getSuffix().equals(suf)) {
         team.setSuffix(suf);
      }

   }

   public void removeSlot(int slot) {
      String entry = this.genEntry(slot);
      if (this.scoreboard.getEntries().contains(entry)) {
         this.scoreboard.resetScores(entry);
      }

   }

   public void setSlotsFromList(List<String> list) {
      int slot = list.size();
      if (slot < 15) {
         for(int i = slot + 1; i <= 15; ++i) {
            this.removeSlot(i);
         }
      }

      for(Iterator var5 = list.iterator(); var5.hasNext(); --slot) {
         String line = (String)var5.next();
         this.setSlot(slot, line);
      }

   }

   private String genEntry(int slot) {
      return ChatColor.values()[slot].toString();
   }

   private String getFirstSplit(String s) {
      return s.length() > 200 ? s.substring(0, 200) : s;
   }

   private String getSecondSplit(String s) {
      if (s.length() > 200) {
         s = s.substring(0, 32);
      }

      return s.length() > 200 ? s.substring(200, s.length()) : "";
   }
}

