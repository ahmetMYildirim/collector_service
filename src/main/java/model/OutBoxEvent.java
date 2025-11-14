package model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * The type Out box event.
 */
@Entity
@Table(name = "outbox")
public class OutBoxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aggregateType;
    private String aggregateId;
    private String eventType;
    @Column(columnDefinition = "TEXT")
    private String payload;
    private LocalDateTime createdAt;

    @Column(name = "is_processed")
    private boolean processed = false;

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
     * Gets aggregate type.
     *
     * @return the aggregate type
     */
    public String getAggregateType() {
        return aggregateType;
    }

    /**
     * Sets aggregate type.
     *
     * @param aggregateType the aggregate type
     */
    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    /**
     * Gets aggregate ıd.
     *
     * @return the aggregate ıd
     */
    public String getAggregateId() {
        return aggregateId;
    }

    /**
     * Sets aggregate ıd.
     *
     * @param aggregateId the aggregate ıd
     */
    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    /**
     * Gets event type.
     *
     * @return the event type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Sets event type.
     *
     * @param eventType the event type
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Gets event data.
     *
     * @return the event data
     */
    public String getPayload() {
        return payload;
    }

    /**
     * Sets event data.
     *
     * @param payload the event data
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

    /**
     * Gets created at.
     *
     * @return the created at
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets created at.
     *
     * @param createdAt the created at
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Is processed boolean.
     *
     * @return the boolean
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * Sets processed.
     *
     * @param processed the processed
     */
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
