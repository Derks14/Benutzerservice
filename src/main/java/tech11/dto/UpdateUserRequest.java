package tech11.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UpdateUserRequest {
        @NotBlank(message = "You did not provide user's firstname")
        String firstname;

        @NotBlank(message = "You did not provide user's lastname")
        String lastname;

        @NotBlank(message = "You did not provide user's phone number")
        String phone;
}
