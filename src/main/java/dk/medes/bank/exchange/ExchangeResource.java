package dk.medes.bank.exchange;

import dk.medes.bank.exchange.dto.ExchangeResponse;
import dk.medes.bank.exchange.dto.PairConversionResponse;
import dk.medes.bank.shared.Money;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;

@Path("/exchange")
@Tag(name = "Exchange", description = "Currency conversion via a third-party provider")
public class ExchangeResource {

    @Inject
    @RestClient
    ExchangeRateClient client;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Convert 100 DKK to USD at the current rate")
    @APIResponse(responseCode = "200", description = "Shows converted amounts, e.g. {\"DKK\":100.00,\"USD\":15.54}")
    @APIResponse(responseCode = "502", description = "Exchange rate provider failed")
    // @Retry would be ideal here for transient errors e.g (Bad Gateway, Gateway Timeout, etc.)
    public ExchangeResponse getDKKtoUSDExchangeRate() {
        // Hardcoded amount and currency to satisfy task (but there's no reason why they couldn't be parameters)
        BigDecimal amount = Money.of("100");
        PairConversionResponse response = client.convert("DKK", "USD", amount);

        if (!"success".equals(response.result())) {
            throw new WebApplicationException("Call to ExchangeRate-API failed", 502);
        }

        return new ExchangeResponse(amount, Money.normalize(response.conversionResult()));
    }
}
