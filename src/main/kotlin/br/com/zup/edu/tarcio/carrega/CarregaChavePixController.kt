package br.com.zup.edu.tarcio.carrega

import br.com.zup.edu.tarcio.CarregaChavePixRequest
import br.com.zup.edu.tarcio.KeyManagerCarregaServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clientId}")
class CarregaChavePixController(
    val carregaChavePixClient: KeyManagerCarregaServiceGrpc.KeyManagerCarregaServiceBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/{pixId}")
    fun carrega(
        clientId: UUID,
        pixId: UUID
    ): HttpResponse<Any> {

        LOGGER.info("[$clientId] carrega chave pix por id: $pixId")
        val chaveResponse = carregaChavePixClient.carrega(
            CarregaChavePixRequest.newBuilder()
                .setPixId(
                    CarregaChavePixRequest.FiltroPorPixId.newBuilder()
                        .setClientId(clientId.toString())
                        .setPixId(pixId.toString())
                        .build()
                ).build()
        )

        return HttpResponse.ok(DetalheChavePixResponse(chaveResponse))
    }

}