package br.com.zup.edu.tarcio.deleta

import br.com.zup.edu.tarcio.DeletaChavePixRequest
import br.com.zup.edu.tarcio.KeyManagerDeletaServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clientId}")
class DeletaChavePixController(
    private val removeChavePixClient: KeyManagerDeletaServiceGrpc.KeyManagerDeletaServiceBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Delete("/pix/{pixId}")
    fun delete(
        clientId: UUID,
        pixId: UUID
    ): HttpResponse<Any> {

        LOGGER.info("[$clientId] removendo uma chave pix com $pixId")
        removeChavePixClient.deleta(
            DeletaChavePixRequest.newBuilder()
                .setClientId(clientId.toString())
                .setPixId(pixId.toString())
                .build()
        )

        return HttpResponse.ok()
    }
}