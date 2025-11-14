package model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Audio stream data.
 */
public class AudioStreamData {
    private final String meetingId;
    private final String sessionStartTime;
    private final ByteArrayOutputStream buffer;
    private int chunkCounter;
    private static final int CHUNK_SIZE = 4096 * 10;

    /**
     * Instantiates a new Audio stream data.
     *
     * @param meetingId the meeting id
     */
    public AudioStreamData(String meetingId) {
        this.meetingId = meetingId;
        this.sessionStartTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        this.buffer = new ByteArrayOutputStream(CHUNK_SIZE * 2);
        this.chunkCounter = 0;
    }

    /**
     * Gets buffer size.
     *
     * @return the buffer size
     */
    public int getBufferSize() {
        return buffer.size();
    }

    /**
     * Gets meeting id.
     *
     * @return the meeting id
     */
    public String getMeetingId() {
        return meetingId;
    }

    /**
     * Gets session start time.
     *
     * @return the session start time
     */
    public String getSessionStartTime() {
        return sessionStartTime;
    }

    /**
     * Gets chunk counter.
     *
     * @return the chunk counter
     */
    public int getChunkCounter() {
        return chunkCounter;
    }

    /**
     * Increment chunk counter.
     */
    public void incrementChunkCounter() {
        this.chunkCounter++;
    }

    /**
     * Gets chunk size threshold.
     *
     * @return the chunk size threshold
     */
    public static int getChunkSizeThreshold() {
        return CHUNK_SIZE;
    }

    /**
     * Append data.
     *
     * @param data the data
     */
    public void appendData(byte[] data) {
        try {
            buffer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get and clear buffer byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getAndClearBuffer() {
        byte[] data = buffer.toByteArray();
        buffer.reset();
        return data;
    }


}
