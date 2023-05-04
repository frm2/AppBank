package appBank.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import appBank.repositories.PessoaRepository;
import appBank.models.Pessoa;

//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/pessoa")
public class PessoaController {

	@Autowired
	PessoaRepository pessoaRepository;

	@GetMapping("/pessoas")
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

	@PostMapping("/pessoas")
	public ResponseEntity<Pessoa> createPessoa(@RequestBody Pessoa pessoa) {
		//System.out.println(pessoa.getCpf() + ", " + pessoa.getNome() + ", " + pessoa.getDataNascimento().toString());
		try {
			Pessoa _pessoa = pessoaRepository.save(new Pessoa(pessoa.getCpf(), pessoa.getNome(), pessoa.getDataNascimento()));
			return new ResponseEntity<>(_pessoa, HttpStatus.CREATED);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
