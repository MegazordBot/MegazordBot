package com.megazordbot.discord4j.commands;

import com.megazordbot.discord4j.options.NumberOption;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class DiceRoll implements SlashCommand {

    @Override
    public String getName() {
        return "roll";
    }

    @Override
    public String getDescription() {
        return "Roll a dice";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        Long sides = event.getOption("number")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElseThrow();
        if (sides <= 0) {
            event.reply().withContent("Use a value bigger then 0");
        }
        return event.reply()
                .withContent(String.valueOf(RANDOM.nextLong(sides)+1));
    }

    @Override
    public List<ApplicationCommandOptionData> getOptions() {
        return List.of(NumberOption.getOption());
    }
}
