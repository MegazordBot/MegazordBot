package com.megazordbot.discord4j.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class AudioScheduler extends AudioEventAdapter implements AudioLoadResultHandler {

    private final BlockingDeque<AudioTrack> queue;
    private final AudioPlayer audioPlayer;

    public AudioScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.queue = new LinkedBlockingDeque<>();
    }

    public void addToQueue(AudioTrack audioTrack) {
        queue.addLast(audioTrack);
        startNextTrack(false);
    }


    public void startNextTrack(boolean forced) {
        if (audioPlayer.getPlayingTrack() == null || forced) {
            audioPlayer.playTrack(queue.pollFirst());
        }
    }

    public void stop() {
        queue.clear();
        audioPlayer.stopTrack();
    }

    public StringBuilder nowPlaying() {
        StringBuilder response = new StringBuilder();
        return response.append("Playing: ")
                .append(audioPlayer.getPlayingTrack().getInfo().title)
                .append(" - ")
                .append(audioPlayer.getPlayingTrack().getInfo().author);
    }

    public String getQueue() {
        StringBuilder response = new StringBuilder();
        AtomicInteger iterator = new AtomicInteger(1);
        if (queue.isEmpty() && audioPlayer.getPlayingTrack() == null) {
            return "Queue is empty.";
        }
        response.append(nowPlaying()).append("\n");
        queue.forEach(track ->
                response.append(iterator.getAndIncrement())
                        .append(" - ")
                        .append(track.getInfo().title)
                        .append(track.getInfo().author)
                        .append("\n"));
        return response.toString();
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            startNextTrack(true);
        }
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        addToQueue(audioTrack);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
    }

    @Override
    public void noMatches() {
    }

    @Override
    public void loadFailed(FriendlyException e) {
    }
}
