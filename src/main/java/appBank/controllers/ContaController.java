package appBank.controllers;

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
import appBank.models.Conta;
import appBank.models.Pessoa;

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
				return ResponseEntity.notFound().build();
			}
			Conta conta = new Conta(pessoa, getAgencia(), false);
			contaRepository.save(conta);
			//			System.out.println(conta.toString());
			return ResponseEntity.ok().body("Conta criada com êxito");
		} catch (Exception e) {
			System.out.println("deu xabu");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteConta(@PathVariable Long id) {
		Optional<Conta> optionalConta = contaRepository.findById(id);
		if (optionalConta.isEmpty()) {
			// Retorna um NotFound caso a Conta não exista
			return ResponseEntity.notFound().build();
		}

		Conta conta = optionalConta.get();
		// Remove a Conta da lista de Contas da Pessoa
		Pessoa pessoa = conta.getPessoa();
		pessoa.getContas().remove(conta);
		pessoaRepository.save(pessoa);
		contaRepository.delete(conta);
		return ResponseEntity.ok().body("Conta deletada com êxito");
	}

	@PutMapping("/block/{id}")
	public ResponseEntity<String> bloquearConta(@PathVariable Long id) {

		Optional<Conta> optionalConta = contaRepository.findById(id);
		if (optionalConta.isEmpty()) {
			// Retorna um NotFound caso a Conta não exista
			return ResponseEntity.notFound().build();
		}

		Conta conta = optionalConta.get();

		if(conta.isBloqueado()) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Conta já está bloqueada");

		conta.setBloqueado(true);
		contaRepository.save(conta);
		return ResponseEntity.ok().body("Conta bloqueada com êxito");
	}
	
	@PutMapping("/unblock/{id}")
	public ResponseEntity<String> desbloquearConta(@PathVariable Long id) {

		Optional<Conta> optionalConta = contaRepository.findById(id);
		if (optionalConta.isEmpty()) {
			// Retorna um NotFound caso a Conta não exista
			return ResponseEntity.notFound().build();
		}

		Conta conta = optionalConta.get();

		if(!conta.isBloqueado()) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Conta já está desbloqueada");

		conta.setBloqueado(false);
		contaRepository.save(conta);
		return ResponseEntity.ok().body("Conta desbloqueada com êxito");
	}


}