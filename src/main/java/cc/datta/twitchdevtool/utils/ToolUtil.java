

/* Decompiler 7ms, total 152ms, lines 56 */
package cc.datta.twitchdevtool.utils;

import cc.datta.twitchdevtool.TwitchDevTool;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class ToolUtil {
   static int countdowntask;

   public static void pMsg(Player player, String msg) {
      pSound(player, Sound.ENTITY_ITEM_FRAME_ADD_ITEM, 2.0F);
      player.sendMessage(ColorClass.format(player, msg, new Object[0]));
   }

   public static void pTitle(Player player, String title, String subtitle) {
      player.sendTitle(ColorClass.format(player, title, new Object[0]), ColorClass.format(player, subtitle, new Object[0]), 5, 30, 5);
   }

   public static void pSound(Player player, Sound sound) {
      player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
   }

   public static void pSound(Player player, Sound sound, float pitch) {
      player.playSound(player.getLocation(), sound, 1.0F, pitch);
   }

   public static void wFirework(Location player) {
      Firework firework = (Firework) player.getWorld().spawn(player, Firework.class);
      FireworkMeta fireworkMeta = firework.getFireworkMeta();
      FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.LIME).withFade(Color.YELLOW).with(Type.BURST).trail(true).build();
      fireworkMeta.addEffect(effect);
      fireworkMeta.setPower(1);
      firework.setFireworkMeta(fireworkMeta);
   }


   public static Location obtenerLocationConMayorY(Location p1, Location p2) {
      int blockY1 = p1.getBlockY();
      int blockY2 = p2.getBlockY();

      if (blockY1 > blockY2) {
         return p1;
      } else {
         return p2;
      }
   }

   public static void pCountdown(int time) {
      int[] ftime = new int[]{time};
      countdowntask = Bukkit.getScheduler().runTaskTimer(TwitchDevTool.getInstance(), () -> {
         if (ftime[0] > 0) {
            Players.sendTitle("&a" + ftime[0], "");
            int var10002 = ftime[0]--;
         } else {
            Bukkit.getScheduler().cancelTask(countdowntask);
         }

      }, 0L, 20L).getTaskId();
   }
}

