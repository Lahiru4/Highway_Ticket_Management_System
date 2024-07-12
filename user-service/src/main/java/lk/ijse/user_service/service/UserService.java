package     lk.ijse.user_service.service;

import lk.ijse.user_service.dto.UserDTO;

public interface UserService {

    String newUser(UserDTO userDTO) throws Exception;
    void deleteUser(String id);
    UserDTO getUser(String id);
    String updateUser(UserDTO userDTO);
    boolean isUserExist(String id);

}

