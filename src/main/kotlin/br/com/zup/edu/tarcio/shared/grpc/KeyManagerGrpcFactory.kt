package br.com.zup.edu.tarcio.shared.grpc

import br.com.zup.edu.tarcio.KeyManagerDeletaServiceGrpc
import br.com.zup.edu.tarcio.KeyManagerRegistraServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("KeyManager") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() = KeyManagerRegistraServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun deletaChave() = KeyManagerDeletaServiceGrpc.newBlockingStub(channel)
}