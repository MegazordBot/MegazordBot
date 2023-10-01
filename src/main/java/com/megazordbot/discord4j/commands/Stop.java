package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.guild.GuildMusicService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Stop implements SlashCommand {

    private final GuildMusicService guildMusicService;

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Stop a music";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.getInteraction().getGuild()
                .doOnNext(guild -> guildMusicService.getGuildAudioPlayer(guild).player.stopTrack()).then(event.reply()
                        .withContent("Music stopped and fuck u"));
    }

}
