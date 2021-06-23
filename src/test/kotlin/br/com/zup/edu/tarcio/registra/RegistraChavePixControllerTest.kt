package br.com.zup.edu.tarcio.registra

import br.com.zup.edu.tarcio.KeyManagerRegistraServiceGrpc
import br.com.zup.edu.tarcio.RegistraChavePixResponse
import br.com.zup.edu.tarcio.shared.grpc.KeyManagerGrpcFactory
import org.junit.jupiter.api.Assertions.*

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.*

import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegistraChavePixControllerTest {

    @field:Inject
    lateinit var registraStub: KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve registrar uma nova chave pix`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = RegistraChavePixResponse.newBuilder()
            .setClientId(clienteId)
            .setPixId(pixId)
            .build()

        given(registraStub.registra(Mockito.any())).willReturn(respostaGrpc)


        val novaChavePix = NovaChavePixRequest(
            tipoDeConta = TipoDeContaRequest.CONTA_CORRENTE,
            chave = "teste@teste.com.br",
            tipoDeChave = TipoDeChaveRequest.EMAIL
        )

        val request = HttpRequest.POST("/api/v1/clientes/$clienteId/pix", novaChavePix)
        val response = client.toBlocking().exchange(request, NovaChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.contains(pixId))
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() =
            Mockito.mock(KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub::class.java)
    }

}