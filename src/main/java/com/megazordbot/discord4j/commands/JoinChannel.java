package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.audio.AudioPlayerProvider;
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

@Component
@RequiredArgsConstructor
public class JoinChannel implements SlashCommand {

    private final GuildMusicService guildMusicService;

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Join a channel";
    }

    //TODO refatora saporra
    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.getInteraction().getGuild().doOnNext(guild -> {
            GuildMusicManager musicManager = guildMusicService.getGuildAudioPlayer(guild);
            AudioProvider provider = new AudioPlayerProvider(musicManager.player);
            Mono.justOrEmpty(event.getInteraction().getMember())
                    .flatMap(Member::getVoiceState)
                    .flatMap(VoiceState::getChannel)
                    .flatMap(channel -> channel.join(VoiceChannelJoinSpec.builder().provider(provider).build())
                            .and(event.reply().withContent("Entering " + channel.getName())))
                    .subscribe();
        }).then();
    }
}
