package com.Projects.SmartRailApplication.booking.service;

import java.util.List;

import com.Projects.SmartRailApplication.auth.entities.CustomUserDetails;
import com.Projects.SmartRailApplication.auth.entities.User;
import com.Projects.SmartRailApplication.booking.dto.bookingReponse;
import com.Projects.SmartRailApplication.booking.dto.bookingRequest;

public interface BookingServices {
    public bookingReponse createBooking(bookingRequest bookingRequest,User user);
    public bookingReponse getBookingById(Long bookingId);
    public List<bookingReponse> getBookingByUser(CustomUserDetails userDetails);
}
