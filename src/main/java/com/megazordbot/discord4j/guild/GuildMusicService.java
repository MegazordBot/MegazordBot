package com.megazordbot.discord4j.guild;

import com.megazordbot.discord4j.audio.AudioPlayerService;
import discord4j.core.object.entity.Guild;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GuildMusicService {

    private final AudioPlayerService audioPlayerService;
    private final Map<Long, GuildMusicManager> musicManagers;

    public GuildMusicService(AudioPlayerService audioPlayerService) {
        this.audioPlayerService = audioPlayerService;
        musicManagers = new HashMap<>();
    }

    public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = guild.getId().asLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(audioPlayerService);
            musicManagers.put(guildId, musicManager);
        }

        return musicManager;
    }

}
