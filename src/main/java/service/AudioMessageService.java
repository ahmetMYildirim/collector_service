package service;

import model.AudioMessage;
import model.OutBoxEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AudioMessageRepository;
import repository.OutBoxEventRepository;

import java.time.LocalDateTime;

/**
 * The type Audio message service.
 */
@Service
public class AudioMessageService {
    private final AudioMessageRepository audioMessageRepository;
    private final OutBoxEventRepository outBoxEventRepository;
    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new Audio message service.
     *
     * @param audioMessageRepository the audio message repository
     * @param outBoxEventRepository  the out box event repository
     * @param objectMapper           the object mapper
     */
    public AudioMessageService(AudioMessageRepository audioMessageRepository, OutBoxEventRepository outBoxEventRepository, ObjectMapper objectMapper) {
        this.audioMessageRepository = audioMessageRepository;
        this.outBoxEventRepository = outBoxEventRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Process and save audio message.
     *
     * @param audioMessage the audio message
     */
    @Transactional
    public void processAndSaveAudioMessage(AudioMessage audioMessage) {
        AudioMessage savedAudioMessage = audioMessageRepository.save(audioMessage);

        try{
            String payload = objectMapper.writeValueAsString(savedAudioMessage);
            OutBoxEvent outBoxEvent = new OutBoxEvent();
            outBoxEvent.setAggregateType("AudioMessage");
            outBoxEvent.setAggregateId(savedAudioMessage.getId().toString());
            outBoxEvent.setEventType("AudioMessageCreated");
            outBoxEvent.setPayload(payload);
            outBoxEvent.setCreatedAt(LocalDateTime.now());

            outBoxEventRepository.save(outBoxEvent);
        }catch (Exception e){
            throw new RuntimeException("Failed to create OutBoxEvent: " + e.getMessage());
        }
    }

    /**
     * Update audio message.
     *
     * @param audioMessage the audio message
     */
    @Transactional
    public void updateAudioMessage(AudioMessage audioMessage) {
        audioMessageRepository.findById(audioMessage.getId()).ifPresent(a -> {
            audioMessage.setTranscription(audioMessage.getTranscription());
            audioMessage.setAudioUrl(audioMessage.getAudioUrl());

            try{
                String payload = objectMapper.writeValueAsString(audioMessage);
                OutBoxEvent outBoxEvent = new OutBoxEvent();
                outBoxEvent.setAggregateType("AudioMessage");
                outBoxEvent.setAggregateId(audioMessage.getId().toString());
                outBoxEvent.setEventType("AudioMessageUpdated");
                outBoxEvent.setPayload(payload);
                outBoxEvent.setCreatedAt(LocalDateTime.now());

                outBoxEventRepository.save(outBoxEvent);
            }catch (Exception e){
                throw new RuntimeException("Failed to create OutBoxEvent: " + e.getMessage());
            }
        });
    }

    /**
     * Process zoom recording.
     *
     * @param meetingId the meeting id
     * @param filePath  the file path
     */
    @Transactional
    public void processZoomRecording(String meetingId, String filePath) {
        System.out.println("Processing Zoom recording for meetingId: " + meetingId + ", filePath: " + filePath);

        AudioMessage audioMessage = new AudioMessage();
        audioMessage.setPlatform("Zoom");
        audioMessage.setChannelId(meetingId);
        audioMessage.setAuthor("Zoom Meeting Bot");
        audioMessage.setAudioUrl(filePath);
        audioMessage.setTimestamp(LocalDateTime.now());
        audioMessage.setVoiceSessionId("zoom_" + meetingId);

        processAndSaveAudioMessage(audioMessage);

        System.out.printf("Zoom recording audio message saved for meetingId: %s%n", meetingId);
    }
}
