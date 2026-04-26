package com.suman.project.hotelBooking.repository;

import com.suman.project.hotelBooking.entity.Hotel;
import com.suman.project.hotelBooking.entity.Inventory;
import com.suman.project.hotelBooking.entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long>
{
    void deleteByAndRoom( Room room);

    @Query("""
        SELECT DISTINCT i.hotel
        FROM Inventory i
        WHERE i.city = :city
        AND i.date BETWEEN :startDate AND :endDate
        AND i.closed = false
        AND (i.totalCount - i.bookCount - i.reservedCount ) >= :roomCount
        GROUP BY i.hotel , i.room
        HAVING COUNT(i.date) = :dateCount
""")
    Page<Hotel> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomCount,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );

    /**
     * Finds inventory entries for a specific room and date range that have at least the requested available units.
     *
     * Matching rows are locked with a PESSIMISTIC_WRITE lock for the current transaction.
     *
     * @param roomId    the identifier of the room
     * @param startDate the start date of the inclusive date range
     * @param endDate   the end date of the inclusive date range
     * @param roomCount the minimum number of available units required per day
     * @return a list of Inventory entries that match the criteria; may be empty
     */
    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.room.id = :roomId
            AND i.date BETWEEN :startDate AND :endDate
            AND i.closed = false
            AND (i.totalCount - i.bookCount -i.reservedCount) >= :roomCount
""")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAndLockAvailableInventory(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomCount
    );

    /**
 * Retrieves inventory records for the specified hotel within the given date range.
 *
 * @param hotel the hotel whose inventory to retrieve
 * @param startDate the start of the date range (inclusive)
 * @param endDate the end of the date range (inclusive)
 * @return a list of Inventory entries for the hotel with `date` between `startDate` and `endDate`
 */
List<Inventory> findByHotelAndDateBetween(Hotel hotel, LocalDate startDate, LocalDate endDate);


}
