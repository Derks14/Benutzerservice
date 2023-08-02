package tech11.utils.exception_mappers;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tech11.utils.request.ErrorResponse;

import java.time.LocalDateTime;

public class ExceptionMapperUtils {
    static protected Response buildErrorMessage(Throwable exception, Response.Status status) {

        Class<? extends Throwable> error = exception.getClass();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .error(error.getName())
                .status(status.getStatusCode())
                .build();

        return Response.status(status)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();

    }
}
