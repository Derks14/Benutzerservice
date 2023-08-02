package tech11.services;

import org.hibernate.sql.Update;
import tech11.dto.AddUserRequest;
import tech11.dto.UpdateUserRequest;
import tech11.dto.UserDTO;
import tech11.utils.request.ApiResponse;

import java.util.List;

public interface UserService {
    ApiResponse<List<UserDTO>> fetchUsers(int page, int size);

    UserDTO addUser(AddUserRequest addUserRequest);

    UserDTO fetchSingleUser(long id);

    UserDTO modifyUser(long id, UpdateUserRequest request);

    UserDTO deleteUser(long id);
}
