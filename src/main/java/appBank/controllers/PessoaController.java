package appBank.controllers;

import java.util.ArrayList;
import java.util.List;

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
	public ResponseEntity<String> addPerson(@RequestBody Pessoa pessoa) {
	    // Validate CPF
	    if (!pessoa.isCPFValid()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF Inválido");
	    }
	    
	    // Save person to database
	    pessoaRepository.save(pessoa);
	    
	    return ResponseEntity.status(HttpStatus.CREATED).body("Pessoa adicionada com êxito");
	}

}
