package model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * The type Audio message.
 */
@Entity
@Table(name = "audio_messages")
public class AudioMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String platform;
    private String channelId;
    private String author;
    private String audioUrl; // ses dosyasının URL'si
    @Column(columnDefinition = "TEXT")
    private String transcription; // transkript edilmiş metin
    private LocalDateTime timestamp;
    private String voiceSessionId; // İlgili sesli oturumun ID'si

    /**
     * Gets ıd.
     *
     * @return the ıd
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets ıd.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets platform.
     *
     * @return the platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Sets platform.
     *
     * @param platform the platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * Gets channel ıd.
     *
     * @return the channel ıd
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * Sets channel ıd.
     *
     * @param channelId the channel ıd
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets author.
     *
     * @param author the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets audio url.
     *
     * @return the audio url
     */
    public String getAudioUrl() {
        return audioUrl;
    }

    /**
     * Sets audio url.
     *
     * @param audioUrl the audio url
     */
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    /**
     * Gets transcription.
     *
     * @return the transcription
     */
    public String getTranscription() {
        return transcription;
    }

    /**
     * Sets transcription.
     *
     * @param transcription the transcription
     */
    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets voice session ıd.
     *
     * @return the voice session ıd
     */
    public String getVoiceSessionId() {
        return voiceSessionId;
    }

    /**
     * Sets voice session ıd.
     *
     * @param voiceSessionId the voice session ıd
     */
    public void setVoiceSessionId(String voiceSessionId) {
        this.voiceSessionId = voiceSessionId;
    }
}
