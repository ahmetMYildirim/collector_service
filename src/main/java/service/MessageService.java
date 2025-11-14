package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Message;
import model.OutBoxEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.MessageRepository;
import repository.OutBoxEventRepository;

import java.time.LocalDateTime;

/**
 * The type Message service.
 */
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final OutBoxEventRepository outBoxEventRepository;
    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new Message service.
     *
     * @param messageRepository     the message repository
     * @param outBoxEventRepository the out box event repository
     * @param objectMapper          the object mapper
     */
    public MessageService(MessageRepository messageRepository, OutBoxEventRepository outBoxEventRepository, ObjectMapper objectMapper) {
        this.messageRepository = messageRepository;
        this.outBoxEventRepository = outBoxEventRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Process and save message.
     *
     * @param message the message
     */
    @Transactional
    public void processAndSaveMessage(Message message) {
        Message saveMessage = messageRepository.save(message);
        try {
            String payload = objectMapper.writeValueAsString(saveMessage);
            OutBoxEvent outBoxEvent = new OutBoxEvent();
            outBoxEvent.setAggregateType("Message");
            outBoxEvent.setAggregateId(saveMessage.getId().toString());
            outBoxEvent.setEventType("MessageCreated");
            outBoxEvent.setPayload(payload);
            outBoxEvent.setCreatedAt(LocalDateTime.now());

            outBoxEventRepository.save(outBoxEvent);
        }catch (Exception e) {
            throw new RuntimeException("Could not create outbox event", e);
        }
    }
}
