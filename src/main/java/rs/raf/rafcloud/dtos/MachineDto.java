package rs.raf.rafcloud.dtos;

import lombok.Data;
import rs.raf.rafcloud.model.User;

@Data
public class MachineDto {

    private Long id;
    private String status;
    private User createdBy;
    private Boolean active;
}
