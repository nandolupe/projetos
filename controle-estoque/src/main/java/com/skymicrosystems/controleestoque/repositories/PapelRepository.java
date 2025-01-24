package com.skymicrosystems.controleestoque.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skymicrosystems.controleestoque.enums.StatusEnum;
import com.skymicrosystems.controleestoque.enums.TipoAcessoEnum;
import com.skymicrosystems.controleestoque.model.Papel;

@Repository
public interface PapelRepository extends JpaRepository<Papel, Long> {
	public List<Papel> findByStatus(StatusEnum status);
	public List<Papel> findByStatusAndTipoAcesso(StatusEnum status, TipoAcessoEnum tipoAcesso);
	
	public Page<Papel> findAll(Pageable pageable);
	
	public List<Papel> findByNome(String nome);
}
