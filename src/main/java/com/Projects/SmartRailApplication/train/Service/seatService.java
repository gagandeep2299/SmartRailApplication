package com.Projects.SmartRailApplication.train.Service;

import java.util.List;

import com.Projects.SmartRailApplication.train.dto.SeatRequest;
import com.Projects.SmartRailApplication.train.dto.SeatResponse;

public interface seatService {
    public SeatResponse addSeat(Long coachId, SeatRequest seatRequest);
    public List<SeatResponse> getSeatsByCoachId(Long coachId);
    public SeatResponse getSeatById(Long id);
    public SeatResponse updateSeat(Long id, SeatRequest seatRequest);
    public void deleteSeat(Long id);
}
