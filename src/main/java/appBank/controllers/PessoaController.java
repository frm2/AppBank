package appBank.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import appBank.repositories.PessoaRepository;
import appBank.models.Pessoa;

//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/pessoa")
public class PessoaController {

	@Autowired
	PessoaRepository pessoaRepository;

	@GetMapping("/")
	public ResponseEntity<List<Pessoa>> getAllPessoas(@RequestParam(required = false) String cpf) {
		try {
			List<Pessoa> pessoas = new ArrayList<Pessoa>();

			if (cpf == null)
				pessoaRepository.findAll().forEach(pessoas::add);

			if (pessoas.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(pessoas, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/")
	public ResponseEntity<String> addPessoa(@RequestBody Pessoa pessoa) {
		try {
			if(pessoaRepository.existsBycpf(pessoa.getCpf())) {
				return ResponseEntity.badRequest().body("Uma Pessoa com esse CPF já existe");
			}

			if (!pessoa.isCPFValid()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF Inválido");
			}

			pessoaRepository.save(pessoa);

			return ResponseEntity.status(HttpStatus.CREATED).body("Pessoa adicionada com êxito");

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@DeleteMapping("/{cpf}")
	public ResponseEntity<String> deletePessoa(@PathVariable String cpf) {
		try {
			Optional<Pessoa> optionalPerson = pessoaRepository.findBycpf(cpf);
			if (optionalPerson.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Não foi encontrado pessoa com CPF: " + cpf);
			}
			
			Pessoa pessoa= optionalPerson.get();
			pessoaRepository.delete(pessoa);
			return ResponseEntity.ok("Pessoa com CPF " + cpf + " deletada com êxito");
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
