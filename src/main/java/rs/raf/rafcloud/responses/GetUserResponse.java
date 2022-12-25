package rs.raf.rafcloud.responses;

import lombok.Data;
import rs.raf.rafcloud.model.User;

@Data
public class GetUserResponse {
    private User user;

    public GetUserResponse(User user) {
        this.user = user;
    }
}
