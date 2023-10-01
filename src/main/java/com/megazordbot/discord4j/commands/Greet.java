package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.options.EphemeralOption;
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
        return List.of(NameOption.getOption(), EphemeralOption.getOption());
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String name = event.getOption("name")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);
        Boolean ephemeral = event.getOption("anonymous")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asBoolean)
                .orElse(false);

        return event.reply()
                .withEphemeral(ephemeral)
                .withContent("Hello, " + name);
    }
}
