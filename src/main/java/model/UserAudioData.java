package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The type User audio data.
 */
public class UserAudioData {
    private String userId;
    private String userName;
    private File tempPcmData;
    private FileOutputStream outputStream;
    private volatile boolean isClosed = false;


    /**
     * Instantiates a new User audio data.
     *
     * @param userId   the user ıd
     * @param userName the user name
     */
    public UserAudioData(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.tempPcmData = new File("audio_storage/temp_" + userId + ".pcm");

        try{
            if(tempPcmData.exists()){
                tempPcmData.delete();
            }
            this.outputStream = new FileOutputStream(tempPcmData,true);
        }catch(Exception e){
            throw new RuntimeException("Could not create output stream for user: " + userName, e);
        }
    }

    /**
     * Gets user ıd.
     *
     * @return the user ıd
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets user ıd.
     *
     * @param userId the user ıd
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets temp pcm data.
     *
     * @return the temp pcm data
     */
    public File getTempPcmData() {
        return tempPcmData;
    }

    /**
     * Sets temp pcm data.
     *
     * @param tempPcmData the temp pcm data
     */
    public void setTempPcmData(File tempPcmData) {
        this.tempPcmData = tempPcmData;
    }

    /**
     * Gets output stream.
     *
     * @return the output stream
     */
    public FileOutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Close output stream.
     *
     * @throws IOException the ıo exception
     */
    public synchronized void closeOutputStream() throws IOException {
        if(outputStream != null && !isClosed){
            isClosed = true;
            outputStream.flush();
            outputStream.close();
        }
    }

    /**
     * Is closed boolean.
     *
     * @return the boolean
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * Write audio data.
     *
     * @param data the data
     * @throws IOException the io exception
     */
    public synchronized void writeAudioData(byte[] data) throws IOException {
        if (isClosed) {
            throw new IOException("Output stream is already closed");
        }
        if (outputStream != null) {
            outputStream.write(data);
            outputStream.flush();
        }
    }
}
