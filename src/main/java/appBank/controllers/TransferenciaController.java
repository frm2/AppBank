package appBank.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import appBank.repositories.ContaRepository;
import appBank.repositories.PessoaRepository;
import appBank.repositories.TransferenciaRepository;
import appBank.dto.TransferenciaDTO;
import appBank.enums.TransferenciaType;
import appBank.models.Conta;
import appBank.models.Pessoa;
import appBank.models.Transferencia;

import java.util.Date;


@RestController
@RequestMapping("/transf")
public class TransferenciaController {
	
	@Autowired
	private TransferenciaRepository transferenciaRepository;
	
	@Autowired
	private ContaRepository contaRepository;
	
	@PutMapping("/dep")
	public ResponseEntity<String> depositar(@RequestBody TransferenciaDTO transferenciaDTO){

		Optional<Conta> optionalConta = contaRepository.findById(transferenciaDTO.getIdConta());
		
		if (optionalConta.isEmpty()) {
			// Retorna um NotFound caso a Conta não exista
			return ResponseEntity.notFound().build();
		}
		
		Conta conta = optionalConta.get();

		Date currentDate = new Date();
		java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
		
		Transferencia transferencia = new Transferencia(
				conta, 
				TransferenciaType.DEPOSITO,
				transferenciaDTO.getValor(),
				sqlDate);
		
		transferenciaRepository.save(transferencia);
		
		return ResponseEntity.ok().body(
				"Valor de " + transferenciaDTO.getValor() + " depositado com sucesso");
	}
	
	private BigDecimal getSaldo(List<Transferencia> transferencias) {
		BigDecimal saldo = BigDecimal.ZERO;
		for (Transferencia transferencia: transferencias) {
			if (transferencia.getTransferenciaType().equals(TransferenciaType.DEPOSITO))
				saldo = saldo.add(transferencia.getValor());
			else
				saldo = saldo.subtract(transferencia.getValor());
		}
		return saldo;
	}
	
	@PutMapping("/saq")
	public ResponseEntity<String> sacar(@RequestBody TransferenciaDTO transferenciaDTO){

		Optional<Conta> optionalConta = contaRepository.findById(transferenciaDTO.getIdConta());
		
		if (optionalConta.isEmpty()) {
			// Retorna um NotFound caso a Conta não exista
			return ResponseEntity.notFound().build();
		}
				
		Conta conta = optionalConta.get();
		
		List<Transferencia> transferencias = transferenciaRepository.findByConta(conta);
		
		BigDecimal saldo = getSaldo(transferencias);
		if (saldo.compareTo(transferenciaDTO.getValor())<0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Você não tem fundos o suficiente");
		}

		Date currentDate = new Date();
		java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
		
		Transferencia transferencia = new Transferencia(
				conta, 
				TransferenciaType.SAQUE,
				transferenciaDTO.getValor(),
				sqlDate);
		
		transferenciaRepository.save(transferencia);
		
		return ResponseEntity.ok().body(
				"Valor de " + transferenciaDTO.getValor() + " sacado com sucesso");
	}
	
	//TODO: está dando erro e eu não entendo porquê
	@GetMapping("/saldo/{idConta}")
	public ResponseEntity<Transferencia> getSaldo(@PathVariable long idConta) {
		Optional<Conta> optionalConta = contaRepository.findById(idConta);
		
		if (optionalConta.isEmpty()) {
			// Retorna um NotFound caso a Conta não exista
			return ResponseEntity.notFound().build();
		}
		
		Conta conta = optionalConta.get();
		
		List<Transferencia> transferencias = transferenciaRepository.findByConta(conta);
		
		BigDecimal saldo = getSaldo(transferencias);
		
		System.out.println("saldo de " + saldo);
		
//		for (Transferencia transferencia : transferencias) {
//			System.out.println(transferencias.toString());
//		}
		
		return ResponseEntity.ok().body(transferencias.get(0));
	}
	
	
	
}
