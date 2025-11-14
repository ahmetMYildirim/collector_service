package repository;

import model.VoiceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Voice session repository.
 */
@Repository
public interface VoiceSessionRepository extends JpaRepository<VoiceSession, Long> {
    /**
     * Find by platform, channel and end time is null.
     *
     * @param platform  the platform
     * @param channelId the channel id
     * @return the optional
     */
    Optional<VoiceSession> findByPlatformAndChannelIdAndEndTimeIsNull(String platform, String channelId);

    /**
     * Find by channel id list.
     *
     * @param channelId the channel id
     * @return the list
     */
    List<VoiceSession> findByChannelId(String channelId);
}
