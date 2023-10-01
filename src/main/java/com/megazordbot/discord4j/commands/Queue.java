package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.audio.AudioScheduler;
import com.megazordbot.discord4j.guild.GuildMusicService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Queue implements SlashCommand {

    private final GuildMusicService guildMusicService;

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "Queue";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.getInteraction().getGuild()
                .doOnNext(guild -> {
                    AudioScheduler scheduler = guildMusicService.getGuildAudioScheduler(guild);
                    event.reply()
                            .withContent(scheduler.getQueue()).subscribe();
                }).then();
    }
}
