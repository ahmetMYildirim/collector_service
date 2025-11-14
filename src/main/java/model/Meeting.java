package model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * The type Meeting.
 */
@Entity
@Table(name = "meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String platform;
    private String meetingId; // Platform üzerindeki toplantı ID'si
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer participants; // Katılımcılar

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
     * Gets meeting ıd.
     *
     * @return the meeting ıd
     */
    public String getMeetingId() {
        return meetingId;
    }

    /**
     * Sets meeting ıd.
     *
     * @param meetingId the meeting ıd
     */
    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
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
     * Gets participants.
     *
     * @return the participants
     */
    public Integer getParticipants() {
        return participants;
    }

    /**
     * Sets participants.
     *
     * @param participants the participants
     */
    public void setParticipants(Integer participants) {
        this.participants = participants;
    }
}
