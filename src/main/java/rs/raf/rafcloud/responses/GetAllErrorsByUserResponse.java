package rs.raf.rafcloud.responses;

import lombok.Data;
import rs.raf.rafcloud.model.MyError;

import java.util.List;

@Data
public class GetAllErrorsByUserResponse {
    private List<MyError> myErrors;

    public GetAllErrorsByUserResponse(List<MyError> myErrors) {
        this.myErrors = myErrors;
    }
}
