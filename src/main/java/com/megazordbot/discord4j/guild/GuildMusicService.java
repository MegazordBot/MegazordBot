package com.megazordbot.discord4j.guild;

import com.megazordbot.discord4j.audio.AudioPlayerService;
import com.megazordbot.discord4j.audio.AudioScheduler;
import discord4j.core.object.entity.Guild;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GuildMusicService {

    private final AudioPlayerService audioPlayerService;
    private final Map<Long, AudioScheduler> musicManagers;

    public GuildMusicService(AudioPlayerService audioPlayerService) {
        this.audioPlayerService = audioPlayerService;
        musicManagers = new HashMap<>();
    }

    public synchronized AudioScheduler getGuildAudioScheduler(Guild guild) {
        long guildId = guild.getId().asLong();
        return musicManagers.getOrDefault(guildId, createAudioScheduler(guildId));
    }

    private AudioScheduler createAudioScheduler(long guildId) {
        AudioScheduler audioScheduler = new AudioScheduler(audioPlayerService.createPlayer());
        musicManagers.put(guildId, audioScheduler);
        return audioScheduler;
    }



}
