package com.megazordbot.discord4j.audio;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import org.springframework.stereotype.Service;

@Service
public class AudioPlayerService extends DefaultAudioPlayerManager {

    public AudioPlayerService() {
        getConfiguration()
                .setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(this);
    }

    public void play(String url, AudioScheduler scheduler) {
        loadItem(url, scheduler);
    }

}
