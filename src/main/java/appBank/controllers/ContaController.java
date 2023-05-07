package appBank.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import appBank.models.Conta;
import appBank.models.Pessoa;
import appBank.repositories.ContaRepository;
import appBank.repositories.PessoaRepository;

@RestController
@RequestMapping("/conta")
public class ContaController {

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@GetMapping("/")
	public ResponseEntity<List<Conta>> getAllContas() {
		List<Conta> contas = contaRepository.findAll();
		return ResponseEntity.ok().body(contas);
	}
	/*/
	 * Retorna um número aleatório entre 1-10 para representar a agência da pessoa
	 * @return
	 */
	private int getAgencia() {
		Random random = new Random();
		return random.nextInt(10) + 1;
	}

	@PostMapping("/{cpf}")
	public ResponseEntity<String> createConta(@PathVariable String cpf) {
		try {	
			Pessoa pessoa = pessoaRepository.findBycpf(cpf).orElse(null);
			if (pessoa == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			Conta conta = new Conta(pessoa, getAgencia(), false);
			contaRepository.save(conta);
			return ResponseEntity.status(HttpStatus.OK).body("Conta criada com êxito");
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteConta(@PathVariable Long id) {
		try {
			Optional<Conta> optionalConta = contaRepository.findById(id);
			if (optionalConta.isEmpty()) {
				// Retorna um NotFound caso a Conta não exista
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

			Conta conta = optionalConta.get();
			// Remove a Conta da lista de Contas da Pessoa
			Pessoa pessoa = conta.getPessoa();
			pessoa.getContas().remove(conta);
			pessoaRepository.save(pessoa);
			contaRepository.delete(conta);
			return ResponseEntity.status(HttpStatus.OK).body("Conta deletada com êxito");
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/block/{id}")
	public ResponseEntity<String> bloquearConta(@PathVariable Long id) {
		try {
			Optional<Conta> optionalConta = contaRepository.findById(id);
			if (optionalConta.isEmpty()) {
				// Retorna um NotFound caso a Conta não exista
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

			Conta conta = optionalConta.get();

			if(conta.isBloqueado()) 
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						"Conta já está bloqueada");

			conta.setBloqueado(true);
			contaRepository.save(conta);
			return ResponseEntity.status(HttpStatus.OK).body("Conta bloqueada com êxito");
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/unblock/{id}")
	public ResponseEntity<String> desbloquearConta(@PathVariable Long id) {
		try {
			Optional<Conta> optionalConta = contaRepository.findById(id);
			if (optionalConta.isEmpty()) {
				// Retorna um NotFound caso a Conta não exista
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

			Conta conta = optionalConta.get();

			if(!conta.isBloqueado()) 
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						"Conta já está desbloqueada");
			
			conta.setBloqueado(false);
			contaRepository.save(conta);
			return ResponseEntity.status(HttpStatus.OK).body("Conta desbloqueada com êxito");
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}