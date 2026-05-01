package com.suman.project.hotelBooking.service.Implementation;

import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import com.suman.project.hotelBooking.Strategy.PriceingStrategyImplementation.PricingService;
import com.suman.project.hotelBooking.dto.BookingDto;
import com.suman.project.hotelBooking.dto.BookingRequestDto;
import com.suman.project.hotelBooking.dto.GuestDto;
import com.suman.project.hotelBooking.entity.*;
import com.suman.project.hotelBooking.entity.enums.BookingStatus;
import com.suman.project.hotelBooking.exception.ResourceNotFoundException;
import com.suman.project.hotelBooking.exception.UnAuthorizeException;
import com.suman.project.hotelBooking.repository.*;
import com.suman.project.hotelBooking.service.BookingService;
import com.suman.project.hotelBooking.service.CheckoutService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImplementation implements BookingService
{
    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final HotelBookingRepository hotelBookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final CheckoutService checkoutService;
    private final PricingService pricingService;


    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    @Transactional
    public BookingDto initializeBooking(BookingRequestDto bookingRequestDto)
    {

        log.info("Initializing Booking for hotel : {} , room : {} , date : {}-{}" , bookingRequestDto.getHotelId() , bookingRequestDto.getRoomId()
                , bookingRequestDto.getCheckInDate() ,  bookingRequestDto.getCheckOutDate());


        Hotel hotel = hotelRepository.findById(bookingRequestDto.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel Not Found with id: " + bookingRequestDto.getHotelId()));


        Room room = roomRepository.findById(bookingRequestDto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room Not Found with id: " + bookingRequestDto.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(room.getId() ,
                bookingRequestDto.getCheckInDate() , bookingRequestDto.getCheckOutDate() , bookingRequestDto.getRoomsCount());

        long daysCount = ChronoUnit.DAYS.between(bookingRequestDto.getCheckInDate(), bookingRequestDto.getCheckOutDate())+1;

        if(inventoryList.size() != daysCount)
        {
            throw  new IllegalStateException("Room Is Not available anymore");
        }


        // Reserve the room / update the bookCount of Inventory's

//        for(Inventory inventory : inventoryList)
//        {
//            inventory.setReservedCount(inventory.getReservedCount() + bookingRequestDto.getRoomsCount());
//        }

//        inventoryRepository.saveAll(inventoryList);

//         Reserve the room / update the bookCount of Inventory's THe Updated One with JPQL

        inventoryRepository.initBooking(
                room.getId() , bookingRequestDto.getCheckInDate() ,
                bookingRequestDto.getCheckOutDate() ,
                bookingRequestDto.getRoomsCount()
        );



        // Create the booking
        //Calculate Dynamic amount
        BigDecimal priceForOneRoom = pricingService.calculateTotalPrice(inventoryList);
        BigDecimal totalPrice = priceForOneRoom.multiply(BigDecimal.valueOf(bookingRequestDto.getRoomsCount()));


        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequestDto.getCheckInDate())
                .checkOutDate(bookingRequestDto.getCheckOutDate())
                .user(getCurrentUser())
                .romsCount(bookingRequestDto.getRoomsCount())
                .amount(totalPrice)
                .build();

        booking = hotelBookingRepository.save(booking);

        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    @Transactional
    public BookingDto addGuest(Long bookingId, List<GuestDto> guestDtoList)
    {
        log.info("Adding guest for booking with id : {} ", bookingId);

        Booking booking = hotelBookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking Not Found with id: " + bookingId));

        User user = getCurrentUser();

        if(hasBookingExpired(booking))
        {
            throw new IllegalStateException("Booking has already expired");
        }

        if(!user.equals(booking.getUser()))
        {
            throw new UnAuthorizeException("Booking Does Not Exist to this user with Id "+user.getId());
        }

        if(booking.getBookingStatus() != BookingStatus.RESERVED)
        {
            throw new IllegalStateException("Booking is not under RESERVED state , cannot add guest");
        }

        for(GuestDto guestDto : guestDtoList)
        {
            Guest guest = modelMapper.map(guestDto, Guest.class);
            guest.setUser(user);
            guest = guestRepository.save(guest);
            booking.getGuests().add(guest);
        }

        booking.setBookingStatus(BookingStatus.GUEST_ADDED);
        booking = hotelBookingRepository.save(booking);

        return modelMapper.map(booking, BookingDto.class);

    }

    public boolean hasBookingExpired(Booking booking)
    {
        return booking.getCreateTime().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser()
    {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @Override
    @Transactional
    public String initiatePayment(Long bookingId)
    {
        Booking booking = hotelBookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Hotel Not Found with id: " + bookingId)
        );
        User user = getCurrentUser();

        if(!user.equals(booking.getUser()))
        {
            throw new UnAuthorizeException("Booking Does Not Exist to this user with Id "+user.getId());
        }
        if(hasBookingExpired(booking))
        {
            throw new IllegalStateException("Booking has already expired");
        }

        String sessionUrl = checkoutService.getCheckoutSession(booking ,
                frontendUrl+"/payment/success" , frontendUrl+"/payment/failure");


        booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
        bookingRepository.save(booking);


        return sessionUrl;
    }

//    @Override
//    @Transactional
//    public void capturePayment(Event event)
//    {
//
//        log.info("Event received: {}", event.getType());
//
//        if ("checkout.session.completed".equals(event.getType())
//                || "payment_intent.succeeded".equals(event.getType()))
//        {
//
//            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
//            if(session == null)
//            {
//                return;
//            }
//            String sessionId = session.getId();
//            Booking booking = bookingRepository.findByPaymentSessionId(sessionId).orElseThrow(() ->
//                    new ResourceNotFoundException("Booking Is Not found with is  "+sessionId));
//
//            booking.setBookingStatus(BookingStatus.CONFIRMED);
//            bookingRepository.save(booking);
//
//            inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId() ,booking.getCheckInDate() , booking.getCheckOutDate() , booking.getRomsCount());
//
//            inventoryRepository.confirmBooking(booking.getRoom().getId() ,booking.getCheckInDate() , booking.getCheckOutDate() , booking.getRomsCount());
//
//            log.info("Successfully confirm the booking for booking id "+booking.getId());
//
//        }
//        else
//        {
//            log.warn("Unhandled Event Type: {}", event.getType());
//        }




    @Override
    @Transactional
    public void capturePayment(Event event, String payload)
    {

        log.info(" Event received: {}", event.getType());

        if ("checkout.session.completed".equals(event.getType()))
        {

            try
            {
                //  Parse raw JSON (NO Stripe deserializer)
                com.google.gson.JsonObject json =
                        new com.google.gson.JsonParser().parse(payload).getAsJsonObject();

                String sessionId = json
                        .getAsJsonObject("data")
                        .getAsJsonObject("object")
                        .get("id")
                        .getAsString();

                log.info("Session ID from payload: {}", sessionId);

                Booking booking = bookingRepository
                        .findByPaymentSessionId(sessionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Booking not found for session: " + sessionId));

                booking.setBookingStatus(BookingStatus.CONFIRMED);
                bookingRepository.save(booking);

                inventoryRepository.findAndLockReservedInventory(
                        booking.getRoom().getId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getRomsCount()
                );

                inventoryRepository.confirmBooking(
                        booking.getRoom().getId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getRomsCount()
                );

                log.info("Booking CONFIRMED: {}", booking.getId());

            }
            catch (Exception e)
            {
                log.error("Error processing webhook", e);
                throw new RuntimeException(e);
            }
        }
        else
        {
            log.warn("Unhandled Event Type: {}", event.getType());
        }
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId)
    {
        Booking booking = hotelBookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Hotel Not Found with id: " + bookingId)
        );
        User user = getCurrentUser();

        if(!user.equals(booking.getUser()))
        {
            throw new UnAuthorizeException("Booking Does Not Exist to this user with Id "+user.getId());
        }
        if(booking.getBookingStatus() != BookingStatus.CONFIRMED)
        {
            throw new IllegalStateException("Only Confirmed Bookings can be cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        inventoryRepository.findAndLockReservedInventory(
                booking.getRoom().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getRomsCount()
        );

        inventoryRepository.cancelBooking(
                booking.getRoom().getId() ,
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getRomsCount()
        );

        // Handle Refund Process
        try
        {
            Session session = Session.retrieve(booking.getPaymentSessionId());
            RefundCreateParams refundCreateParams = RefundCreateParams.builder()
                    .setPaymentIntent(session.getPaymentIntent())
                    .build();

            Refund.create(refundCreateParams);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }


    }

    @Override
    public String getBookingStatus(Long bookingId)
    {
        Booking booking = hotelBookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Hotel Not Found with id: " + bookingId)
        );
        User user = getCurrentUser();

        if(!user.equals(booking.getUser()))
        {
            throw new UnAuthorizeException("Booking Does Not Exist to this user with Id "+user.getId());
        }

        return booking.getBookingStatus().name();
    }


}
