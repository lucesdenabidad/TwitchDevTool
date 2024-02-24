package cc.datta.twitchdevtool;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;

import static cc.datta.twitchdevtool.utils.ColorClass.format;

@CommandPermission("op")
@CommandAlias("twitchtool|twitchdev|dev|twitch")
public class TwitchToolCommand extends BaseCommand {

    @Subcommand("config reload")
    public void configReload(CommandSender sender){
        sender.sendMessage(format("&aConfiguracion recargada."));
        TwitchDevTool.getInstance().getConfig().reload();
    }
}
