package com.exercicios.exerciciocrud.services;

import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.exercicios.exerciciocrud.model.Orcamento;
import com.exercicios.exerciciocrud.repositories.OrcamentoRepository;

@Service
public class OrcamentoServiceImpl {
	
	@Autowired
    private OrcamentoRepository orcamentoRepository;
    
    public Page<Orcamento> searchPaginate(Orcamento orcamento, Pageable pageable) {
    	
		return orcamentoRepository.findAll(this.defaultSearchSpec(orcamento), pageable);
	}
    
    public Optional<Orcamento> findById(Long id) {
    	return orcamentoRepository.findById(id);
	}
     
    public Orcamento save(Orcamento orcamento) {
        return orcamentoRepository.save(orcamento);
	}
    
    public Orcamento update(Orcamento orcamento) {
        return orcamentoRepository.saveAndFlush(orcamento);
	}
    
    public void delete(Long id) {
    	orcamentoRepository.findById(id).ifPresent(u -> orcamentoRepository.delete(u));
	}
	
    private Specification<Orcamento> defaultSearchSpec(Orcamento orcamento) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			if (orcamento != null) {
			
				if (StringUtils.isNotBlank(orcamento.getCodigo())) {
					predicate = 
							criteriaBuilder
							.like(criteriaBuilder.upper(
									root.get("codigo")), 
									"%" + orcamento.getCodigo().toUpperCase() + "%");
				}
			}
			
			return predicate;
		};
	}
    
}
