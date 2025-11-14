package repository;

import model.OutBoxEvent;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface Out box event repository.
 */
@Repository
public interface OutBoxEventRepository extends JpaRepository<OutBoxEvent, Long> {

    /**
     * Find unprocessed list.
     *
     * @return the list
     */
    @Query("SELECT e FROM OutBoxEvent e WHERE e.processed = false")
    List<OutBoxEvent> findUnprocessed();

    /**
     * Find 100 unprocessed list.
     *
     * @param pageRequest the page request
     * @return the list
     */
    @Query("SELECT e FROM OutBoxEvent e WHERE e.processed = false ORDER BY e.createdAt ASC LIMIT 100")
    List<OutBoxEvent> find100Unprocessed(PageRequest pageRequest);

    /**
     * Find unprocessed by event type list.
     *
     * @param eventType the event type
     * @return the list
     */
    @Query("SELECT e FROM OutBoxEvent e WHERE e.processed = false AND e.eventType = :eventType")
    List<OutBoxEvent> findUnprocessedByEventType(@Param("eventType") String eventType);

    /**
     * Find by aggregate type and aggregate ıd list.
     *
     * @param aggregateType the aggregate type
     * @param aggregateId   the aggregate ıd
     * @return the list
     */
    List<OutBoxEvent> findByAggregateTypeAndAggregateId(String aggregateType, String aggregateId);

    /**
     * Mark as processed int.
     *
     * @param ids the ids
     * @return the int
     */
    @Modifying
    @Query("UPDATE OutBoxEvent e SET e.processed = true WHERE e.id IN :ids")
    int markAsProcessed(@Param("ids") List<Long> ids);

    /**
     * Delete processed event before int.
     *
     * @param beforeDate the before date
     * @return the int
     */
    @Modifying
    @Query("DELETE FROM OutBoxEvent e WHERE e.processed = true AND e.createdAt < :beforeDate")
    int deleteProcessedEventBefore(@Param("beforeDate") LocalDateTime beforeDate);
}