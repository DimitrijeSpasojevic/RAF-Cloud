package rs.raf.rafcloud.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import rs.raf.rafcloud.aspect.MyAuthorization;
import rs.raf.rafcloud.dtos.CreateMachineDto;
import rs.raf.rafcloud.model.Message;
import rs.raf.rafcloud.permissions.RoleEnum;
import rs.raf.rafcloud.requests.CreateUserRequest;
import rs.raf.rafcloud.responses.GetUserResponse;
import rs.raf.rafcloud.services.MachineService;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public ResponseEntity<?> createMachine(@Valid @RequestBody CreateMachineDto createMachineDto, @RequestAttribute("userId") Long userId ){
        return ResponseEntity.ok(machineService.createMachine(createMachineDto , userId));
    }


    @PutMapping(value = "/start/{machineId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> startMachine(@RequestAttribute("userId") Long userId, @PathVariable Long machineId){
        machineService.startMachine(machineId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/stop/{machineId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> stopMachine(@RequestAttribute("userId") Long userId, @PathVariable Long machineId){
        machineService.stopMachine(machineId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/restart/{machineId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> restartMachine(@RequestAttribute("userId") Long userId, @PathVariable Long machineId){
        machineService.restartMachine(machineId, userId);
        return ResponseEntity.ok().build();
    }

    @MyAuthorization(authorization = RoleEnum.can_destroy_machines)
    @PutMapping(value = "/destroy/{machineId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> destroyMachine(@RequestAttribute("userId") Long userId, @PathVariable Long machineId){
        machineService.destroyMachine(machineId, userId);
        return ResponseEntity.ok().build();
    }

}
