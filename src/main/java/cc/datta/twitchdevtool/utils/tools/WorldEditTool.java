

/* Decompiler 21ms, total 169ms, lines 80 */
package cc.datta.twitchdevtool.utils.tools;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldEditTool {
   public static Region getWorldEditSelection(Player player) {
      try {
         WorldEditPlugin worldEditPlugin = getWorldEditPlugin();
         if (worldEditPlugin != null && worldEditPlugin.isEnabled()) {
            return worldEditPlugin.getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld()));
         } else {
            throw new IllegalStateException("WorldEdit no está habilitado. Instálalo y actívalo para usar esta función.");
         }
      } catch (IncompleteRegionException var2) {
         player.sendMessage("Error: Debes seleccionar dos puntos con WorldEdit para crear un área.");
         return null;
      } catch (Exception var3) {
         player.sendMessage("Error al obtener la selección del jugador. Consulta la consola para más detalles.");
         return null;
      }
   }

   private static WorldEditPlugin getWorldEditPlugin() {
      return (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
   }

   public static void fill(Location pos1, Location pos2, Material material) {
      World world = pos1.getWorld();
      int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
      int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
      int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
      int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
      int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
      int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

      for(int x = minX; x <= maxX; ++x) {
         for(int y = minY; y <= maxY; ++y) {
            for(int z = minZ; z <= maxZ; ++z) {
               Location blockLocation = new Location(world, (double)x, (double)y, (double)z);
               Block block = blockLocation.getBlock();
               block.setType(material);
            }
         }
      }

   }

   public static void replace(Location pos1, Location pos2, Material material, boolean bypassAir) {
      World world = pos1.getWorld();
      int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
      int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
      int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
      int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
      int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
      int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

      for(int x = minX; x <= maxX; ++x) {
         for(int y = minY; y <= maxY; ++y) {
            for(int z = minZ; z <= maxZ; ++z) {
               Location blockLocation = new Location(world, (double)x, (double)y, (double)z);
               Block block = blockLocation.getBlock();
               if ((bypassAir || block.getType() != Material.AIR) && block.getType() != Material.TNT) {
                  block.setType(material);
               }
            }
         }
      }

   }
}

