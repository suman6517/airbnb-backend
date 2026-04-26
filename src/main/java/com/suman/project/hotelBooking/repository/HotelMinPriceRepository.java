package com.suman.project.hotelBooking.repository;

import com.suman.project.hotelBooking.dto.HotelPriceDto;
import com.suman.project.hotelBooking.entity.Hotel;
import com.suman.project.hotelBooking.entity.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HotelMinPriceRepository  extends JpaRepository<HotelMinPrice , Long>
{

    /**
     * Finds hotels in the specified city that have inventory within the given date range and returns each hotel with its average nightly price for that range.
     *
     * The query filters by hotel city, dates between {@code startDate} and {@code endDate}, and only includes hotels marked active; results are grouped by hotel and projected as {@code HotelPriceDto(hotel, avgPrice)}.
     *
     * @param roomCount  currently not referenced by the query; provided for API compatibility
     * @param dateCount  currently not referenced by the query; provided for API compatibility
     * @param pageable   paging and sorting information applied to the returned page
     * @return           a page of {@code HotelPriceDto}, each containing a hotel and the average price across matching {@code HotelMinPrice} entries
     */
    @Query("""
        SELECT new com.suman.project.hotelBooking.dto.HotelPriceDto(i.hotel, AVG(i.price))
        FROM HotelMinPrice i
        WHERE i.hotel.city = :city
        AND i.date BETWEEN :startDate AND :endDate
        AND i.hotel.active = true
        GROUP BY i.hotel
""")
    Page<HotelPriceDto> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomCount,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );

    /**
 * Finds the minimum price record for a given hotel on the specified date.
 *
 * @param hotel the hotel to search for
 * @param date the date of the price entry to find
 * @return an Optional containing the matching HotelMinPrice if found, empty otherwise
 */
Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);
}
