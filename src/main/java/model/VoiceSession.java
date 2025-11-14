package model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * The type Voice session.
 */
@Entity
@Table(name = "voice_sessions")
public class VoiceSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String platform;
    private String channelId;
    private String channelName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer participantCount;

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
     * Gets channel name.
     *
     * @return the channel name
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * Sets channel name.
     *
     * @param channelName the channel name
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets end time.
     *
     * @return the end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets end time.
     *
     * @param endTime the end time
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets participant count.
     *
     * @return the participant count
     */
    public Integer getParticipantCount() {
        return participantCount;
    }

    /**
     * Sets participant count.
     *
     * @param participantCount the participant count
     */
    public void setParticipantCount(Integer participantCount) {
        this.participantCount = participantCount;
    }
}
