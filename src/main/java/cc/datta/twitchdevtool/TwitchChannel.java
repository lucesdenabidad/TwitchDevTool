package cc.datta.twitchdevtool;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.ITwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;

import static cc.datta.twitchdevtool.utils.ColorClass.format;

public class TwitchChannel implements Listener {

    @Getter
    private OfflinePlayer offlinePlayer;

    @Getter
    private String oauthToken;
    @Getter
    private String clientId;
    @Getter
    private String clientSecret;
    @Getter
    private ArrayList<String> channels;
    @Getter
    private OAuth2Credential credential;
    @Getter
    private ITwitchClient client;
    @Getter
    private String prefix;

    @Getter
    @Setter
    public boolean sendChat;

    public TwitchChannel(String oauthToken, String clientId, String clientSecret, String channel, String streamerName, String prefix, boolean sendChat) {
        this.oauthToken = oauthToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.channels = new ArrayList<>();
        this.channels.add(channel);
        this.credential = StringUtils.isNotBlank(this.oauthToken) ? new OAuth2Credential("twitch", this.oauthToken) : null;
        this.client = TwitchClientBuilder.builder()
                .withClientId(this.clientId)
                .withClientSecret(this.clientSecret)
                .withEnableChat(true)
                .withChatAccount(credential)
                .withEnableHelix(true)
                .withDefaultAuthToken(credential)
                .withChatCommandsViaHelix(false)
                .build();
        this.offlinePlayer = Bukkit.getOfflinePlayer(streamerName);
        this.prefix = prefix;
        this.sendChat = sendChat;

        if (!this.channels.isEmpty()) {
            for (String name : this.channels) {
                this.client.getChat().joinChannel(name);
            }

            this.client.getClientHelper().enableStreamEventListener(this.channels);
            this.client.getClientHelper().enableFollowEventListener(this.channels);
        }

        this.client.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(this);

    }

    public void disable() {
        this.client.getChat().leaveChannel(channels.get(0));
        this.client.getClientHelper().disableStreamEventListener(this.channels);
        this.client.getClientHelper().disableFollowEventListener(this.channels);

    }

    @EventSubscriber
    public void onChatMessage(ChannelMessageEvent event) {
        String username = event.getUser().getName();
        String message = event.getMessage();

        if (sendChat) {
            OfflinePlayer offlinePlayer = getOfflinePlayer();
            if (offlinePlayer.isOnline()) {
                if (message.length() > 20) {
                    message = message.substring(0, 20);
                }

                Player player = offlinePlayer.getPlayer();
                player.sendMessage(format(getPrefix() + " &8> &f{0}&7: &f{1}", new Object[]{username, message}));

            }
        }

        if (TwitchOperator.sendChat) {
            OfflinePlayer operatorof = TwitchOperator.offlinePlayer;
            if (operatorof.isOnline()) {
                Player operator = operatorof.getPlayer();
                operator.sendMessage(format("&7[Operador] " + getPrefix() + " &8> &f{0}&7: &f{1}", new Object[]{username, message}));
            }
        }
    }
}