package com.tmax.eTest.Admin.dashboard.controller;

import com.tmax.eTest.Admin.dashboard.dto.DashboardOverallDTO;
import com.tmax.eTest.Admin.dashboard.dto.FilterDTO;
import com.tmax.eTest.Admin.dashboard.dto.SignUpCreateDateDTO;
import com.tmax.eTest.Admin.dashboard.service.DashboardService;
import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.repository.user.UserMasterRepo;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.repository.StatementRepository;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("admin")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private StatementRepository statementRepository;
    @Autowired
    @Qualifier("AU-UserRepository")
    private UserRepository userRepository;
    @Autowired
    private LRSAPIManager lrsapiManager;

    @GetMapping("dashboard/overall")
    public ResponseEntity<DashboardOverallDTO> getOverallCards
            (@RequestBody(required = false) FilterDTO filterDTO){
        Integer diagnosis = dashboardService.getDiagnosis(filterDTO).size();
        Integer minitest = dashboardService.getMinitest(filterDTO).size();
        Integer userIncrease = dashboardService.getUserIncrease(filterDTO).size();
        Integer userDelete = dashboardService.getUserDelete(filterDTO).size();
        Integer totalAccessUser = dashboardService.getAccessor(filterDTO).size();
        return ResponseEntity.ok(DashboardOverallDTO.builder()
                .totalAccessUser(totalAccessUser)
                .userIncrease(userIncrease)
//                .userRegistered()
                .userDeleted(userDelete)
//                .userTotal()
                .diagnosisTotal(diagnosis + minitest)
                .diagnosis(diagnosis)
                .minitest(minitest)
                .build());
    }

    @PostMapping("/dashboard/accessor")
    public CMRespDto<?> getAccessor (@RequestBody FilterDTO filterDTO){
        int result = dashboardService.getAccessor(filterDTO).size();
        return new CMRespDto<>(200, "success", result);
    }
    @PostMapping("/dashboard/user/increase")
    public CMRespDto<?> getUserIncrease (@RequestBody FilterDTO filterDTO){
        int result = dashboardService.getUserIncrease(filterDTO).size();
        return new CMRespDto<>(200, "success", result);
    }
    @PostMapping("/dashboard/user/delete")
    public CMRespDto<?> getUserDelete (@RequestBody FilterDTO filterDTO){
        int result = dashboardService.getUserDelete(filterDTO).size();
        return new CMRespDto<>(200, "success", result);
    }
}
