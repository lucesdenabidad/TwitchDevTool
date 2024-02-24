package cc.datta.twitchdevtool.games;

import cc.datta.twitchdevtool.TwitchDevTool;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;

@CommandAlias("tntgame")
public class TNTGameCommand extends BaseCommand {

    @CommandCompletion("soarinng|aquino")
    @Subcommand("test")
    public void test(String streamer, CommandSender sender, int cheers) {
        if (streamer.contains("soarinng")) {
            TNTGameConst.sendCheers(TwitchDevTool.getInstance().soarinng.twitchChannel, TwitchDevTool.getInstance().soarinngList, sender.getName(), cheers);
        }
        if (streamer.contains("aquino")) {
            TNTGameConst.sendCheers(TwitchDevTool.getInstance().aquino.twitchChannel, TwitchDevTool.getInstance().aquinoList, sender.getName(), cheers);
        }
    }
}