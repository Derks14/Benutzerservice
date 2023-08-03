package tech11.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO{
    String id;
    String firstname;
    String lastname;
    String email;
    String birthday;
    String phone;
    LocalDateTime created;
    LocalDateTime modified;
}
