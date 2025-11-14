package bot;

import model.AudioMessage;
import model.UserAudioData;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import service.AudioMessageService;


import java.io.File;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * The type Discord audio receiver.
 */
public class DiscordAudioReceiver implements AudioReceiveHandler {

    private final AudioMessageService messageService;
    private final Map<String, UserAudioData> userAudioMap = new HashMap<>();
    private String channleId;
    private volatile boolean isCleaningUp = false;


    /**
     * Instantiates a new Discord audio receiver.
     *
     * @param messageService the message service
     */
    public DiscordAudioReceiver(AudioMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public boolean canReceiveUser() {
        return true;
    }

    @Override
    public void handleUserAudio(UserAudio userAudio) {
        if (isCleaningUp) {
            System.out.println("[NEW CODE v2] Cleanup in progress, ignoring audio packet");
            return;
        }

        byte[] audioData = userAudio.getAudioData(1.0);
        String userId = userAudio.getUser().getId();
        String userName = userAudio.getUser().getName();

        try{
            new File("audio_storage").mkdirs();

            userAudioMap.computeIfAbsent(userId, k -> new UserAudioData(userId, userName));

            UserAudioData userAudioData = userAudioMap.get(userId);
            
            if (!userAudioData.isClosed()) {
                userAudioData.writeAudioData(audioData);
            }

        }catch(java.io.IOException e){

            String message = e.getMessage();
            if (message != null && (message.contains("Stream Closed") || message.contains("stream is already closed"))) {
                return;
            }
            throw new RuntimeException("Could not handle user audio", e);
        }catch(Exception e){
            throw new RuntimeException("Could not handle user audio", e);
        }
    }

    /**
     * Clean up.
     */
    public void cleanUp(){
        System.out.println("Converting starting...");
        
        isCleaningUp = true;
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for(UserAudioData userData : userAudioMap.values()){
            try{
                userData.closeOutputStream();
                convertToMp3(userData);
            }catch(Exception e){
                throw new RuntimeException("Could not convert user audio data", e);
            }
        }
        userAudioMap.clear();
        isCleaningUp = false;

    }

    private void convertToMp3(UserAudioData userData) throws Exception {
        File pcmFile = userData.getTempPcmData();

        if(!pcmFile.exists()||pcmFile.length()==0){
            System.out.printf("PCM file empty or doesn't exist!!");
            return;
        }

        String mp3FilePath = "audio_storage/audio_" + userData.getUserId() + "_" + System.currentTimeMillis() + ".mp3";

        ProcessBuilder processBuilder = new ProcessBuilder(
          "ffmpeg",
                "-f", "s16be",
                "-ar", "48000",
                "-ac", "2",
                "-i", pcmFile.getAbsolutePath(),
                "-y",
                "-b:a", "128k",
                mp3FilePath
        );

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        int exitVal = process.waitFor();

        if(exitVal != 0){
            System.out.println("Ffmpeg converted is failed with exitcode: " + exitVal + "for user: "  + userData.getUserId());
        }
        else{
            System.out.println("Ffmpeg converted is successful for user: " + userData.getUserId());

            saveAudioMessageDB(userData, mp3FilePath);
            if(pcmFile.delete()){
                System.out.println("PCM file deleted successfully! " + pcmFile.getName());
            }
        }
    }

    private void saveAudioMessageDB(UserAudioData userAudioData, String mp3FilePath) {
        try{
            AudioMessage audioMessage = new AudioMessage();
            audioMessage.setPlatform("Discord");
            audioMessage.setChannelId(this.channleId);
            audioMessage.setAuthor(userAudioData.getUserName());
            audioMessage.setAudioUrl(mp3FilePath);
            audioMessage.setTimestamp(LocalDateTime.now());
            audioMessage.setVoiceSessionId(userAudioData.getUserId());

            messageService.processAndSaveAudioMessage(audioMessage);

            System.out.println("Audio Message saved to database");
        }catch(Exception e){
            System.err.println("Failed to save audio message to database");
            e.printStackTrace();
        }
    }

    /**
     * Sets channle ıd.
     *
     * @param channleId the channle ıd
     */
    public void setChannleId(String channleId) {
        this.channleId = channleId;
    }
}
