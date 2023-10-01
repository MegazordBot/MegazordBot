package com.megazordbot.discord4j.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.MessageCreateFields;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Log4j2
public class Shrek implements SlashCommand {

    @Override
    public String getName() {
        return "shrek";
    }

    @Override
    public String getDescription() {
        return "Shrek";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        int shrekNumber = ThreadLocalRandom.current().nextInt(1, 8);
        log.info("shrek number: " + shrekNumber);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return event.reply()
                .withFiles(MessageCreateFields.File.of("shrek.jpg", Objects.requireNonNull(classLoader.getResourceAsStream("dontOpen/shrek-"+shrekNumber+".jpg"))));
    }
}