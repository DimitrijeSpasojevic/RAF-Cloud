package rs.raf.rafcloud.responses;

import lombok.Data;
import rs.raf.rafcloud.dtos.UserDtoWithRoles;

import java.util.List;

@Data
public class GetAllUsersResponse {
    private List<UserDtoWithRoles> users;

    public GetAllUsersResponse(List<UserDtoWithRoles> users) {
        this.users = users;
    }
}
