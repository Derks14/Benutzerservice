package tech11.dto;


import jakarta.validation.constraints.*;

// todo remember to validate the dateOfBirth in a simple function during the conversion of string to date
public record AddUserRequest(
        @NotBlank(message = "You did not provide user's firstname")
        String firstname,

        @NotBlank(message = "You did not provide user's lastname")
        String lastname,

        @NotBlank
        String username,
        @NotBlank(message = "You did not provide user's email")
        @Email(message = "The user's email address you provided is invalid")
        String email,

        @Pattern( regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "User's password should have at least ( a digit, a lowercase letter , an uppercase letter,a symbol")
        @Size(min = 8, max = 30, message = "User's password's length should be more than 8")
        String password,

        @NotBlank(message = "You did not provide user's date of birth")
        @Pattern( regexp = "^(0[1-9]|[1-2]\\d|3[0-1])-(0[1-9]|1[0-2])-\\d{4}$",
        // @Pattern( regexp = "^(0[1-9]|[1-2]\\d|3[0-1])(-|\\/)(0[1-9]|1[0-2])(-|\\/)\\d{4}$",
                message = "You passed an invalid date. date Of birth should be of the format dd-MM-yyyy ")
        String dateOfBirth,

        @NotBlank(message = "You did not provide user's phone")
        @Size(min = 10, message = "Phone number should be more than 10 numbers")
        @Digits(message = "User's phone number should be only digits", integer = 28, fraction = 0)
        String phone
) {
}
