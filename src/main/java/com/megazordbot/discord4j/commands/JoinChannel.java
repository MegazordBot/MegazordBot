package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.audio.AudioPlayerProvider;
import com.megazordbot.discord4j.audio.AudioPlayerService;
import com.megazordbot.discord4j.guild.GuildMusicManager;
import com.megazordbot.discord4j.guild.GuildMusicService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.VoiceChannelJoinSpec;
import discord4j.voice.AudioProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JoinChannel implements SlashCommand {

    private final GuildMusicService guildMusicService;
    private final AudioPlayerService audioPlayerService;

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Join a channel";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        GuildMusicManager musicManager = guildMusicService.getGuildAudioPlayer(event.getInteraction().getGuild().blockOptional().orElseThrow());
        AudioProvider provider = new AudioPlayerProvider(musicManager.player);
        if (audioPlayerService.isInAVoiceChat(event)) {
            return event.reply().withEphemeral(true).withContent("Já estou em um canal de voz");
        }
        return Mono.justOrEmpty(event.getInteraction().getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(channel -> channel.join(VoiceChannelJoinSpec.builder().provider(provider).build())
                        .and(event.reply().withEphemeral(true).withContent("Entering " + channel.getName())))
                .then();
    }
}
