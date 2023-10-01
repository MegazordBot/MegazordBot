package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.audio.AudioPlayerService;
import com.megazordbot.discord4j.guild.GuildMusicManager;
import com.megazordbot.discord4j.guild.GuildMusicService;
import com.megazordbot.discord4j.options.UrlOption;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Play implements SlashCommand {

    private final AudioPlayerService audioPlayerService;
    private final GuildMusicService guildMusicService;

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Play a music";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String url = event.getOption("url").orElseThrow().getValue().orElseThrow().asString();
        return event.getInteraction().getGuild()
                .doOnNext(guild -> {
                    GuildMusicManager musicManager = guildMusicService.getGuildAudioPlayer(guild);
                    audioPlayerService.play(url, musicManager.scheduler);
                }).then(event.reply()
                        .withContent("Playing: " + url));
    }

    @Override
    public List<ApplicationCommandOptionData> getOptions() {
        return List.of(UrlOption.getOption());
    }
}