package com.megazordbot.discord4j.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface SlashCommand {

    String getName();

    String getDescription();

    Mono<Void> handle(ChatInputInteractionEvent event);

    List<ApplicationCommandOptionData> getOptions();
}
