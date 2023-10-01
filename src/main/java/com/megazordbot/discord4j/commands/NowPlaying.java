package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.audio.AudioScheduler;
import com.megazordbot.discord4j.guild.GuildMusicService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class NowPlaying implements SlashCommand {

    private final GuildMusicService guildMusicService;

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getDescription() {
        return "Shows what music are playing.";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.getInteraction().getGuild()
                .doOnNext(guild -> {
                    AudioScheduler scheduler = guildMusicService.getGuildAudioScheduler(guild);
                    event.reply()
                            .withContent(scheduler.nowPlaying().toString()).subscribe();
                }).then();
    }
}
