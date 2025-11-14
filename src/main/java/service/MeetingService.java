package service;

import model.Meeting;
import model.OutBoxEvent;
import model.VoiceSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.MeetingRepository;
import repository.OutBoxEventRepository;

import java.time.LocalDateTime;

/**
 * The type Meeting service.
 */
@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final OutBoxEventRepository outBoxEventRepository;
    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new Meeting service.
     *
     * @param meetingRepository     the meeting repository
     * @param outBoxEventRepository the out box event repository
     * @param objectMapper          the object mapper
     */
    public MeetingService(MeetingRepository meetingRepository, OutBoxEventRepository outBoxEventRepository, ObjectMapper objectMapper) {
        this.meetingRepository = meetingRepository;
        this.outBoxEventRepository = outBoxEventRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Start meeting.
     *
     * @param meeting the meeting
     */
    @Transactional
    public void startMeeting(Meeting meeting) {
        meeting.setStartTime(LocalDateTime.now());
        Meeting savedMeeting = meetingRepository.save(meeting);
        createdOutboxEvent(savedMeeting, "MeetingStarted");
    }

    /**
     * End meeting.
     *
     * @param meetingId the meeting ıd
     */
    @Transactional
    public void endMeeting(Long meetingId) {
        meetingRepository.findById(meetingId).ifPresent(meeting -> {
           meeting.setEndTime(LocalDateTime.now());
           Meeting updatedMeeting = meetingRepository.save(meeting);
           createdOutboxEvent(updatedMeeting, "MeetingEnded");
        });
    }

    private void createdOutboxEvent(Meeting meeting, String eventType) {
        try{
            String payload = objectMapper.writeValueAsString(meeting);
            OutBoxEvent outboxEvent = new OutBoxEvent();
            outboxEvent.setAggregateType("Meeting");
            outboxEvent.setAggregateId(meeting.getId().toString());
            outboxEvent.setEventType(eventType);
            outboxEvent.setPayload(payload);
            outboxEvent.setCreatedAt(LocalDateTime.now());

            outBoxEventRepository.save(outboxEvent);
        }catch (Exception e){
            throw new RuntimeException("Failed to create outbox event: " + e.getMessage());
        }
    }
}
