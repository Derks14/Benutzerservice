package tech11.utils.request;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@RequiredArgsConstructor
@Value
public class ErrorResponse {
    LocalDateTime timestamp;
    String error;
    int status;
    String message;
}