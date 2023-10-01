package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.guild.GuildMusicService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Leave implements SlashCommand {

    private final GuildMusicService guildMusicService;

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Leave channel";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.getInteraction().getGuild()
                .doOnNext(guild -> guildMusicService.getGuildAudioScheduler(guild).stop())
                .then(Mono.justOrEmpty(event.getInteraction().getMember())
                        .flatMap(Member::getVoiceState)
                        .flatMap(VoiceState::getChannel)
                        .flatMap(channel -> channel.sendDisconnectVoiceState()
                                    .and(event.reply().withContent("wanna sex?")))
                        .then());
    }
}
