package rs.raf.rafcloud.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDtoWithRoles {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private List<String> roles = new ArrayList<>();
}
