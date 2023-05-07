package appBank.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import appBank.models.Conta;
import appBank.models.Transferencia;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long>{
	
	List<Transferencia> findByConta(Conta conta);
}
