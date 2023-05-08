package appBank;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import appBank.controllers.TransferenciaController;
import appBank.dto.TransferenciaDTO;
import appBank.enums.TransferenciaType;
import appBank.models.Conta;
import appBank.models.Pessoa;
import appBank.models.Transferencia;
import appBank.repositories.ContaRepository;
import appBank.repositories.PessoaRepository;
import appBank.repositories.TransferenciaRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@RunWith(MockitoJUnitRunner.class)
public class TransferenciaControllerTests {

	@Mock
	private ContaRepository contaRepository;

	@Mock
	private PessoaRepository pessoaRepository;

	@Mock
	private TransferenciaRepository transferenciaRepository;

	@InjectMocks
	private TransferenciaController transferenciaController;

	@Test
	public void deveDepositar() {
		//Dado que
		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.", new Date(1));
		Conta conta = new Conta(pessoa, 1, false);
		BigDecimal valor = BigDecimal.TEN;
		Transferencia transferencia = new Transferencia(
				conta,TransferenciaType.DEPOSITO,valor,new Date(1));
		TransferenciaDTO transferenciaDTO = new TransferenciaDTO(conta.getId(), valor);
		Mockito.when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
		Mockito.when(transferenciaRepository.save(transferencia)).thenReturn(transferencia);

		//Quando
		ResponseEntity<String> result = transferenciaController.depositar(transferenciaDTO);


		//Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(
				HttpStatus.OK);
		Assertions.assertThat(result.getBody()).matches("Valor de " 
				+ transferenciaDTO.getValor() + " depositado com sucesso");

	}

	@Test
	public void naoDeveDepositarContaNaoExistente() {
		//Dado que
		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.", new Date(1));
		Conta conta = new Conta(pessoa, 1, false);
		BigDecimal valor = BigDecimal.TEN;
		Transferencia transferencia = new Transferencia(
				conta,TransferenciaType.DEPOSITO,valor,new Date(1));
		TransferenciaDTO transferenciaDTO = new TransferenciaDTO(conta.getId(), valor);
		Mockito.when(contaRepository.findById(conta.getId())).thenReturn(Optional.empty());
		Mockito.when(transferenciaRepository.save(transferencia)).thenReturn(transferencia);

		//Quando
		ResponseEntity<String> result = transferenciaController.depositar(transferenciaDTO);

		//Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(
				HttpStatus.NOT_FOUND);

	}


	@Test
	public void naoDeveDepositarContaBloqueada() {
		//Dado que
		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.", new Date(1));
		Conta conta = new Conta(pessoa, 1, false);
		BigDecimal valor = BigDecimal.TEN;
		Transferencia transferencia = new Transferencia(
				conta,TransferenciaType.DEPOSITO,valor,new Date(1));
		TransferenciaDTO transferenciaDTO = new TransferenciaDTO(conta.getId(), valor);
		Mockito.when(contaRepository.findById(conta.getId())).thenReturn(Optional.empty());
		Mockito.when(transferenciaRepository.save(transferencia)).thenReturn(transferencia);

		//Quando
		ResponseEntity<String> result = transferenciaController.depositar(transferenciaDTO);

		//Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(
				HttpStatus.NOT_FOUND);

	}

	@Test
	public void deveSacar() {
		//Dado que
		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.", new Date(1));
		Conta conta = new Conta(pessoa, 1, false);
		BigDecimal valor = BigDecimal.TEN;
		Transferencia transferencia1 = new Transferencia(
				conta,TransferenciaType.DEPOSITO,valor,new Date(1));
		conta.setTransferencias(List.of(transferencia1));
		Transferencia transferencia2 = new Transferencia(
				conta,TransferenciaType.SAQUE,valor,new Date(1));
		TransferenciaDTO transferenciaDTO = new TransferenciaDTO(conta.getId(), valor);
		Mockito.when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
		Mockito.when(transferenciaRepository.findByConta(conta)).thenReturn(conta.getTransferencias());
		Mockito.when(transferenciaRepository.save(transferencia2)).thenReturn(transferencia2);

		//Quando
		ResponseEntity<String> result = transferenciaController.sacar(transferenciaDTO);


		//Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(
				HttpStatus.OK);
		Assertions.assertThat(result.getBody()).matches("Valor de " 
				+ transferenciaDTO.getValor() + " sacado com sucesso");

	}

	@Test
	public void naoDeveSacarSemSaldo() {
		//Dado que
		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.", new Date(1));
		Conta conta = new Conta(pessoa, 1, false);
		BigDecimal valor = BigDecimal.TEN;
		Transferencia transferencia2 = new Transferencia(
				conta,TransferenciaType.SAQUE,valor,new Date(1));
		TransferenciaDTO transferenciaDTO = new TransferenciaDTO(conta.getId(), valor);
		Mockito.when(
				contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
		Mockito.when(
				transferenciaRepository.save(transferencia2)).thenReturn(transferencia2);

		//Quando
		ResponseEntity<String> result = transferenciaController.sacar(transferenciaDTO);


		//Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(
				HttpStatus.BAD_REQUEST);
		Assertions.assertThat(result.getBody()).matches("Você não tem fundos o suficiente");

	}

	@Test
	public void naoDeveSacarContaBloqueada() {
		//Dado que
		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.", new Date(1));
		Conta conta = new Conta(pessoa, 1, true);
		BigDecimal valor = BigDecimal.TEN;
		Transferencia transferencia = new Transferencia(
				conta,TransferenciaType.SAQUE,valor,new Date(1));
		TransferenciaDTO transferenciaDTO = new TransferenciaDTO(conta.getId(), valor);
		Mockito.when(
				contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
		Mockito.when(
				transferenciaRepository.save(transferencia)).thenReturn(transferencia);

		//Quando
		ResponseEntity<String> result = transferenciaController.sacar(transferenciaDTO);


		//Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(
				HttpStatus.BAD_REQUEST);
		Assertions.assertThat(result.getBody()).matches("Sua conta está bloqueada");

	}
	
	@Test
	public void deveRetornarSaldo() {
		//Dado que
		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.", new Date(1));
		Conta conta = new Conta(pessoa, 1, true);
		BigDecimal valor = BigDecimal.TEN;
		Transferencia transferencia1 = new Transferencia(
				conta,TransferenciaType.DEPOSITO,valor,new Date(1));
		List<Transferencia> transferencias = List.of(transferencia1);
		conta.setTransferencias(transferencias);
		Mockito.when(contaRepository
				.findById(conta.getId())).thenReturn(Optional.of(conta));
		Mockito.when(transferenciaRepository.findByConta(conta)).thenReturn(transferencias);
		//Quando
		ResponseEntity<List<Transferencia>> result =
				transferenciaController.getSaldo(conta.getId());

		//Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(
				HttpStatus.OK);
		Assertions.assertThat(result.getBody()).isEqualTo(transferencias);
	}
	
	@Test
	public void naoDeveRetornarSaldoContaNaoExiste() {
		//Dado que
		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.", new Date(1));
		Conta conta = new Conta(pessoa, 1, true);
		BigDecimal valor = BigDecimal.TEN;
		Mockito.when(contaRepository
				.findById(conta.getId())).thenReturn(Optional.empty());

		//Quando
		ResponseEntity<List<Transferencia>> result =
				transferenciaController.getSaldo(conta.getId());

		//Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(
				HttpStatus.NOT_FOUND);
	}

}
