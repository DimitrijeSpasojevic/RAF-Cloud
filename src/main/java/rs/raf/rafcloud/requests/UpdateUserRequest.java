package rs.raf.rafcloud.requests;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateUserRequest {

    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private List<String> roles = new ArrayList<>();

}
