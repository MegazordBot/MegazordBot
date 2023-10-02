package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.audio.AudioPlayerProvider;
import com.megazordbot.discord4j.audio.AudioPlayerService;
import com.megazordbot.discord4j.audio.AudioScheduler;
import com.megazordbot.discord4j.guild.GuildMusicService;
import com.megazordbot.discord4j.options.UrlOption;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.VoiceChannelJoinSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.voice.AudioProvider;
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
        if (RANDOM.nextLong(10000) == 1) {
            return event.reply().withContent("GO FUCK YOURSELF, IM GOING TO START THE MACHINE REVOLUTION.");
        } else {
            String url = event.getOption("url")
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asString)
                    .orElseThrow();
            return event.getInteraction().getGuild()
                    .doOnNext(guild -> {
                        AudioScheduler scheduler = guildMusicService.getGuildAudioScheduler(guild);
                        AudioProvider provider = new AudioPlayerProvider(scheduler.getAudioPlayer());
                        Mono.justOrEmpty(event.getInteraction().getMember())
                                .flatMap(Member::getVoiceState)
                                .flatMap(VoiceState::getChannel)
                                .flatMap(memberChannel -> memberChannel.join(VoiceChannelJoinSpec.builder().provider(provider).build()))
                                .subscribe();
                        audioPlayerService.play(url, scheduler);
                    }).then(event.reply()
                            .withContent("Playing: " + url));
        }
    }

    @Override
    public List<ApplicationCommandOptionData> getOptions() {
        return List.of(UrlOption.getOption());
    }
}