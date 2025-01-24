package com.skymicrosystems.controleestoque.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import com.skymicrosystems.controleestoque.enums.EventoEnum;
import com.skymicrosystems.controleestoque.model.Notificacao;


@Component
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> , JpaSpecificationExecutor<Notificacao>{
	
	public Optional<Notificacao> findByChaveAndEvento(String chave, EventoEnum evento);
	public void deleteByChaveAndEvento(String chave, EventoEnum evento);
}
