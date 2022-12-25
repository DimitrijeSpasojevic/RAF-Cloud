package rs.raf.rafcloud.requests;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateUserRequest {

    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private List<String> roles = new ArrayList<>();

}
