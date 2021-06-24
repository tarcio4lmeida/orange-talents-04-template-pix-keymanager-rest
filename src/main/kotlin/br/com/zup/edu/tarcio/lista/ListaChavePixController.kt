package br.com.zup.edu.tarcio.lista

import br.com.zup.edu.tarcio.KeyManagerListaServiceGrpc
import br.com.zup.edu.tarcio.ListaChavesPixRequest
import br.com.zup.edu.tarcio.carrega.ChavePixResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clientId}")
class ListaChavePixController(
    val listaChavePixClient: KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/")
    fun lista(clientId: UUID): HttpResponse<Any> {
        LOGGER.info("[$clientId] listando chaves pix")
        val pix = listaChavePixClient.lista(
            ListaChavesPixRequest.newBuilder()
                .setClientId(clientId.toString())
                .build()
        )
        val chaves = pix.chavesList.map { ChavePixResponse(it) }
        return HttpResponse.ok(chaves)
    }

}