package appBank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import appBank.models.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long>{

}
