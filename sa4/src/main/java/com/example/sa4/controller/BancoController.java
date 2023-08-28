package com.example.sa4.controller;

import com.example.sa4.model.AccountType;
import com.example.sa4.model.ContaCorrentePF;
import com.example.sa4.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class BancoController implements ContaCorrente{

    @Autowired
    private BancoRepository bancoRepository;

    @Autowired
    private Controller controller;

    private Long number = 0L;

    public void delete(String name){
        bancoRepository.delete(this.consultaConta(name));
    }

    public ContaCorrentePF criarConta(String name, String accountType) throws Exception {
        ContaCorrentePF contaCorrentePF = new ContaCorrentePF();
        StringBuilder message = new StringBuilder();
        if(accountType == null){
            message.append("Por favor, insera o tipo de conta.  ");
        }
        switch (accountType){
            case "POUPANCA" :
                contaCorrentePF.setAccountType(AccountType.CONTA_POUPANCA);
                break;
            case "CORRENTE" :
                contaCorrentePF.setAccountType(AccountType.CONTA_CORRENTE);
                break;
            default:
                message.append("Tipo da conta esta incorreto.  ");
        }

        Person person = controller.findPerson(name);
        if (person != null && contaCorrentePF.getError() == null){
            number++;
            contaCorrentePF.setNumeroConta(number);
            contaCorrentePF.setPerson(person);
            bancoRepository.save(contaCorrentePF);
        }else if(contaCorrentePF.getError() == null){
            message.append("  Pessoa ");
            message.append(name).append(" não cadastrada.");
        }
        if(!message.isEmpty()){
            contaCorrentePF.setError(message.toString());
        }

        return contaCorrentePF;
    }

    public ContaCorrentePF consultaConta(String name){

        List<ContaCorrentePF> contas = (List<ContaCorrentePF>) bancoRepository.findAll();
        for(ContaCorrentePF cc : contas){
            if(cc.getPerson() != null && cc.getPerson().getName().equals(name)){
                return cc;
            }
        }
        return null;
    }
    @Override
    public String consultarSaldo(Long conta) {
        String message = "";
        ContaCorrentePF extrato = bancoRepository.findById(conta).get();
        return message + "Saldo da conta número "+ extrato.getNumeroConta() + " de "+ extrato.getPerson().getName() + " é de R$" + extrato.getSaldo() + ".";
    }

    @Override
    public String depositar( Double valor, Long conta) {
        String message = "";
        ContaCorrentePF C = bancoRepository.findById(conta).get();
        C.setSaldo(C.getSaldo() + valor);
        bancoRepository.save(C);
        return message = message + " O valor de R$" + valor +" depositados na conta de " + C.getPerson().getName() + ".";
    }

    @Override
    public String transferir(Long contaOrigem, Long contaDestino, Double valor) {
        String message = "";
        ContaCorrentePF destino = bancoRepository.findById(contaDestino).get();
        ContaCorrentePF origem = bancoRepository.findById(contaOrigem).get();

        if(origem.getSaldo() >= valor){
            destino.setSaldo(destino.getSaldo() + valor);
            origem.setSaldo(origem.getSaldo() - valor);

            if (origem.getAccountType() != destino.getAccountType()){
                origem.setSaldo(origem.getSaldo());
            }

            bancoRepository.save(destino);
            bancoRepository.save(origem);
            message = message + origem.getPerson().getName() + "vc recebeu o valor de R$ " + valor + " pagador" + destino.getPerson().getName() + ".";

        }else{
            message = message + " Seu saldo é insuficiente para esta operação.";
        }
        return message;
    }

    @Override
    public String sacar(Double valor, Long conta) {
        String message = "";
        ContaCorrentePF saque = bancoRepository.findById(conta).get();

        if (saque.getSaldo() >= valor) {
            saque.setSaldo(saque.getSaldo() - valor);
            bancoRepository.save(saque);
            message = message + "R$" + valor + " sacado com sucesso.";
            ;

        } else {
            message = message + "Saldo insuficiente para esta operaçao.";
        }

        return message;
    }
}
