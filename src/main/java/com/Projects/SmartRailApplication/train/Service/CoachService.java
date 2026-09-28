package com.Projects.SmartRailApplication.train.Service;

import java.util.List;

import com.Projects.SmartRailApplication.train.dto.CoachRequest;
import com.Projects.SmartRailApplication.train.dto.CoachResponse;

public interface CoachService {
    public CoachResponse addCoach(Long trainId, CoachRequest coachRequest);
    public List<CoachResponse> getCoachesByTrainId(Long trainId);
    public CoachResponse getCoachById(Long coachId);
    public CoachResponse updateCoach(Long coachId, CoachRequest coachRequest);
    public void deleteCoach(Long coachId);
}
