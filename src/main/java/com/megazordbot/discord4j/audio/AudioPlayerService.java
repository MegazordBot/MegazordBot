package com.megazordbot.discord4j.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Service;

@Service
public class AudioPlayerService extends DefaultAudioPlayerManager {

    public AudioPlayerService() {
        getConfiguration()
                .setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(this);
    }

    public void play(String url, TrackScheduler scheduler) {
        loadItem(url, scheduler);
    }

    public boolean isInAVoiceChat(ChatInputInteractionEvent event) {
        return event.getClient().getSelfMember(
                event.getInteraction()
                        .getMember()
                        .orElseThrow()
                        .getGuild().blockOptional().orElseThrow().getId())
                .blockOptional().orElseThrow().getVoiceState().blockOptional().isPresent();
    }

}
