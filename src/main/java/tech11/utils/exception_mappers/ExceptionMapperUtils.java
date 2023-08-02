package tech11.utils.exception_mappers;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tech11.utils.request.ErrorResponse;

import java.time.LocalDateTime;

public class ExceptionMapperUtils {
    static protected Response buildErrorMessage(Throwable exception) {

        Class<? extends Throwable> error = exception.getClass();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .error(error.getName())
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .build();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();

    }
}
