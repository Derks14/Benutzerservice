package tech11.utils.exception_mappers;

import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class InternalServerErrorExceptionMapper implements ExceptionMapper<InternalServerErrorException> {
    @Override
    public Response toResponse(InternalServerErrorException exception) {
        return ExceptionMapperUtils.buildErrorMessage(exception, Response.Status.INTERNAL_SERVER_ERROR);
    }
}
