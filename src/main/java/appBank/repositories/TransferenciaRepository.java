package appBank.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import appBank.models.Conta;
import appBank.models.Transferencia;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long>{
	
	List<Transferencia> findByConta(Conta conta);
	//TODO:talvez conta ao invés de Conta?
}
