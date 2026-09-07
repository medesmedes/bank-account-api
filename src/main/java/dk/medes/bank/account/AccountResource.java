package dk.medes.bank.account;

import dk.medes.bank.account.dto.CreateAccountRequest;
import dk.medes.bank.account.dto.DepositRequest;
import dk.medes.bank.account.dto.TransferRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Accounts", description = "Create accounts and manage balances")
public class AccountResource {

    @Inject
    AccountService service;

    @POST
    @Operation(summary = "Create an account")
    @APIResponse(responseCode = "201", description = "Account created",
            content = @Content(schema = @Schema(implementation = AccountResponse.class)))
    @APIResponse(responseCode = "400", description = "Invalid request")
    public Response createAccount(@Valid CreateAccountRequest request, @Context UriInfo uriInfo) {
        Account account = service.createAccount(request.firstName(), request.lastName());
        URI location = uriInfo.getAbsolutePathBuilder().path(account.getAccountNumber()).build();
        return Response.created(location).entity(AccountResponse.from(account)).build();
    }

    @GET
    @Path("/{accountNumber}")
    @Operation(summary = "Get an account and its balance")
    @APIResponse(responseCode = "200", description = "The account")
    @APIResponse(responseCode = "404", description = "Account not found")
    public AccountResponse getBalance(@PathParam("accountNumber") String accountNumber) {
        return AccountResponse.from(service.getByAccountNumber(accountNumber));
    }

    @POST
    @Path("/{accountNumber}/deposit")
    @Operation(summary = "Deposit money into an account")
    @APIResponse(responseCode = "200", description = "Updated account")
    @APIResponse(responseCode = "400", description = "Invalid amount")
    @APIResponse(responseCode = "404", description = "Account not found")
    public AccountResponse depositMoney(@PathParam("accountNumber") String accountNumber,
                                        @Valid DepositRequest request) {
        return AccountResponse.from(service.deposit(accountNumber, request.amount()));
    }

    @POST
    @Path("/{accountNumber}/transfer")
    @Operation(summary = "Transfer money to another account")
    @APIResponse(responseCode = "200", description = "Updated source account")
    @APIResponse(responseCode = "400", description = "Invalid request or same-account transfer")
    @APIResponse(responseCode = "404", description = "Account not found")
    @APIResponse(responseCode = "422", description = "Insufficient funds")
    public AccountResponse transferMoney(@PathParam("accountNumber") String accountNumber,
                                         @Valid TransferRequest request) {
        return AccountResponse.from(
                service.transfer(accountNumber, request.toAccountNumber(), request.amount()));
    }
}
