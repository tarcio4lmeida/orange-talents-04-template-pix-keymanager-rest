package br.com.zup.edu.tarcio.deleta

import br.com.zup.edu.tarcio.DeletaChavePixResponse
import br.com.zup.edu.tarcio.KeyManagerDeletaServiceGrpc
import br.com.zup.edu.tarcio.shared.grpc.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class DeletaChavePixControllerTest {


    @field:Inject
    lateinit var removeStub: KeyManagerDeletaServiceGrpc.KeyManagerDeletaServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve remover uma chave pix existente`() {

        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = DeletaChavePixResponse.newBuilder()
            .setClientId(clientId)
            .setPixId(pixId)
            .build()
        given(removeStub.deleta(any())).willReturn(respostaGrpc)


        val request = HttpRequest.DELETE<Any>("/api/v1/clientes/$clientId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class RemoveStubFactory {

        @Singleton
        fun deletaChave() = mock(KeyManagerDeletaServiceGrpc.KeyManagerDeletaServiceBlockingStub::class.java)
    }

}