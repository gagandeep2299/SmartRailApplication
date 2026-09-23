package com.Projects.SmartRailApplication.train.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.train.Repository.trainRepository;

@Service 
public class trainService {
    @Autowired 
    private trainRepository trainRepository;
}
