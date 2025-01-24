package com.skymicrosystems.controleestoque.services;

import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.skymicrosystems.controleestoque.enums.TipoAcessoEnum;
import com.skymicrosystems.controleestoque.model.Empresa;
import com.skymicrosystems.controleestoque.model.Usuario;
import com.skymicrosystems.controleestoque.repositories.UsuarioRepository;
import com.skymicrosystems.controleestoque.utils.BuildManagementUtils;

@Service
public class UsuarioServiceImpl implements UserDetailsService {
	
	@Autowired
    private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PapelServiceImpl papelServiceImpl;
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
        return usuarioRepository
                .findByUsername(username)
                .orElseThrow(() -> 
                new UsernameNotFoundException(
                		String.format(
                				BuildManagementUtils.getStaticMessage("manutencao.usuario.label.not-found"), 
                				username)));
    }
    
    public Page<Usuario> searchPaginate(Usuario usuario, Pageable pageable) {
    	
		return usuarioRepository.findAll(
				this.defaultSearchSpec(usuario, 
						BuildManagementUtils.getUserAuthenticated()), 
				pageable);
	}
    
    private Specification<Usuario> defaultSearchSpec(Usuario usuarioSearch, Usuario usuarioAutenticated) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			predicate =  criteriaBuilder.notEqual(root.get("id"), usuarioAutenticated.getId());
			
			if (usuarioAutenticated.getEmpresa() != null) {
				predicate = criteriaBuilder.and(criteriaBuilder.equal(root.get("empresa"), usuarioAutenticated.getEmpresa()), predicate);
			}
			
			if (usuarioSearch != null) {
			
				if (StringUtils.isNotBlank(usuarioSearch.getUsername())) {
					predicate = 
							criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("username")), 
											"%" + usuarioSearch.getUsername().toUpperCase() + "%"), 
									predicate);
				}
				
				if (StringUtils.isNotBlank(usuarioSearch.getName())) {
					predicate = criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("name")), 
											"%" + usuarioSearch.getName().toUpperCase() + "%"), 
									predicate);
				}
			}
			return predicate;
		};
	}
    
    public Optional<Usuario> findById(Long id) {
    	return usuarioRepository.findById(id);
	}
    
    public Usuario saveEmpresaPrincipal(Usuario usuario, Empresa empresa) {
    	
    	usuario.setEmpresa(empresa);
    	usuario.setPapeis(papelServiceImpl.findByNome("ROLE_EMPRESA_ADMIN"));
    	usuario.setName(empresa.getNomeFantasia());
    	usuario.setTipoAcesso(TipoAcessoEnum.EMPRESA);
    	usuario.setLocked(Boolean.FALSE);
    	usuario.setEnabled(Boolean.TRUE);
    	usuario.setEmail(empresa.getContato().getEmail());
    	
        return usuarioRepository.save(usuario);
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
    
    public String proximoCodigo() {
        return BuildManagementUtils.defaultCodeFormat(
        		"X", 
        		usuarioRepository.findLastId(PageRequest.of(0, 1)).get(0), 
        		3);
	}
    
    
    /**
     * @param username
     * @param currentPassword
     * @param newPassword
     * @throws Exception
     */
    public void updatePassword(String currentPassword, String newPassword) throws Exception {
        Usuario user = usuarioRepository.findByUsername(BuildManagementUtils.getUserAuthenticated().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(newPassword);
        usuarioRepository.save(user);
    }
}
