package com.example.sa4.controller;

public interface ContaCorrente {

    String sacar(Double valor, Long conta);

    String depositar(Double valor, Long conta);

    String transferir(Long contaOrigem, Long contaDestino, Double valor);

    String consultarSaldo(Long conta);

}
