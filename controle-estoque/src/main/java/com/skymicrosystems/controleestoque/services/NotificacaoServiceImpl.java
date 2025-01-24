package com.skymicrosystems.controleestoque.services;

import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.skymicrosystems.controleestoque.enums.EventoEnum;
import com.skymicrosystems.controleestoque.model.Empresa;
import com.skymicrosystems.controleestoque.model.Notificacao;
import com.skymicrosystems.controleestoque.repositories.NotificacaoRepository;
import com.skymicrosystems.controleestoque.utils.BuildManagementUtils;

@Service
public class NotificacaoServiceImpl {
	
	@Autowired
    private NotificacaoRepository notificacaoRepository;
	
    public Page<Notificacao> searchPaginate(Notificacao notificacao, Pageable pageable) {
    	
    	return notificacaoRepository.findAll(
				this.defaultSearchSpec(notificacao, 
						BuildManagementUtils.getEmpresaAuthenticated()), 
				pageable);
	}
    
    public Optional<Notificacao> findById(Long id) {
    	return notificacaoRepository.findById(id);
	}
    
    public Notificacao save(Notificacao notificacao) {
    	
    	Optional<Notificacao> findByKeyAndEvento = notificacaoRepository.findByChaveAndEvento(notificacao.getChave(), notificacao.getEvento());
    	
    	if (findByKeyAndEvento.isPresent()) {
    		return findByKeyAndEvento.get();
    	}
    	
        return notificacaoRepository.save(notificacao);
	}
    
    public Notificacao update(Notificacao notificacao) {
        return notificacaoRepository.saveAndFlush(notificacao);
	}
    
    public void delete(Long id) {
    	notificacaoRepository.findById(id).ifPresent(u -> notificacaoRepository.delete(u));
	}
    
    public void deleteByKeyAndEvento(String key, EventoEnum evento) {
    	Optional<Notificacao> findByKeyAndEvento = notificacaoRepository.findByChaveAndEvento(key, evento);
    	if (findByKeyAndEvento.isPresent()) {
    		notificacaoRepository.delete(findByKeyAndEvento.get());
    	}
	}
    
    private Specification<Notificacao> defaultSearchSpec(Notificacao notificacao, Empresa empresa) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			predicate = criteriaBuilder.equal(root.get("notificacoesEmpresa"), empresa);
			
			if (notificacao != null) {
			
				if (StringUtils.isNotBlank(notificacao.getCriadoPor())) {
					predicate = 
							criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("criadoPor")), 
											"%" + notificacao.getCriadoPor().toUpperCase() + "%"), 
									predicate);
				}

			}
			return predicate;
		};
	}
}
