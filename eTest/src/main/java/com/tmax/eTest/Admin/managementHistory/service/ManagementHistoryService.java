package com.tmax.eTest.Admin.managementHistory.service;

import com.tmax.eTest.Admin.managementHistory.model.ManagementHistory;
import com.tmax.eTest.Admin.managementHistory.model.RequestMapper;
import com.tmax.eTest.Admin.managementHistory.repository.ManagementHistoryRepository;
import com.tmax.eTest.Admin.managementHistory.repository.RequestMapperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagementHistoryService {
    private final ManagementHistoryRepository managementHistoryRepository;
    private final RequestMapperRepository requestMapperRepository;

    public ManagementHistory createManagementHistory(ManagementHistory managementHistory){
        return managementHistoryRepository.save(managementHistory);
    }

    public void deleteManagementHistory (Long id){
        managementHistoryRepository.deleteById(id);
    }

    public List<ManagementHistory> getManagementHistoryList (){
        return managementHistoryRepository.findAll();
    }

    public RequestMapper createRequestMapper(RequestMapper requestMapper){
        return requestMapperRepository.save(requestMapper);
    }

    public void deleteRequestMapper (Long id){
        requestMapperRepository.deleteById(id);
    }

    public List<RequestMapper> getRequestMapperList (){
        return requestMapperRepository.findAll();
    }

    public RequestMapper getRequestMapper (String method, String controller, String name){
        return requestMapperRepository.findByMethodAndControllerAndName(method, controller, name);
    }
}