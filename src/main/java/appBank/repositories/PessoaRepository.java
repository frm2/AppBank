package appBank.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import appBank.models.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
	List<Pessoa> findByNomeContaining(String nome);
}
