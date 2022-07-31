package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.guild.GuildMusicManager;
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
        GuildMusicManager guildMusicManager = guildMusicService.getGuildAudioPlayer(event.getInteraction().getGuild().block());
        guildMusicManager.player.stopTrack();
        return event.reply()
                .withEphemeral(true)
                .withContent("VAI TOMA NO CU")
                .then();
    }

}
