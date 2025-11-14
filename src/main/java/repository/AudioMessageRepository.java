package repository;

import model.AudioMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Audio message repository.
 */
@Repository
public interface AudioMessageRepository extends JpaRepository<AudioMessage, Long> {
    /**
     * Find by voice session ıd list.
     *
     * @param voiceSessionId the voice session ıd
     * @return the list
     */
    List<AudioMessage> findByVoiceSessionId(String voiceSessionId);
}
