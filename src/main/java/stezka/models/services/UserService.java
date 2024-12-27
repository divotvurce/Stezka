package stezka.models.services;


import stezka.models.dto.UserDTO;

public interface UserService {

    void create(UserDTO user, boolean isAdmin);

}
