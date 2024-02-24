package cc.datta.twitchdevtool.games;

import cc.datta.twitchdevtool.TwitchChannel;
import cc.datta.twitchdevtool.TwitchDevTool;
import cc.datta.twitchdevtool.utils.ToolUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static cc.datta.twitchdevtool.utils.ColorClass.format;
import static cc.datta.twitchdevtool.utils.tools.WorldEditTool.replace;

public class TNTGameConst {

    public static void tnt(Location location) {
        World world = location.getWorld();
        TNTPrimed entity = (TNTPrimed) world.spawnEntity(location, EntityType.PRIMED_TNT);
        entity.setFuseTicks(5);
    }

    @EventHandler
    public static void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }

        Iterator<Block> iterator = event.blockList().iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (!isBuild(block.getType())) {
                iterator.remove();
            }
        }
    }

    private static boolean isBuild(Material material) {
        List<Material> builds = List.of(Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.AMETHYST_BLOCK, Material.TNT);
        return builds.contains(material);
    }

    public static void sendCheers(TwitchChannel channel, List<TntGameDonations> donationsList, String name, int cheers) {
        OfflinePlayer offlinePlayer = channel.getOfflinePlayer();
        Player player = offlinePlayer.getPlayer();

        if (player == null || !player.isOnline()) {
            TwitchDevTool.getInstance().getLogger().info("Streamer offline: " + channel.getChannels().get(0));
            return;
        }

        for (TntGameDonations donation : donationsList) {
            if (donation.bits == cheers) {
                donation.run.accept(player);
                Bukkit.broadcastMessage(format(channel.getPrefix() + " &8> &#94fc03{0}&f {1} con &#fc0303{2} bits", name, donation.getName(), cheers));
                ToolUtil.pSound(player, Sound.ENTITY_VILLAGER_CELEBRATE, 1.0F);
                return;
            }
        }

        TwitchDevTool.getInstance().getLogger().info("Valor inesperado: " + cheers);
    }

    public static HashMap<Location, Location> getLocations(@NotNull Location pos1, Location pos2) {
        HashMap<Location, Location> locations = new HashMap<>();

        int x1 = pos1.getBlockX();
        int y1 = pos1.getBlockY();
        int z1 = pos1.getBlockZ();

        int x2 = pos2.getBlockX();
        int y2 = pos2.getBlockY();
        int z2 = pos2.getBlockZ();

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);

        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location location1 = new Location(pos1.getWorld(), x, y, z);
                    Location location2 = new Location(pos2.getWorld(), x, y, z);
                    locations.put(location1, location2);
                }
            }
        }

        return locations;
    }

    public static Material getMaterialForY(int y) {
        if (y >= 21) {
            return Material.AMETHYST_BLOCK;
        } else if (y >= 18) {
            return Material.DIAMOND_BLOCK;
        } else if (y >= 15) {
            return Material.GOLD_BLOCK;
        } else if (y >= 12) {
            return Material.IRON_BLOCK;
        } else {
            return Material.BEDROCK;
        }
    }

    public static void replaceBlocksInCube(Location pos1, Location pos2) {
        HashMap<Location, Location> locations = getLocations(pos1, pos2);
        for (Map.Entry<Location, Location> loc : locations.entrySet()) {
            Location key = loc.getKey();
            Location value = loc.getValue();
            Location location = obtenerUbicacionMenor(key, value);
            int blockY = location.getBlockY();
            Material materialForY = getMaterialForY(blockY);

            replace(key, value, materialForY, false);
        }
    }

    public static Location obtenerUbicacionMenor(Location pos1, Location pos2) {
        double distancia1 = pos1.distanceSquared(pos1.getWorld().getSpawnLocation());
        double distancia2 = pos2.distanceSquared(pos2.getWorld().getSpawnLocation());

        if (distancia1 < distancia2) {
            return pos1;
        } else {
            return pos2;
        }
    }

    public static boolean isFull(Location pos1, Location pos2) {
        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (pos1.getWorld().getBlockAt(x, y, z).getType() == Material.AIR) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static String percentage(Location pos1, Location pos2) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        World world = pos1.getWorld();
        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        int totalBlocks = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
        if (totalBlocks <= 0) {
            return "0.0%";
        }

        int filledBlocks = 0;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() != Material.AIR) {
                        filledBlocks++;
                    }
                }
            }
        }

        double percentage = (double) filledBlocks / totalBlocks * 100.0;
        // Formatear el porcentaje con un solo decimal
        String formattedPercentage = decimalFormat.format(percentage);

        return formattedPercentage + "%";
    }
}
