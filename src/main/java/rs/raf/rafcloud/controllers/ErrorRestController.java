package rs.raf.rafcloud.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.rafcloud.aspect.MyAuthorization;
import rs.raf.rafcloud.dtos.ErrorFromFront;
import rs.raf.rafcloud.permissions.RoleEnum;
import rs.raf.rafcloud.responses.GetAllErrorsByUserResponse;
import rs.raf.rafcloud.services.ErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/errors")
public class ErrorRestController {

    private final ErrorService errorService;

    @Autowired
    public ErrorRestController(ErrorService errorService) {
        this.errorService = errorService;
    }

    @MyAuthorization(authorization = RoleEnum.can_search_machines)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllErrorsByUserId(@RequestAttribute("userId") Long userId){
        return ResponseEntity.ok(new GetAllErrorsByUserResponse(errorService.findAllByUserId(userId)));
    }

    @MyAuthorization(authorization = RoleEnum.can_create_machines)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addError(@RequestBody ErrorFromFront errorFromFront, @RequestAttribute("userId") Long userId ){
        return ResponseEntity.ok(errorService.addError(errorFromFront,userId));
    }

}
