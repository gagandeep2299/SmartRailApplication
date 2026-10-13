package com.Projects.SmartRailApplication.booking.service;

import com.Projects.SmartRailApplication.auth.entities.User;
import com.Projects.SmartRailApplication.booking.dto.bookingReponse;
import com.Projects.SmartRailApplication.booking.dto.bookingRequest;

public interface BookingServices {
    public bookingReponse createBooking(bookingRequest bookingRequest,User user);
}
