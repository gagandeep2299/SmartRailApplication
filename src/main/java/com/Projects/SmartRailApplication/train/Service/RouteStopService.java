package com.Projects.SmartRailApplication.train.Service;

import java.util.List;

import com.Projects.SmartRailApplication.train.dto.RouteStopRequest;
import com.Projects.SmartRailApplication.train.dto.RouteStopResponse;

public interface RouteStopService {
    public void addRouteStop(Long trainId,RouteStopRequest routeStopRequest);

    public RouteStopResponse getRouteStopsById(Long id);

    List<RouteStopResponse> getRouteStopsByTrain(Long trainId);

    public RouteStopResponse updateRouteStop(Long id, RouteStopRequest routeStopRequest);
    public void deleteRouteStop(Long id);
}
