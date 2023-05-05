package appBank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import appBank.models.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
}
