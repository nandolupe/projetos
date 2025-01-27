package com.exercicios.exerciciocrud.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.exercicios.exerciciocrud.model.Usuario;

@SpringBootTest
public class UsuarioServiceTest {
	
	@Autowired
    UsuarioServiceImpl usuarioServiceImpl;
    
	private Usuario usuarioTest;
	
	@Test
	void contextLoads() {
		assertThat(usuarioServiceImpl).isNotNull();
	}
	
	@BeforeEach
	public void setUp() {
	    // Initialize test data before each test method
		usuarioTest = new Usuario();
		usuarioTest.setNome("Test ABC");
		usuarioTest.setSobrenome("Test ");
		usuarioTest.setEstado("SP");
		usuarioTest.setEmail("teste@test.com");
		usuarioTest.setCidade("Suzano");
		
		usuarioTest = usuarioServiceImpl.save(usuarioTest);
	}

	@AfterEach
	public void tearDown() {
		
		usuarioServiceImpl.findById(
				usuarioTest
				.getId())
		.ifPresent(t -> usuarioServiceImpl.delete(usuarioTest.getId()));
	}
	
	@Test
    void givenNewUsuario_whenSave_thenSuccess() {
        Usuario usuario = new Usuario("Teste", "Teste ABC", 10, "teste@test.com", "Suzano", "SP");
        Usuario usuarioInsert = usuarioServiceImpl.save(usuario);
        
        assertNotNull(usuarioInsert.getId());
    }
    
    
    @Test
    void givenUsuario_whenGet_thenSuccess() {

        Optional<Usuario> usuarioInsert = usuarioServiceImpl.findById(usuarioTest.getId());
        
        assertNotNull(usuarioInsert.get());
        assertEquals(usuarioInsert.get().getNome(), usuarioTest.getNome());
    }
    
    @Test
    void givenUsuario_whenDelete_thenSuccess() {

        Optional<Usuario> usuarioInsert = usuarioServiceImpl.findById(usuarioTest.getId());
        usuarioServiceImpl.delete(usuarioInsert.get().getId());
        
        Optional<Usuario> usuarioPostDelete = usuarioServiceImpl.findById(usuarioTest.getId());
        
        assertTrue(usuarioPostDelete.isEmpty());
    }
    
    @Test
    void givenUsuario_whenSearch_thenSuccess() {
    	
        Page<Usuario> searchPaginate = usuarioServiceImpl.searchPaginate(usuarioTest, PageRequest.of(0, 5));
        
        assertNotNull(searchPaginate.getContent());
        assertTrue(searchPaginate.getContent().size() > 0);
    }
	
}
