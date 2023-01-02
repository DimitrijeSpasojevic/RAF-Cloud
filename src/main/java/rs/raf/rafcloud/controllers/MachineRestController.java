package rs.raf.rafcloud.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.rafcloud.aspect.MyAuthorization;
import rs.raf.rafcloud.dtos.CreateMachineDto;
import rs.raf.rafcloud.permissions.RoleEnum;
import rs.raf.rafcloud.requests.CreateUserRequest;
import rs.raf.rafcloud.responses.GetUserResponse;
import rs.raf.rafcloud.services.MachineService;

@CrossOrigin
@RestController
@RequestMapping("/api/machines")
public class MachineRestController {

    private final MachineService machineService;

    @Autowired
    public MachineRestController(MachineService machineService) {
        this.machineService = machineService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllMachines(){
        return ResponseEntity.ok(machineService.findAll());
    }

    @MyAuthorization(authorization = RoleEnum.can_search_machines)
    @GetMapping(value = "/createdByLoggedUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllMachinesByUserId(@RequestAttribute("userId") Long userId){
        return ResponseEntity.ok(machineService.findAllByUserId(userId));
    }

    @MyAuthorization(authorization = RoleEnum.can_create_machines)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createMachine(@RequestBody CreateMachineDto createMachineDto, @RequestAttribute("userId") Long userId ){
        return ResponseEntity.ok(machineService.createMachine(createMachineDto , userId));
    }

    @PutMapping(value = "/start/{machineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> startMachine(@RequestAttribute("userId") Long userId, @PathVariable Long machineId){
        return ResponseEntity.ok(machineService.startMachine(machineId, userId));
    }

//    @MyAuthorization(authorization = RoleEnum.can_destroy_machines)
//    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> destroyMachine(@RequestBody CreateMachineDto createMachineDto, @RequestAttribute("userId") Long userId ){
//        return ResponseEntity.ok(machineService.createMachine(createMachineDto , userId));
//    }

}
