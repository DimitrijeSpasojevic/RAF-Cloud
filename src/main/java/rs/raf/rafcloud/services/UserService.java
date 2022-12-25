package rs.raf.rafcloud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.raf.rafcloud.dtos.UserDtoWithRoles;
import rs.raf.rafcloud.mappers.UserMapper;
import rs.raf.rafcloud.model.User;
import rs.raf.rafcloud.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IService<User,Long>, UserDetailsService {

    private UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = this.userRepository.findByUsername(username);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+username+" not found");
        }

        return new org.springframework.security.core.userdetails.User(myUser.getUsername(), myUser.getPassword(), myUser.getRoles());
    }

    @Override
    public <S extends User> S save(S user) {
        Object o = userRepository.save(user);
        System.out.println(o + " object");
        return (S) o;
    }

    public List<UserDtoWithRoles> findAll() {
        List<UserDtoWithRoles> usersDtoWithRoles = new ArrayList<>();
        List<User> usersFromDb = userRepository.findAll();
        for (User u : usersFromDb){
            usersDtoWithRoles.add(userMapper.mapUserToUserDtoWithRoles(u));
        }
        return (List<UserDtoWithRoles>) usersDtoWithRoles;
    }

    @Override
    public User findById(Long var1) {
        return userRepository.findByUserId(var1);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
