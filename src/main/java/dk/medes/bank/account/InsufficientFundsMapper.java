package dk.medes.bank.account;

import dk.medes.bank.shared.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InsufficientFundsMapper implements ExceptionMapper<InsufficientFundsException> {

    @Override
    public Response toResponse(InsufficientFundsException e) {
        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
    }
}
