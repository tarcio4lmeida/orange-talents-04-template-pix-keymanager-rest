package br.com.zup.edu.tarcio.registra

import br.com.zup.edu.tarcio.KeyManagerRegistraServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes/{clientId}")
class RegistraChavePixController(
    @Inject val grpcClient: KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub
) {
    private val logger = LoggerFactory.getLogger((this::class.java))

    @Post("/pix")
    fun registra(
        clientId: UUID,
        @Valid @Body request: NovaChavePixRequest
    ): HttpResponse<Any> {
        logger.info("[${clientId}] criando uma nova chave pix com $request")
        val grpcResponse = grpcClient.registra(request.paraModeloGrpc(clientId))

        return HttpResponse.created(location(clientId, grpcResponse.pixId))
    }

    private fun location(clienteId: UUID, pixId: String) = HttpResponse
        .uri("/api/v1/clientes/$clienteId/pix/${pixId}")
}