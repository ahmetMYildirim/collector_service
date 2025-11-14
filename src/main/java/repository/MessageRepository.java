package repository;

import model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

/**
 * The interface Message repository.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    /**
     * Find by platform list.
     *
     * @param platform the platform
     * @return the list
     */
    List<Message> findByPlatform(String platform);

    /**
     * Findby timestamp list.
     *
     * @param start the start
     * @param end   the end
     * @return the list
     */
    List<Message> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Find by two metod list.
     *
     * @param platform the platform
     * @param start    the start
     * @param end      the end
     * @return the list
     */
    List<Message> findByPlatformAndTimestampBetween(
      String platform,
      LocalDateTime start,
      LocalDateTime end
    );

    /**
     * Findby author list.
     *
     * @param author the author
     * @return the list
     */
    List<Message> findByAuthor(String author);

    /**
     * Findby last ten list.
     *
     * @param platform the platform
     * @return the list
     */
    List<Message> findTop10ByPlatformOrderByTimestampDesc(String platform);

    /**
     * Searchby content list.
     *
     * @param keyword the keyword
     * @return the list
     */
    @Query("SELECT m FROM Message m WHERE m.content LIKE %:keyword%")
    List<Message> SearchbyContent(@Param("keyword") String keyword);
}
