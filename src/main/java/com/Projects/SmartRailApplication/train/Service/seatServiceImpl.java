package com.Projects.SmartRailApplication.train.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.train.dto.SeatRequest;
import com.Projects.SmartRailApplication.train.dto.SeatResponse;

import jakarta.persistence.EntityNotFoundException;

import com.Projects.SmartRailApplication.train.Entities.Coach;
import com.Projects.SmartRailApplication.train.Entities.Seat;
import com.Projects.SmartRailApplication.train.Repository.*;

@Service
public class seatServiceImpl implements seatService {
    
    @Autowired
    private seatRepository seatRepository;

    @Autowired
    private coachRepository coachRepository;
    @Override
    public SeatResponse addSeat(Long coachId, SeatRequest seatRequest) {
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found with ID: " + coachId));
        if(seatRepository.existsByCoachIdAndSeatNumber(coach, seatRequest.getSeatNumber())) {
            throw new IllegalArgumentException("Seat with this number already exists in the coach");
        }
        Seat seat = new Seat();
        seat.setCoachId(coach);
        seat.setSeatNumber(seatRequest.getSeatNumber());
        seat.setSeatType(seatRequest.getSeatType());
        seatRepository.save(seat);
        return mapToResponse(seat);
    }

    @Override
    public List<SeatResponse> getSeatsByCoachId(Long coachId) {
        coachRepository.findById(coachId)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found with ID: " + coachId));
        List<Seat> seats = seatRepository.findByCoachId(coachId);
        return seats.stream().map(this::mapToResponse).toList();
    }

    @Override
    public SeatResponse getSeatById(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seat not found with ID: " + id));
        return mapToResponse(seat);
    }
    @Override
    public SeatResponse updateSeat(Long id, SeatRequest seatRequest) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seat not found with ID: " + id));
        if(seatRequest.getSeatNumber() != null) {
            seat.setSeatNumber(seatRequest.getSeatNumber());
        }
        if(seatRequest.getSeatType() != null) {
            seat.setSeatType(seatRequest.getSeatType());
        }
        seatRepository.save(seat);
        return mapToResponse(seat);
    }
    @Override
    public void deleteSeat(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seat not found with ID: " + id));
        seatRepository.delete(seat);
    }

    //helper method to map Seat entity to SeatResponse DTO
    public SeatResponse mapToResponse(Seat seat) {
        SeatResponse seatResponse = new SeatResponse();
        seatResponse.setId(seat.getId());
        seatResponse.setCoachId(seat.getCoachId().getId());
        seatResponse.setSeatNumber(seat.getSeatNumber());
        seatResponse.setSeatType(seat.getSeatType());
        return seatResponse;
    }





}
    