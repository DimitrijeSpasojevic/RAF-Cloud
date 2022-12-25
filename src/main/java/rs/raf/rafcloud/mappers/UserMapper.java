package rs.raf.rafcloud.mappers;

import org.springframework.stereotype.Component;
import rs.raf.rafcloud.dtos.UserDtoWithRoles;
import rs.raf.rafcloud.model.Role;
import rs.raf.rafcloud.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public UserDtoWithRoles mapUserToUserDtoWithRoles(User user){
        UserDtoWithRoles userDtoWithRoles = new UserDtoWithRoles();

        List<Role> roles = user.getRoles();
        List<String> roleNames = new ArrayList<>();
        for(Role r: roles){
            roleNames.add(r.getRole());
        }
        userDtoWithRoles.setRoles(roleNames);
        userDtoWithRoles.setUsername(user.getUsername());
        userDtoWithRoles.setFirstName(user.getFirstName());
        userDtoWithRoles.setLastName(user.getLastName());
        userDtoWithRoles.setUserId(user.getUserId());
        userDtoWithRoles.setPassword(user.getPassword());

        return userDtoWithRoles;
    }
}
