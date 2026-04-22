package com.suman.project.hotelBooking.service.Implementation;

import com.suman.project.hotelBooking.Strategy.PriceingStrategyImplementation.PricingService;
import com.suman.project.hotelBooking.entity.Hotel;
import com.suman.project.hotelBooking.entity.HotelMinPrice;
import com.suman.project.hotelBooking.entity.Inventory;
import com.suman.project.hotelBooking.repository.HotelMinPriceRepository;
import com.suman.project.hotelBooking.repository.HotelRepository;
import com.suman.project.hotelBooking.repository.InventoryRepository;
import com.suman.project.hotelBooking.service.PricingUpdateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class PricingUpdateServiceImplementation implements PricingUpdateService
{
    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final PricingService pricingService;


    @Scheduled(cron = "0 0 * * * *")
    @Override
    public void UpdatePrices()
    {

        int page = 0;
        int batchSize = 100;

        while (true)
        {
            Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page, batchSize));
            if(hotelPage.isEmpty())
            {
                break;
            }
            hotelPage.getContent().forEach(this::updateHotelPrices);
            page++;
        }
    }

    private void updateHotelPrices(Hotel hotel)
    {
        log.info("Updating Prices for Hotel Booking... for hotel {}" , hotel.getId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel,startDate ,endDate);

        updateInventoryPrices(inventoryList);

        updateHotelMinPrice(hotel , inventoryList , startDate , endDate);

    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {
        // Complete minimum price per day for the hotel
        Map<LocalDate, BigDecimal> dailyMinPrice = inventoryList.stream()
                .collect(Collectors.groupingBy(
                        Inventory::getDate,
                        Collectors.mapping(Inventory::getPrice, Collectors.minBy (Comparator.naturalOrder()))
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey , e -> e.getValue().orElse(BigDecimal.ZERO)));


        // Prepare  HotelPrice entities in bulk

        List<HotelMinPrice> hotelPrices = new ArrayList<>();
        dailyMinPrice.forEach((date,price)->{
            HotelMinPrice hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel , date)
                    .orElse(new HotelMinPrice(hotel,date));

            hotelPrice.setPrice(price);
            hotelPrices.add(hotelPrice);
        });

        // Save all HotelPrice entities in bulk
        hotelMinPriceRepository.saveAll(hotelPrices);

    }


    private  void updateInventoryPrices(List<Inventory> inventoryList)
    {
        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPrice = pricingService.calculateDynamicPrice(inventory);
            inventory.setPrice(dynamicPrice);
        });
        inventoryRepository.saveAll(inventoryList);
    }
}
