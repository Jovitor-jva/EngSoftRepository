package com.unicap.os.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unicap.os.domain.PrestadorDeServico;

@Repository
public interface PrestadorRepository extends JpaRepository<PrestadorDeServico, Integer>{

	@Query("SELECT obj FROM PrestadorDeServico obj WHERE obj.cpf =:cpf")
	PrestadorDeServico findByCpf(@Param("cpf") String cpf);
}
