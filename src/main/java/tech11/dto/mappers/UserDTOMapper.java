package tech11.dto.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import tech11.dto.UserDTO;
import tech11.models.User;

import java.util.function.Function;

@ApplicationScoped
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getUsername(),
                user.getEmail(),
                user.getBirthday(),
                user.getPhone(),
                user.getCreated(),
                user.getModified()
                );
    }
}
