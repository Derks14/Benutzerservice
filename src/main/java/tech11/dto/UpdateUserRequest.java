package tech11.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// todo remember to validate the dateofbirth in a simple function during the conversion of string to date
public record UpdateUserRequest(
        @NotBlank(message = "You did not provide user's firstname")
        String firstname,

        @NotBlank(message = "You did not provide user's lastname")
        String lastname,

        String phone
) {
}
