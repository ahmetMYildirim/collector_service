package bot.discord;

import bot.DiscordAudioReceiver;
import model.Message;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import service.AudioMessageService;
import service.MessageService;
import service.VoiceSessionService;
import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;

/**
 * The type Discord bot listener.
 */
@Component
@ConditionalOnProperty(name = "discord.bot.enabled", havingValue = "true", matchIfMissing = false)
public class DiscordBotListener extends ListenerAdapter {

    @Value("${discord.bot.token}")
    private String token;

    private final MessageService messageService;
    private final VoiceSessionService voiceSessionService;
    private final AudioMessageService audioMessageService;
    private final DiscordAudioReceiver discordAudioReceiver;

    /**
     * Instantiates a new Discord bot listener.
     *
     * @param messageService      the message service
     * @param voiceSessionService the voice session service
     * @param audioMessageService the audio message service
     */
    public DiscordBotListener(MessageService messageService, VoiceSessionService voiceSessionService, service.AudioMessageService audioMessageService) {
        this.messageService = messageService;
        this.voiceSessionService = voiceSessionService;
        this.audioMessageService = audioMessageService;
        this.discordAudioReceiver = new DiscordAudioReceiver(audioMessageService);
    }


    /**
     * Start bot.
     */
    @PostConstruct
    public void startBot() {
        try{
            JDABuilder.createDefault(token)
                    .enableIntents(
                            GatewayIntent.MESSAGE_CONTENT,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_VOICE_STATES
                    )
                    .addEventListeners(this)
                    .build();
            System.out.println("Discord Bot started...");
        }catch (Exception e) {
            throw new RuntimeException("Discord not started: " + e.getMessage());
        }
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;

        String author = event.getAuthor().getName();
        String content = event.getMessage().getContentRaw();
        String platform = "Discord";

        if(content.equalsIgnoreCase("!join")){
            joinVoiceChannel(event);
            return;
        } else if(content.equalsIgnoreCase("!leave")){
            leftVoiceChannel(event);
            return;
        }

        Message message = new Message();
        message.setPlatform(platform);
        message.setAuthor(author);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        messageService.processAndSaveMessage(message);

        System.out.printf("Message received in channel %s from %s: %s%n", platform, author, content);
    }

    private void joinVoiceChannel(MessageReceivedEvent event) {
        Member member = event.getMember();
        if(member == null) return;

        var voiceState = member.getVoiceState();
        if(voiceState == null || !voiceState.inAudioChannel()){
            event.getChannel().sendMessage("You need to be in a voice channel for me to join!").queue();
            return;
        }

        var audioChannel = voiceState.getChannel();
        var audioManager = event.getGuild().getAudioManager();

        this.discordAudioReceiver.setChannleId(audioChannel.getId());
        audioManager.openAudioConnection(audioChannel);
        audioManager.setReceivingHandler(this.discordAudioReceiver);

        event.getChannel().sendMessage("I joined the voice channel! Conversations are being recorded!!!" + audioChannel.getId()).queue();
        System.out.println("Joined voice channel: " + audioChannel.getName() + " ID: " + audioChannel.getId());
    }

    private void leftVoiceChannel(MessageReceivedEvent event) {
        var audioManager = event.getGuild().getAudioManager();
        if(audioManager.isConnected()){
            this.discordAudioReceiver.cleanUp();
            audioManager.closeAudioConnection();
            event.getChannel().sendMessage("I left the voice channel!").queue();
            System.out.println("Left voice channel");
        } else {
            event.getChannel().sendMessage("I'm not in a voice channel!").queue();
        }
    }
}
