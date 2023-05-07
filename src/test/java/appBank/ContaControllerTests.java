package appBank;

import static org.mockito.Mockito.times;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
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

import appBank.controllers.ContaController;
import appBank.models.Conta;
import appBank.models.Pessoa;
import appBank.repositories.ContaRepository;
import appBank.repositories.PessoaRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@RunWith(MockitoJUnitRunner.class)
public class ContaControllerTests {

	@Mock
	private ContaRepository contaRepository;

	@Mock
	private PessoaRepository pessoaRepository;

	@InjectMocks
	private ContaController contaController;

	@Test
	public void deveCriarConta() {
		//Dado que
		Conta conta = new Conta(
				new Pessoa("089.269.990-68", "Joao Teste Jr.",new Date(1))
				, 1, false);
		Mockito.when(pessoaRepository.findBycpf(
				conta.getPessoa().getCpf())).thenReturn(Optional.of(conta.getPessoa()));
		Mockito.when(contaRepository.save(conta)).thenReturn(conta);

		//Quando
		ResponseEntity<String> result = contaController.createConta(
				conta.getPessoa().getCpf());

		//Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(
				HttpStatus.OK);
		Assertions.assertThat(result.getBody()).matches("Conta criada com êxito");
	}

	@Test
	public void naoDeveCriarConta() {
		//Dado que
		Conta conta = new Conta(
				new Pessoa("089.269.990-68", "Joao Teste Jr.",new Date(1))
				, 1, false);
		Mockito.when(contaRepository
				.save(conta)).thenReturn(conta);
		Mockito.when(pessoaRepository
				.findBycpf(Mockito.anyString())).thenReturn(Optional.empty());

		//Quando
		ResponseEntity<String> result = contaController.createConta(
				conta.getPessoa().getCpf());

		//Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(
				HttpStatus.NOT_FOUND);
	}

	@Test
	public void deveDeletarConta() {
		// Dado que
		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.",new Date(1));
		Conta conta = new Conta(pessoa, 13, false);
		List<Conta> contas = new ArrayList<Conta>();
		contas.add(conta);
		pessoa.setContas(contas);

		Mockito.when(contaRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(conta));
		Mockito.when(pessoaRepository.save(pessoa)).thenReturn(pessoa);

		// Quando
		ResponseEntity<String> result = contaController.deleteConta(conta.getId());

		// Então
		Mockito.verify(contaRepository, Mockito.times(1)).delete(conta);
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(result.getBody()).isEqualTo("Conta deletada com êxito");
	}

	@Test
	public void naoDeveDeletarContaQueNaoExiste() {
		// Dado que
		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.",new Date(1));
		Conta conta = new Conta(pessoa, 13, false);
		List<Conta> contas = new ArrayList<Conta>();
		contas.add(conta);
		pessoa.setContas(contas);

		Mockito.when(contaRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		Mockito.when(pessoaRepository.save(pessoa)).thenReturn(pessoa);

		// Quando
		ResponseEntity<String> result = contaController.deleteConta(conta.getId());

		// Então
		Mockito.verify(contaRepository, Mockito.never()).delete(conta);
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void naoBloquearContaQueNaoExiste() {
		// Dado que
		Long id = 1L;
		Mockito.when(contaRepository.findById(id)).thenReturn(Optional.empty());

		// Quando
		ResponseEntity<String> result = contaController.bloquearConta(id);

		// Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		Mockito.verify(contaRepository, Mockito.never()).save(Mockito.any(Conta.class));
	}

	@Test
	public void naoBloquearContaJaBloqueada() {
		// Dado que
		Conta conta = new Conta();
		conta.setBloqueado(true);
		Mockito.when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));

		// Quanto
		ResponseEntity<String> result = contaController.bloquearConta(conta.getId());

		// Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Mockito.verify(contaRepository, Mockito.never()).save(Mockito.any(Conta.class));
	}

	@Test
	public void deveBloquearConta() {
		// Dado que
		Conta conta = new Conta();
		Mockito.when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));

		// Quanto
		ResponseEntity<String> result = contaController.bloquearConta(conta.getId());

		// Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(result.getBody()).isEqualTo("Conta bloqueada com êxito");
		Assertions.assertThat(conta.isBloqueado()).isTrue();
		Mockito.verify(contaRepository).save(conta);
	}
	
	@Test
	public void naoDesbloquearContaQueNaoExiste() {
		// Dado que
		Long id = 1L;
		Mockito.when(contaRepository.findById(id)).thenReturn(Optional.empty());

		// Quando
		ResponseEntity<String> result = contaController.desbloquearConta(id);

		// Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		Mockito.verify(contaRepository, Mockito.never()).save(Mockito.any(Conta.class));
	}

	@Test
	public void naoDesbloquearContaJaDesbloqueada() {
		// Dado que
		Conta conta = new Conta();
		conta.setBloqueado(false);
		Mockito.when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));

		// Quanto
		ResponseEntity<String> result = contaController.desbloquearConta(conta.getId());

		// Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Mockito.verify(contaRepository, Mockito.never()).save(Mockito.any(Conta.class));
	}

	@Test
	public void deveDesbloquearConta() {
		// Dado que
		Conta conta = new Conta();
		conta.setBloqueado(true);
		Mockito.when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));

		// Quanto
		ResponseEntity<String> result = contaController.desbloquearConta(conta.getId());

		// Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(result.getBody()).isEqualTo("Conta desbloqueada com êxito");
		Assertions.assertThat(conta.isBloqueado()).isFalse();
		Mockito.verify(contaRepository).save(conta);
	}


}
