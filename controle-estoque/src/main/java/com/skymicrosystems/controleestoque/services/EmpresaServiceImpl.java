package com.skymicrosystems.controleestoque.services;

import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.skymicrosystems.controleestoque.model.Empresa;
import com.skymicrosystems.controleestoque.model.Usuario;
import com.skymicrosystems.controleestoque.repositories.EmpresaRepository;

@Service
public class EmpresaServiceImpl {
	
	@Autowired
    private EmpresaRepository empresaRepository;
	
	@Autowired
	private UsuarioServiceImpl usuarioServiceImpl;
    
    public Page<Empresa> searchPaginate(Empresa empresa, Pageable pageable) {
    	
		return empresaRepository.findAll(
				this.defaultSearchSpec(empresa), 
				pageable);
	}
    
    private Specification<Empresa> defaultSearchSpec(Empresa empresa) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			if (empresa != null) {
				if (StringUtils.isNotBlank(empresa.getCnpjCpf())) {
					predicate = 
							criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("cnpjCpf")), 
											"%" + empresa.getCnpjCpf().toUpperCase() + "%"), 
									predicate);
				}
			}
			return predicate;
		};
	}
    
    public Optional<Empresa> findById(Long id) {
    	return empresaRepository.findById(id);
	}
    
    public Empresa save(Empresa empresa) {
    	
    	Usuario usuario = empresa.getUsuario();
    	empresa.setUsuario(null);

    	Empresa empresaSaved = empresaRepository.save(empresa);
    	
    	usuarioServiceImpl.saveEmpresaPrincipal(usuario, empresaSaved);
    	
        return empresaSaved;
	}
    
    public Empresa update(Empresa empresa) {
        return empresaRepository.saveAndFlush(empresa);
	}
    
    public void delete(Long id) {
    	empresaRepository.findById(id).ifPresent(u -> empresaRepository.delete(u));
	}
	
}
