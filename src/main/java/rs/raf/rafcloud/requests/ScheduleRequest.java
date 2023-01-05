package rs.raf.rafcloud.requests;

import lombok.Data;
import rs.raf.rafcloud.actions.ActionEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleRequest {

    @NotNull
    private Long machineId;
    @NotNull
    private Long timestamp;
}
