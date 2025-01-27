package com.exercicios.exerciciocrud.repositories;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.exercicios.exerciciocrud.model.Usuario;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UsuarioRepositoryTest {
	
	@Autowired
    UsuarioRepository usuarioRepository;
    
	private Usuario usuarioTest;
	
	@BeforeEach
	public void setUp() {
	    // Initialize test data before each test method
		usuarioTest = new Usuario();
		usuarioTest.setNome("Test ABC");
		usuarioTest.setSobrenome("Test ");
		usuarioTest.setEstado("SP");
		usuarioTest.setEmail("teste@test.com");
		usuarioTest.setCidade("Suzano");
		
		usuarioTest = usuarioRepository.save(usuarioTest);
	}

	@AfterEach
	public void tearDown() {
		
		usuarioRepository.findById(
				usuarioTest
				.getId())
		.ifPresent(t -> usuarioRepository.delete(usuarioTest));
	}
	
    @Test
    void givenNewUsuario_whenSave_thenSuccess() {
        Usuario usuario = new Usuario("Teste", "Teste ABC", 10, "teste@test.com", "Suzano", "SP");
        Usuario usuarioInsert = usuarioRepository.save(usuario);
        
        assertNotNull(usuarioInsert.getId());
    }
    
    
    @Test
    void givenUsuario_whenGet_thenSuccess() {

        Optional<Usuario> usuarioInsert = usuarioRepository.findById(usuarioTest.getId());
        
        assertNotNull(usuarioInsert.get());
        assertEquals(usuarioInsert.get().getNome(), usuarioTest.getNome());
    }
    
    @Test
    void givenUsuario_whenDelete_thenSuccess() {

        Optional<Usuario> usuarioInsert = usuarioRepository.findById(usuarioTest.getId());
        usuarioRepository.delete(usuarioInsert.get());
        
        Optional<Usuario> usuarioPostDelete = usuarioRepository.findById(usuarioTest.getId());
        
        assertTrue(usuarioPostDelete.isEmpty());
    }
    
    @Test
    void givenUsuario_whenFindAll_thenSuccess() {

        List<Usuario> findAll = usuarioRepository.findAll();
        
        assertNotNull(findAll);
        assertTrue(findAll.size() > 0);
    }
}
