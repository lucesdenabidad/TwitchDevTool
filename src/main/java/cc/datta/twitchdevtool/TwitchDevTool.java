package cc.datta.twitchdevtool;

import cc.datta.twitchdevtool.channels.Aquino;
import cc.datta.twitchdevtool.channels.Soarinng;
import cc.datta.twitchdevtool.events.Messages;
import cc.datta.twitchdevtool.games.TNTGame;
import cc.datta.twitchdevtool.games.TNTGameCommand;
import cc.datta.twitchdevtool.games.TNTGameUI;
import cc.datta.twitchdevtool.games.TntGameDonations;
import cc.datta.twitchdevtool.utils.configuration.Configuration;
import cc.datta.twitchdevtool.utils.configuration.ConfigurationManager;
import cc.datta.twitchdevtool.utils.menus.MenuManager;
import co.aikar.commands.BukkitCommandManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LightningStrike;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;

import static cc.datta.twitchdevtool.games.TNTGameConst.tnt;
import static cc.datta.twitchdevtool.utils.ColorClass.centerLocations;
import static cc.datta.twitchdevtool.utils.ColorClass.stringToLocation;
import static cc.datta.twitchdevtool.utils.tools.WorldEditTool.fill;

public final class TwitchDevTool extends JavaPlugin {

    @Getter
    public static TwitchDevTool instance;

    @Getter
    public ConfigurationManager manager = new ConfigurationManager(this);
    @Getter
    public Configuration config = manager.getConfig("config.yml");

    public static MenuManager menuManager;
    public Soarinng soarinng;
    public Aquino aquino;

    public TNTGame soarinngtntgame;
    public TNTGame aquinotntgame;

    public TwitchPlaceholderExtension extension;
    public HashMap<String, TNTGame> games;

    @Override
    public void onEnable() {
        instance = this;
        menuManager = new MenuManager(this);
        extension = new TwitchPlaceholderExtension();
        games = new HashMap<>();

        soarinng = new Soarinng(this);
        aquino = new Aquino(this);

        soarinngtntgame = new TNTGame(soarinng.twitchChannel, new Location[]{stringToLocation("27 23 -15"), stringToLocation("12 12 1")}, soarinngList);
        aquinotntgame = new TNTGame(aquino.twitchChannel, new Location[]{stringToLocation("-15 23 1"), stringToLocation("0 12 -15")}, aquinoList);

        soarinngtntgame.startTask();
        aquinotntgame.startTask();

        games.put(soarinng.twitchChannel.getOfflinePlayer().getName(), soarinngtntgame);
        games.put(aquino.twitchChannel.getOfflinePlayer().getName(), aquinotntgame);


        BukkitCommandManager bukkitmanager = new BukkitCommandManager(this);
        bukkitmanager.registerCommand(new TwitchToolCommand());
        bukkitmanager.registerCommand(new TNTGameCommand());

        bukkitmanager.registerCommand(new TNTGameUI());
        Bukkit.getPluginManager().registerEvents(new Messages(), this);

        extension.register();
    }

    public TNTGame getTntGameWithTwitchChannel(TwitchChannel channel) {
        String name = channel.getOfflinePlayer().getName();

        if (games.containsKey(name)) {
            return games.get(name);
        } else {
            return null;
        }
    }

    public List<TntGameDonations> aquinoList = List.of(
//            new TntGameDonations(Material.PAPER, 40, "te ha tirado una TNT", (player) -> {
//                tnt(player.getLocation());
//            }),

            new TntGameDonations(Material.PAPER, 100, "te ha tirado 3 TNTs", (player) -> {
                for (int i = 0; i < 3; ++i) {
                    tnt(player.getLocation());
                }
            }),

            new TntGameDonations(Material.PAPER, 500, "te ha tirado 20 TNT", (player) -> {
                for (int i = 0; i < 30; ++i) {
                    tnt(player.getLocation());
                }
            }),

            new TntGameDonations(Material.PAPER, 1000, "te ha tirado un meteorito", (player) -> {
                Fireball fireball = player.getWorld().spawn(player.getLocation().add(0.0D, 10.0D, 0.0D), Fireball.class);
                Vector direction = (new Vector(0, -1, 0)).multiply(1.5D);
                fireball.setDirection(direction);
                fireball.setIsIncendiary(false);
                fireball.setYield(20.0F);

                for (int i = 0; i < 40; ++i) {
                    tnt(player.getLocation());
                }
            }),

            new TntGameDonations(Material.PAPER, 2000, "te ha impactado con truenos explosivos", (player) -> {
                LightningStrike lightningStrike = (LightningStrike) player.getWorld().spawnEntity(player.getLocation(), EntityType.LIGHTNING);
                lightningStrike.setVisualFire(false);

                for (int i = 0; i < 50; ++i) {
                    tnt(player.getLocation());
                }
            }),

            new TntGameDonations(Material.PAPER, 3500, "te lanzo 100 tnts", (player) -> {
                for (int i = 0; i < 100; ++i) {
                    tnt(player.getLocation());
                }
            }),

            new TntGameDonations(Material.PAPER, 10000, "te lanzo una bomba atomica", (player) -> {
                Location location = centerLocations(aquinotntgame.pos1, aquinotntgame.pos2);

                fill(aquinotntgame.pos1.clone().subtract(0, 1, 0), aquinotntgame.pos2, Material.TNT);
                fill(location, location, Material.AIR);
                tnt(centerLocations(aquinotntgame.pos1, aquinotntgame.pos2));
            }

            ));

        public List<TntGameDonations> soarinngList = List.of(
//            new TntGameDonations(Material.PAPER, 20, "te ha tirado una TNT", (player) -> {
//                tnt(player.getLocation());
//            }),

            new TntGameDonations(Material.PAPER, 50, "te ha tirado 3 TNTs", (player) -> {
                for (int i = 0; i < 3; ++i) {
                    tnt(player.getLocation());
                }
            }),

            new TntGameDonations(Material.PAPER, 250, "te ha tirado 20 TNT", (player) -> {
                for (int i = 0; i < 30; ++i) {
                    tnt(player.getLocation());
                }
            }),

            new TntGameDonations(Material.PAPER, 500, "te ha tirado un meteorito", (player) -> {
                Fireball fireball = player.getWorld().spawn(player.getLocation().add(0.0D, 10.0D, 0.0D), Fireball.class);
                Vector direction = (new Vector(0, -1, 0)).multiply(1.5D);
                fireball.setDirection(direction);
                fireball.setIsIncendiary(false);
                fireball.setYield(20.0F);

                for (int i = 0; i < 40; ++i) {
                    tnt(player.getLocation());
                }
            }),

            new TntGameDonations(Material.PAPER, 1000, "te ha impactado con truenos explosivos", (player) -> {
                LightningStrike lightningStrike = (LightningStrike) player.getWorld().spawnEntity(player.getLocation(), EntityType.LIGHTNING);
                lightningStrike.setVisualFire(false);

                for (int i = 0; i < 50; ++i) {
                    tnt(player.getLocation());
                }
            }),

            new TntGameDonations(Material.PAPER, 2000, "te lanzo 100 tnts", (player) -> {
                for (int i = 0; i < 100; ++i) {
                    tnt(player.getLocation());
                }
            }),

            new TntGameDonations(Material.PAPER, 10000, "te lanzo una bomba atomica", (player) -> {
                Location location = centerLocations(aquinotntgame.pos1, aquinotntgame.pos2);

                fill(aquinotntgame.pos1.clone().subtract(0, 1, 0), aquinotntgame.pos2, Material.TNT);
                fill(location, location, Material.AIR);
                tnt(centerLocations(aquinotntgame.pos1, aquinotntgame.pos2));
            }

            ));


    @Override
    public void onDisable() {
        soarinng.twitchChannel.disable();
        aquino.twitchChannel.disable();

        extension.unregister();
    }
}
