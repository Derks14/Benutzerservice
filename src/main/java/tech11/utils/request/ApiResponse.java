package tech11.utils.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

    String status;
    String message;
    T data;
    Pagination pagination;

}
