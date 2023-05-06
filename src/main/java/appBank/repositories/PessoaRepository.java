package appBank.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import appBank.models.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
	
	boolean existsBycpf(String cpf);
	Optional<Pessoa> findBycpf(String cpf);
	
}
