package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.OutBoxEvent;
import model.VoiceSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.OutBoxEventRepository;
import repository.VoiceSessionRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * The type Voice session service.
 */
@Service
public class VoiceSessionService {
    private final VoiceSessionRepository voiceSessionRepository;
    private final OutBoxEventRepository outBoxEventRepository;
    private final ObjectMapper objectMapper;

    private final Map<String, Long> activeSessions = new HashMap<>();

    /**
     * Instantiates a new Voice session service.
     *
     * @param voiceSessionRepository the voice session repository
     * @param outBoxEventRepository  the out box event repository
     * @param objectMapper           the object mapper
     */
    public VoiceSessionService(VoiceSessionRepository voiceSessionRepository, OutBoxEventRepository outBoxEventRepository, ObjectMapper objectMapper) {
        this.voiceSessionRepository = voiceSessionRepository;
        this.outBoxEventRepository = outBoxEventRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Handle user joined voice channel.
     *
     * @param platform    the platform
     * @param channelId   the channel ıd
     * @param channelName the channel name
     * @param userName    the user name
     */
    @Transactional
    public void handleUserJoinedVoiceChannel(String platform, String channelId, String channelName, String userName) {
        String sessionKey = platform + "_" + channelId;

        if(!activeSessions.containsKey(sessionKey)) {
            VoiceSession voiceSession = new VoiceSession();
            voiceSession.setPlatform(platform);
            voiceSession.setChannelId(channelId);
            voiceSession.setChannelName(channelName);
            voiceSession.setStartTime(LocalDateTime.now());
            voiceSession.setParticipantCount(1);

            VoiceSession savedVoiceSession = voiceSessionRepository.save(voiceSession);
            activeSessions.put(sessionKey, savedVoiceSession.getId());

            createdOutboxEvent(savedVoiceSession, "VoiceSessionStarted");
        }else{
            Long sessionId = activeSessions.get(sessionKey);
            voiceSessionRepository.findById(sessionId).ifPresent(voiceSession -> {
                voiceSession.setParticipantCount(voiceSession.getParticipantCount() + 1);
                voiceSessionRepository.save(voiceSession);
            });
        }
    }

    /**
     * Handle user left voice channel.
     *
     * @param platform  the platform
     * @param channelId the channel ıd
     */
    @Transactional
    public void handleUserLeftVoiceChannel(String platform, String channelId) {
        String sessionKey = platform + "_" + channelId;

        if(activeSessions.containsKey(sessionKey)) {
            Long sessionId = activeSessions.get(sessionKey);
            voiceSessionRepository.findById(sessionId).ifPresent(voiceSession -> {
                int newCount = voiceSession.getParticipantCount() - 1;
                if(newCount <=0){
                    voiceSession.setParticipantCount(0);
                    voiceSession.setEndTime(LocalDateTime.now());
                    voiceSessionRepository.save(voiceSession);
                    createdOutboxEvent(voiceSession, "VoiceSessionEnded");
                    activeSessions.remove(sessionKey);
                }else{
                    voiceSession.setParticipantCount(newCount);
                    voiceSessionRepository.save(voiceSession);
                }
            });
        }
    }

    private void createdOutboxEvent(VoiceSession voiceSession, String eventType) {
        try{
            String payload = objectMapper.writeValueAsString(voiceSession);
            OutBoxEvent outboxEvent = new OutBoxEvent();
            outboxEvent.setAggregateType("VoiceSession");
            outboxEvent.setAggregateId(voiceSession.getId().toString());
            outboxEvent.setEventType(eventType);
            outboxEvent.setPayload(payload);
            outboxEvent.setCreatedAt(LocalDateTime.now());

            outBoxEventRepository.save(outboxEvent);
        }catch (Exception e){
            throw new RuntimeException("Failed to create outbox event: " + e.getMessage());
        }
    }
}
