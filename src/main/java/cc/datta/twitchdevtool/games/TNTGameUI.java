package cc.datta.twitchdevtool.games;

import cc.datta.twitchdevtool.TwitchChannel;
import cc.datta.twitchdevtool.TwitchDevTool;
import cc.datta.twitchdevtool.TwitchOperator;
import cc.datta.twitchdevtool.utils.menus.MenuUtils;
import cc.datta.twitchdevtool.utils.menus.menubuilder.MenuItemBuilder;
import cc.datta.twitchdevtool.utils.tools.WorldEditTool;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.Map.Entry;

import static cc.datta.twitchdevtool.TwitchDevTool.menuManager;
import static cc.datta.twitchdevtool.utils.ColorClass.*;
import static cc.datta.twitchdevtool.utils.menus.MenuUtils.slot;

public class TNTGameUI extends BaseCommand {

    @CommandAlias("tntgamegui")
    public void menu(Player player) {
        if (!playerIsStreamer(player)) {
            admimenu(player);
            return;
        }

        TwitchChannel twitchChannel = playerGetStreamer(player);

        menuManager.createMenu(player, "Propiedades de " + twitchChannel.getChannels().get(0), 9 * 5, false);
        menuManager.setContents(player, () -> {
            menuManager.setItem(player, slot(2, 2), new MenuItemBuilder(Material.LEVER, "&6Envio de mensajes &7(sendchat boolean)",
                    "&f",
                    "&fsendChat: &e" + twitchChannel.sendChat,
                    "&f", "&aClic para alternar.").build(), () -> {

                togglesendchat(twitchChannel);

                player.sendMessage(format("&#8934d9( ! ) &fAlternaste el recibir mensajes de su chat. &8(" + twitchChannel.sendChat + ")"));
            });

            menuManager.setItem(player, slot(3, 2), new MenuItemBuilder(Material.REPEATING_COMMAND_BLOCK, "&eDonaciones",
                    "&f",
                    "&fAbre el menú de donaciones",
                    "&faqui puedes probar las",
                    "&fdonaciones establecidas.",
                    "&f", "&aClic para abrir.").build(), () -> {

                submenuconfig(player, twitchChannel);
            });


            menuManager.setItem(player, slot(2, 3), new MenuItemBuilder(Material.WOODEN_AXE, "&bRellenar cubo",
                    "&8Metodo de testeo",
                    "&f",
                    "&fRellena el cubo por completo.",
                    "&f", "&aClic para rellenar.").build(), () -> {

                TNTGame tntGame = TwitchDevTool.getInstance().games.get(player.getName());
                if (tntGame == null) {
                    player.sendMessage("tntgame null");
                    return;
                }

                WorldEditTool.fill(tntGame.pos1, tntGame.pos2, Material.IRON_BLOCK);
                player.sendMessage(format("&#8934d9( ! ) &fRellenaste tu cubo con éxito."));
            });

        });
    }


    public void submenuconfig(Player player, TwitchChannel channel) {
        TNTGame tntGameWithTwitchChannel = TwitchDevTool.getInstance().getTntGameWithTwitchChannel(channel);
        List<TntGameDonations> donations = tntGameWithTwitchChannel.donationsList;
        menuManager.createMenu(player, "Configuración > Donaciones", 27, false);
        menuManager.setContents(player, () -> {

            Map<Material, List<Integer>> map = new LinkedHashMap();
            Iterator var2 = donations.iterator();

            while (var2.hasNext()) {
                TntGameDonations donation = (TntGameDonations) var2.next();
                map.computeIfAbsent(donation.getMaterial(), (k) -> {
                    return new ArrayList<>();
                }).add(donation.getBits());
            }

            int SLOT = 2;

            for (Entry<Material, List<Integer>> materialListEntry : map.entrySet()) {
                Material material = materialListEntry.getKey();
                List<Integer> bitsValues = materialListEntry.getValue();

                for (Iterator<Integer> var7 = bitsValues.iterator(); var7.hasNext(); ++SLOT) {
                    int bitsValue = var7.next();
                    menuManager.setItem(player, MenuUtils.slot(SLOT, 1), (new MenuItemBuilder(material, "&eMandar " + bitsValue + " bits", "&f", "&fManda la animación de los " + bitsValue + " bits", "&f", "&aClic para solicitar.")).build(), () -> {
                        TNTGameConst.sendCheers(channel, donations, player.getName(), bitsValue);
                    });
                }
            }

            menuManager.setItem(player, MenuUtils.slot(1, 1), (new MenuItemBuilder(Material.BARRIER, "&cVolver al menu", "&f", "&fAbre el menu principal de configuraciones.", "&f", "&aClic para abrir.")).build(), () -> {
                menu(player);
            });
        });
    }

    @CommandAlias("adminmenu")
    public void admimenu(Player player) {
        menuManager.createMenu(player, "Propiedades del servidor", 9 * 5, false);
        menuManager.setContents(player, () -> {

            menuManager.setItem(player, slot(2, 2), new MenuItemBuilder(Material.LEVER, "&6Envio de mensajes de chat &7(administrador)",
                    "&f",
                    "&fsendChat: &e" + TwitchOperator.sendChat,
                    "&f", "&aClic para alternar.").build(), () -> {

                boolean sendChat = TwitchOperator.sendChat;
                TwitchOperator.sendChat = !sendChat;

                player.sendMessage(format("&#8934d9( ! ) &fAlternaste el recibir mensajes de los chat. &8(" + sendChat + ")"));
            });

            menuManager.setItem(player, slot(3, 2), new MenuItemBuilder(Material.ENDER_EYE, "&eTeletransportar",
                    "&f",
                    "&fTeletransportar",
                    "&f", "&aClic para alternar.").build(), () -> {

                HashMap<String, TNTGame> games = TwitchDevTool.getInstance().games;
                for (Entry<String, TNTGame> materialListEntry : games.entrySet()) {
                    String key = materialListEntry.getKey();
                    TNTGame value = materialListEntry.getValue();
                    OfflinePlayer player1 = value.player;
                    if (player1 != null && player1.isOnline()) {
                        Player player2 = player1.getPlayer();
                        player2.teleport(centerLocations(value.pos1, value.pos2).toCenterLocation());
                        player2.setGameMode(GameMode.CREATIVE);
                        player2.getInventory().addItem(new ItemStack(Material.IRON_BLOCK));
                    }
                }
            });
        });
    }

    public void togglesendchat(TwitchChannel twitchChannel){
        boolean sendChat = twitchChannel.sendChat;
        twitchChannel.sendChat = !sendChat;
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.hasBlock()) {
            Block clickedBlock = event.getClickedBlock();
            Location location = clickedBlock.getLocation();
            if (location.equals(stringToLocation("6 13 -3"))) {
                menu(player);
                event.setCancelled(true);
            }
        }
    }

    public  static List<TwitchChannel> list = List.of(TwitchDevTool.getInstance().soarinng.twitchChannel, TwitchDevTool.getInstance().aquino.twitchChannel);

    public static boolean playerIsStreamer(Player player) {
        for (TwitchChannel twitchChannel : list) {
            boolean b = twitchChannel.getOfflinePlayer().getName().equalsIgnoreCase(player.getName());
            if (b) {
                return true;
            }
        }
        return false;
    }

    public static TwitchChannel playerGetStreamer(Player player) {
        for (TwitchChannel twitchChannel : list) {
            if (twitchChannel.getOfflinePlayer().getName().equalsIgnoreCase(player.getName())) {
                return twitchChannel;
            }
        }
        return null; // O puedes lanzar una excepción si prefieres manejar el caso de no encontrar al jugador.
    }
}