package com.exercicios.exerciciocrud.services;

import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.exercicios.exerciciocrud.model.Usuario;
import com.exercicios.exerciciocrud.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImpl {
	
	@Autowired
    private UsuarioRepository usuarioRepository;
    
    public Page<Usuario> searchPaginate(Usuario usuario, Pageable pageable) {
    	
		return usuarioRepository.findAll(this.defaultSearchSpec(usuario), pageable);
	}
    
    public Optional<Usuario> findById(Long id) {
    	return usuarioRepository.findById(id);
	}
     
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
	}
    
    public Usuario update(Usuario usuario) {
        return usuarioRepository.saveAndFlush(usuario);
	}
    
    public void delete(Long id) {
    	usuarioRepository.findById(id).ifPresent(u -> usuarioRepository.delete(u));
	}
	
    private Specification<Usuario> defaultSearchSpec(Usuario produto) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			if (produto != null) {
			
				if (StringUtils.isNotBlank(produto.getNome())) {
					predicate = 
							criteriaBuilder
							.like(criteriaBuilder.upper(
									root.get("nome")), 
									"%" + produto.getNome().toUpperCase() + "%");
				}
			}
			return predicate;
		};
	}
    
}
