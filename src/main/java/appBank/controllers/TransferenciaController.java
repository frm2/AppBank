package appBank.controllers;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import appBank.repositories.ContaRepository;
import appBank.repositories.TransferenciaRepository;
import appBank.dto.TransferenciaDTO;
import appBank.enums.TransferenciaType;
import appBank.models.Conta;
import appBank.models.Transferencia;

import java.util.Date;


@RestController
@RequestMapping("/transf")
public class TransferenciaController {

	@Autowired
	private TransferenciaRepository transferenciaRepository;

	@Autowired
	private ContaRepository contaRepository;

	@PostMapping("/dep")
	public ResponseEntity<String> depositar(
			@RequestBody TransferenciaDTO transferenciaDTO){
		try {
			Optional<Conta> optionalConta = contaRepository.findById(
					transferenciaDTO.getIdConta());

			if (optionalConta.isEmpty()) {
				// Retorna um NotFound caso a Conta não exista
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

			Conta conta = optionalConta.get();
			
			if (conta.isBloqueado()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						"Sua conta está bloqueada");
			}

			Date currentDate = new Date();
			java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

			Transferencia transferencia = new Transferencia(
					conta, 
					TransferenciaType.DEPOSITO,
					transferenciaDTO.getValor(),
					sqlDate);

			transferenciaRepository.save(transferencia);

			return ResponseEntity.status(HttpStatus.OK).body(
					"Valor de " + transferenciaDTO.getValor() 
					+ " depositado com sucesso");
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
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

	@PostMapping("/saq")
	public ResponseEntity<String> sacar(@RequestBody TransferenciaDTO transferenciaDTO){
		try {
			Optional<Conta> optionalConta = contaRepository.findById(
					transferenciaDTO.getIdConta());

			if (optionalConta.isEmpty()) {
				// Retorna um NotFound caso a Conta não exista
				return ResponseEntity.notFound().build();
			}

			Conta conta = optionalConta.get();

			List<Transferencia> transferencias = 
					transferenciaRepository.findByConta(conta);

			BigDecimal saldo = getSaldo(transferencias);
			if (conta.isBloqueado()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						"Sua conta está bloqueada");
			}
			if (saldo.compareTo(transferenciaDTO.getValor())<0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						"Você não tem fundos o suficiente");
			}

			Date currentDate = new Date();
			java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

			Transferencia transferencia = new Transferencia(
					conta, 
					TransferenciaType.SAQUE,
					transferenciaDTO.getValor(),
					sqlDate);

			transferenciaRepository.save(transferencia);

			return ResponseEntity.status(HttpStatus.OK).body(
					"Valor de " + transferenciaDTO.getValor() + " sacado com sucesso");
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/saldo/{idConta}")
	public ResponseEntity<List<Transferencia>> getSaldo(@PathVariable long idConta) {
		try {
			Optional<Conta> optionalConta = contaRepository.findById(idConta);

			if (optionalConta.isEmpty()) {
				// Retorna um NotFound caso a Conta não exista
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

			Conta conta = optionalConta.get();
			
			List<Transferencia> transferencias = 
					transferenciaRepository.findByConta(conta);
			
			BigDecimal saldo = getSaldo(transferencias);
			System.out.println("Saldo de " + saldo);

			return ResponseEntity.status(HttpStatus.OK).body(transferencias);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/saldo/{idConta}/{ano}/{mes}")
	public ResponseEntity<List<Transferencia>> getSaldo(
			@PathVariable long idConta, 
			@PathVariable int mes, 
			@PathVariable int ano) {
		try {
			Optional<Conta> optionalConta = contaRepository.findById(idConta);

			if (optionalConta.isEmpty()) {
				// Retorna um NotFound caso a Conta não exista
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

			Conta conta = optionalConta.get();
			
			List<Transferencia> transferencias = 
					transferenciaRepository.findByConta(conta);
			
			System.out.println("Transferências pré-filtro");
			
			for (Transferencia transferencia : transferencias)
				System.out.println(transferencia.toString());

			Calendar cal = Calendar.getInstance();
			transferencias = transferencias.stream().filter(t ->{
				Date data = t.getData();
				cal.setTime(data);
				int anoAux = cal.get(Calendar.YEAR);
				int mesAux = cal.get(Calendar.MONTH) + 1;
				System.out.println(
						"mesAux: " + mesAux + ", mes: " + mes + 
						", anoAux: " + anoAux + ", ano: " + ano);
				return mesAux == mes && ano == anoAux;
			}).collect(Collectors.toList());

			System.out.println("Transferências pós-filtro");
			for (Transferencia transferencia : transferencias) {
				System.out.println(transferencia.toString());
			}
			BigDecimal saldo = getSaldo(transferencias);
			System.out.println("Saldo de " + saldo);

			return ResponseEntity.status(HttpStatus.OK).body(transferencias);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}
