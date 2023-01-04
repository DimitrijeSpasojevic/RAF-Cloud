package rs.raf.rafcloud.dtos;

import lombok.Data;

import javax.persistence.Column;

@Data
public class ErrorFromFront {

    private String errorText;
    private Long machineId;

}
