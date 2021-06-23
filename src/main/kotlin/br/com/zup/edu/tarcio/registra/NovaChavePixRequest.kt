package br.com.zup.edu.tarcio.registra

import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import br.com.caelum.stella.validation.CPFValidator
import br.com.zup.edu.tarcio.RegistraChavePixRequest
import br.com.zup.edu.tarcio.TipoDeChave
import br.com.zup.edu.tarcio.TipoDeConta
import br.com.zup.edu.tarcio.shared.validation.ValidPixKey

@ValidPixKey
@Introspected
class NovaChavePixRequest(
    @field:NotNull val tipoDeConta: TipoDeContaRequest?,
    @field:Size(max = 77) val chave: String?,
    @field:NotNull val tipoDeChave: TipoDeChaveRequest?
) {

    fun paraModeloGrpc(clienteId: UUID): RegistraChavePixRequest {
        return RegistraChavePixRequest.newBuilder()
            .setClientId(clienteId.toString())
            .setTipoDeConta(tipoDeConta?.atributoGrpc ?: TipoDeConta.UNKNOWN_TIPO_DE_CONTA)
            .setTipoDeChave(tipoDeChave?.atributoGrpc ?: TipoDeChave.UNKNOWN_TIPO_CHAVE)
            .setChave(chave ?: "")
            .build()
    }
}

enum class TipoDeChaveRequest(val atributoGrpc: TipoDeChave) {

    CPF(TipoDeChave.CPF) {

        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            return CPFValidator(false)
                .invalidMessagesFor(chave)
                .isEmpty()
        }

    },

    PHONE(TipoDeChave.PHONE) {
        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }
            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },

    EMAIL(TipoDeChave.EMAIL) {

        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }
            return chave.matches("^[a-z0-9.]+@[a-z0-9]+\\.[a-z]+\\.?([a-z]+)?\$".toRegex())

        }
    },

    RANDOM(TipoDeChave.RANDOM) {
        override fun valida(chave: String?) = chave.isNullOrBlank() // não deve se preenchida
    };

    abstract fun valida(chave: String?): Boolean
}

enum class TipoDeContaRequest(val atributoGrpc: TipoDeConta) {

    CONTA_CORRENTE(TipoDeConta.CONTA_CORRENTE),

    CONTA_POUPANCA(TipoDeConta.CONTA_POUPANCA)
}