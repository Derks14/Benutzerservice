package tech11.dto;

public record UserDTO(
        String firstname,
        String lastname,
        String email,
        String birthday,
        String phone
) {
}
