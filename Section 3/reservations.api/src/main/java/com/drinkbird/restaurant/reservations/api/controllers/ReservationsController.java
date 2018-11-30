package com.drinkbird.restaurant.reservations.api.controllers;

import com.drinkbird.restaurant.reservations.api.domain.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationsController {
    private final ReservationEventStore eventStore;

    public ReservationsController(ReservationEventStore eventStore) {
        this.eventStore = eventStore;
    }

    @RequestMapping(value = "reservation/{dateId}",
                    method = RequestMethod.GET)
    public AvailabilityDto getAvailability(
        @PathVariable("dateId") String dateIdStr
    ) {
        DateId dateId = DateId.parse(dateIdStr);
        DateTime today = DateTime.now();

        if (dateId.isEarlierThan(today)) {
            logger.info("Date is in the past. dateId={}|seats=0", dateId.toString());
            return new AvailabilityDto(dateId, 0);
        }
        else {
            List<ReservationEvent> events = eventStore.GetEvents(dateId);

            DateAggregate date = new DateAggregate(dateId);
            date.apply(events);

            int availableSeats = date.getAvailableSeats();
            logger.info("Retrieved availability. dateId={}|seats={}",
                dateId.toString(), availableSeats);

            return new AvailabilityDto(dateId, availableSeats);
        }
    }

    @RequestMapping(value = "reservation/{dateId}",
                    method = RequestMethod.POST)
    public ResponseEntity<String> createReservation(
        @PathVariable("dateId") String dateIdStr,
        @RequestBody CreateReservationRequestDto requestDto
    ) {
        ReservationCommandData commandData =
            new CreateReservationData(
                requestDto.getEmail(),
                requestDto.getName(),
                requestDto.getSeats());

        return handleCommand(dateIdStr, commandData);
    }

    @RequestMapping(value = "reservation/{dateId}",
                    method = RequestMethod.DELETE)
    public ResponseEntity<String> cancelReservation(
        @PathVariable("dateId") String dateIdStr,
        @RequestBody CancelReservationRequestDto requestDto
    ) {
        ReservationCommandData commandData =
            new CancelReservationData(requestDto.getEmail());

        return handleCommand(dateIdStr, commandData);
    }

    private ResponseEntity<String> handleCommand(
        String dateIdStr, ReservationCommandData commandData)
    {
        DateId dateId = DateId.parse(dateIdStr);

        ReservationCommand command =
            new ReservationCommand(dateId, DateTime.now(), commandData);

        ReservationCommandProcessor processor =
            new ReservationCommandProcessor(eventStore);

        try {
            processor.ProcessCommand(command, DateTime.now());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (ReservationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private static Logger logger = LoggerFactory.getLogger(ReservationsController.class);
}
