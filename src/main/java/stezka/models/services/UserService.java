package stezka.models.services;


import org.springframework.security.core.userdetails.UserDetailsService;
import stezka.models.dto.UserDTO;

public interface UserService extends UserDetailsService {

    void create(UserDTO user, boolean isAdmin);

}
