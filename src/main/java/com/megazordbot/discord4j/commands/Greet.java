package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.options.NameOption;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class Greet implements SlashCommand {

    @Override
    public String getName() {
        return "greet";
    }

    @Override
    public String getDescription() {
        return "Greets you";
    }

    @Override
    public List<ApplicationCommandOptionData> getOptions() {
        return List.of(NameOption.getOption());
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String name = event.getOption("name")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        return event.reply()
                .withEphemeral(true)
                .withContent("Hello, " + name);
    }
}
