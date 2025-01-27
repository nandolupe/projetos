package com.exercicios.exerciciocrud.services;

import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.exercicios.exerciciocrud.model.Produto;
import com.exercicios.exerciciocrud.repositories.ProdutoRepository;

@Service
public class ProdutoServiceImpl {
	
	@Autowired
    private ProdutoRepository produtoRepository;
    
    public Page<Produto> searchPaginate(Produto produto, Pageable pageable) {
    	
		return produtoRepository.findAll(this.defaultSearchSpec(produto), pageable);
	}
    
    public Optional<Produto> findById(Long id) {
    	return produtoRepository.findById(id);
	}
     
    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
	}
    
    public Produto update(Produto produto) {
        return produtoRepository.saveAndFlush(produto);
	}
    
    public void delete(Long id) {
    	produtoRepository.findById(id).ifPresent(u -> produtoRepository.delete(u));
	}
	
    private Specification<Produto> defaultSearchSpec(Produto produto) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			if (produto != null) {
			
				if (StringUtils.isNotBlank(produto.getNomeProduto())) {
					predicate = 
							criteriaBuilder
							.like(criteriaBuilder.upper(
									root.get("nomeProduto")), 
									"%" + produto.getNomeProduto().toUpperCase() + "%");
				}
			}
			return predicate;
		};
	}
    
}
