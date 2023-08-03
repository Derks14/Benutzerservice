package tech11.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    String id;
    String firstname;
    String lastname;
    String username;
    String email;
    String birthday;
    String phone;
    LocalDateTime created;
    LocalDateTime modified;
}
