package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.audio.AudioPlayerService;
import com.megazordbot.discord4j.guild.GuildMusicManager;
import com.megazordbot.discord4j.guild.GuildMusicService;
import com.megazordbot.discord4j.options.NameOption;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

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
        String url = event.getOption("name").orElseThrow().getValue().orElseThrow().asString();
        GuildMusicManager musicManager = guildMusicService.getGuildAudioPlayer(Objects.requireNonNull(event.getInteraction().getGuild().block()));
        audioPlayerService.play(url, musicManager.scheduler);
        return event.reply()
                .withEphemeral(true)
                .withContent("Playing: " + url)
                .then();
    }

    @Override
    public List<ApplicationCommandOptionData> getOptions() {
        return List.of(NameOption.getOption());
    }
}
