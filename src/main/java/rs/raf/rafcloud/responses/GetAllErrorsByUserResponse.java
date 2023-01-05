package rs.raf.rafcloud.responses;

import lombok.Data;
import rs.raf.rafcloud.model.ErrorMessage;

import java.util.List;

@Data
public class GetAllErrorsByUserResponse {
    private List<ErrorMessage> errorMessages;

    public GetAllErrorsByUserResponse(List<ErrorMessage> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
