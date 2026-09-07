package dk.medes.bank.exchange;

import dk.medes.bank.exchange.dto.PairConversionResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.math.BigDecimal;

@RegisterRestClient(configKey = "exchange-api")
public interface ExchangeRateClient {

    @GET
    @Path("/pair/{from}/{to}/{amount}")
    PairConversionResponse convert(@PathParam("from") String baseCurrency,
                                   @PathParam("to") String targetCurrency,
                                   @PathParam("amount") BigDecimal amount);
}