package br.com.zup.edu.tarcio.lista

import br.com.zup.edu.tarcio.*
import br.com.zup.edu.tarcio.KeyManagerListaServiceGrpc
import br.com.zup.edu.tarcio.shared.grpc.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListaChavePixControllerTest{

    @field:Inject
    lateinit var listaChaveStub: KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve listar todas as chaves pix existente`() {

        val clienteId = UUID.randomUUID().toString()

        val respostaGrpc = listaChavePixResponse(clienteId)

        given(listaChaveStub.lista(Mockito.any())).willReturn(respostaGrpc)


        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/")
        val response = client.toBlocking().exchange(request, List::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.status)
        Assertions.assertNotNull(response.body())
        Assertions.assertEquals(response.body()!!.size, 2)
    }

    private fun listaChavePixResponse(clientId: String): ListaChavesPixResponse {
        val CHAVE_EMAIL = "teste@teste.com.br"
        val CHAVE_CELULAR = "+5511912345678"
        val CONTA_CORRENTE = TipoDeConta.CONTA_CORRENTE
        val TIPO_DE_CHAVE_EMAIL = TipoDeChave.EMAIL
        val TIPO_DE_CHAVE_CELULAR = TipoDeChave.PHONE
        val CHAVE_CRIADA_EM = LocalDateTime.now()

        val chaveEmail = ListaChavesPixResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(TIPO_DE_CHAVE_EMAIL)
            .setChave(CHAVE_EMAIL)
            .setTipoDeConta(CONTA_CORRENTE)
            .setCriadaEm(CHAVE_CRIADA_EM.let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        val chaveCelular = ListaChavesPixResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(TIPO_DE_CHAVE_CELULAR)
            .setChave(CHAVE_CELULAR)
            .setTipoDeConta(CONTA_CORRENTE)
            .setCriadaEm(CHAVE_CRIADA_EM.let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()


        return ListaChavesPixResponse.newBuilder()
            .setClientId(clientId)
            .addAllChaves(listOf(chaveEmail, chaveCelular))
            .build()
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubDetalhesMock() =
            Mockito.mock(KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub::class.java)
    }
}