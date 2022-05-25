package com.megazordbot.discord4j.options;

import discord4j.discordjson.json.ApplicationCommandOptionData;

public class NameOption {

    private NameOption() {}

    public static ApplicationCommandOptionData getOption() {
        return ApplicationCommandOptionData.builder().type(3).name("name").description("Your name").required(true).build();
    }

}
