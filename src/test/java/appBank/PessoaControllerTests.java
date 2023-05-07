package appBank;

import java.sql.Date;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import appBank.controllers.PessoaController;
import appBank.models.Pessoa;
import appBank.repositories.PessoaRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@RunWith(MockitoJUnitRunner.class)
public class PessoaControllerTests {

	@Mock
	private PessoaRepository pessoaRepository;

	@InjectMocks
	private PessoaController pessoaController;

	@Test
	public void deveSalvarPessoa() throws Exception {
		//Dado que
		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.", new Date(1));
		Mockito.when(pessoaRepository.save(pessoa)).thenReturn(pessoa);

		//Quando
		ResponseEntity<String> result = pessoaController.addPessoa(pessoa);

		//Então
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}

	@Test
	public void naoDeveSalvarPessoaComCPFInvalido() throws Exception {
		//Dado que
		Pessoa pessoa = new Pessoa("999.999.999-99", "Joao Teste Jr.", new Date(1));

		//Quando
		ResponseEntity<String> result = pessoaController.addPessoa(pessoa);

		//Então
		Mockito.verify(pessoaRepository, Mockito.never()).save(
				ArgumentMatchers.any(Pessoa.class));
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Assertions.assertThat(result.getBody()).matches("CPF Inválido");
	}

	@Test
	public void naoDeveSalvarPessoaComCPFRepetido() throws Exception{
		//Dado que
		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.", new Date(1));
		Mockito.when(pessoaRepository.existsBycpf(pessoa.getCpf())).thenReturn(true);

		//Quando
		ResponseEntity<String> result = pessoaController.addPessoa(pessoa);

		//Então
		Mockito.verify(pessoaRepository, Mockito.never()).save(
				ArgumentMatchers.any(Pessoa.class));
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Assertions.assertThat(result.getBody()).matches("Uma Pessoa com esse CPF já existe");
	}

	@Test
	public void deveApagarPessoa() throws Exception{
		//Dado que
		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.", new Date(1));
		Mockito.when(pessoaRepository.findBycpf(
				pessoa.getCpf())).thenReturn(Optional.of(pessoa));

		//Quando
		ResponseEntity<String> result = pessoaController.deletePessoa(pessoa.getCpf());

		//Então
		Mockito.verify(pessoaRepository, Mockito.times(1)).delete(pessoa);
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(result.getBody()).matches(
				"Pessoa com CPF " + pessoa.getCpf() + " deletada com êxito");
	}
	
	@Test
	public void naoDeveApagarPessoaQueNaoExiste() throws Exception{
		//Dado que
//		Pessoa pessoa = new Pessoa("089.269.990-68", "Joao Teste Jr.", new Date(1));
//		Mockito.when(pessoaRepository.findBycpf(
//				pessoa.getCpf())).thenReturn(null);
		String cpf = "123.456.789-00";
	    Mockito.when(pessoaRepository.findBycpf(cpf)).thenReturn(Optional.empty());
		

		//Quando
		ResponseEntity<String> result = pessoaController.deletePessoa(cpf);

		//Então
		Mockito.verify(pessoaRepository, Mockito.never()).delete(
				ArgumentMatchers.any(Pessoa.class));
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		Assertions.assertThat(result.getBody()).matches(
				"Não foi encontrado pessoa com CPF: " + cpf);
	}

}
