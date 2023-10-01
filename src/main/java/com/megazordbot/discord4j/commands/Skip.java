package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.guild.GuildMusicService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class Skip implements SlashCommand {

    private final GuildMusicService guildMusicService;

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skip";
    }


    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.getInteraction().getGuild()
                .doOnNext(guild -> {
                    guildMusicService.getGuildAudioScheduler(guild).startNextTrack(true);
                    event.reply().withContent("Skipped.").subscribe();
                }).then();
    }
}
