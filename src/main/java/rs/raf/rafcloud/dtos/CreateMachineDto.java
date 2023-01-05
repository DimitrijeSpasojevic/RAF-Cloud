package rs.raf.rafcloud.dtos;

import lombok.Data;
import rs.raf.rafcloud.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateMachineDto {

    @NotEmpty(message = "ne sme biti prazno")
    @NotNull
    private String name;
}
