package relayer;

import jakarta.transaction.Transactional;
import model.OutBoxEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import repository.OutBoxEventRepository;

import java.util.List;

/**
 * The type Out box event relayer.
 */
@Component
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class OutBoxEventRelayer {
    private final OutBoxEventRepository outBoxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Instantiates a new Out box event relayer.
     *
     * @param outBoxEventRepository the out box event repository
     * @param kafkaTemplate         the kafka template
     */
    public OutBoxEventRelayer(OutBoxEventRepository outBoxEventRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outBoxEventRepository = outBoxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Relay events.
     */
    @Scheduled(fixedDelay = 10000) //10 saniyede bir gönderecek
    @Transactional
    public void relayEvents(){
        List<OutBoxEvent> outBoxEvents = outBoxEventRepository.find100Unprocessed(PageRequest.of(0,100));
        for(OutBoxEvent event : outBoxEvents){
            try{
                kafkaTemplate.send("team-messages", event.getPayload());
                event.setProcessed(true);
                outBoxEventRepository.save(event);
            }catch (Exception e){
                throw new RuntimeException("Could not relay event", e);
            }
        }
    }
}
