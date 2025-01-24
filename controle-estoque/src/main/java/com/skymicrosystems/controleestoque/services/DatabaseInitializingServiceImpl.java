package com.skymicrosystems.controleestoque.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.skymicrosystems.controleestoque.enums.MedidaEnum;
import com.skymicrosystems.controleestoque.enums.StatusEnum;
import com.skymicrosystems.controleestoque.enums.TipoAcessoEnum;
import com.skymicrosystems.controleestoque.model.Contato;
import com.skymicrosystems.controleestoque.model.Empresa;
import com.skymicrosystems.controleestoque.model.Endereco;
import com.skymicrosystems.controleestoque.model.Papel;
import com.skymicrosystems.controleestoque.model.Produto;
import com.skymicrosystems.controleestoque.model.Usuario;
import com.skymicrosystems.controleestoque.repositories.EmpresaRepository;
import com.skymicrosystems.controleestoque.repositories.PapelRepository;
import com.skymicrosystems.controleestoque.repositories.ProdutoRepository;
import com.skymicrosystems.controleestoque.repositories.UsuarioRepository;

@Service
public class DatabaseInitializingServiceImpl {
	
	private final static Logger logger = LoggerFactory.getLogger(DatabaseInitializingServiceImpl.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private PapelRepository papelRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	
	public void initialEssentialDataBase() {
		
		// verifica se já existe registro na base
		if(papelRepository.count() == 0) {
		
			logger.info("######################################################################");
			logger.info("################## INITIALINZING ESSENTIAL DATABASE ##################");
			logger.info("######################################################################");
			
			logger.info("Creating initial datas for Papel...");
			
			Papel papel = new Papel("ROLE_INTERNO_ADMIN", "ADMINISTRATIVO", "PERMISSAO COM TODOS OS ACESSOS LIBERADOS PARA TODO SISTEMA", TipoAcessoEnum.INTERNO, StatusEnum.ATIVO);
			papel.setCriadoPor(env.getProperty("default.system.username"));
			papel.setDtCriacao(LocalDateTime.now());
			Papel INTERNO_ADMIN_SAVED = papelRepository.save(papel);
			
			papel = new Papel("ROLE_EMPRESA_ADMIN", "ADMINISTRATIVO EMPRESA", "PERMISSAO COM TODOS OS ACESSOS LIBERADOS PARA EMPRESA" , TipoAcessoEnum.EMPRESA, StatusEnum.ATIVO);
			papel.setCriadoPor(env.getProperty("default.system.username"));
			papel.setDtCriacao(LocalDateTime.now());
			papelRepository.save(papel);
			
			
			papel = new Papel("ROLE_EMPRESA_ESTOQUISTA", "ESTOQUISTA EMPRESA", "PERMISSAO APENAS PARA MANIPULAR ESTOQUE" , TipoAcessoEnum.EMPRESA, StatusEnum.ATIVO);
			papel.setCriadoPor(env.getProperty("default.system.username"));
			papel.setDtCriacao(LocalDateTime.now());
			papelRepository.save(papel);
			
			papel = new Papel("ROLE_EMPRESA_OPERACIONAL", "OPERACIONAL EMPRESA", "PERMISSAO APENAS PARA RETIRADA DE ESTOQUE" , TipoAcessoEnum.EMPRESA, StatusEnum.ATIVO);
			papel.setCriadoPor(env.getProperty("default.system.username"));
			papel.setDtCriacao(LocalDateTime.now());
			papelRepository.save(papel);
			
			logger.info("Creating initial datas for Usuario...");
			
			Usuario usuario = new Usuario(
					TipoAcessoEnum.INTERNO, 
					null, 
					null, 
					null, 
					env.getProperty("default.system.user.administrator.name"), 
					env.getProperty("default.system.user.administrator.login"), 
					env.getProperty("default.system.user.administrator.email"), 
					env.getProperty("default.system.user.administrator.password"), 
					Boolean.parseBoolean(env.getProperty("default.system.user.administrator.locked")), 
					Boolean.parseBoolean(env.getProperty("default.system.user.administrator.enabled")), 
					Collections.singletonList(INTERNO_ADMIN_SAVED), 
					null);
			
			usuario.setCriadoPor(env.getProperty("default.system.username"));
			usuario.setDtCriacao(LocalDateTime.now());
			usuarioRepository.save(usuario);
			
			usuario = new Usuario(
					TipoAcessoEnum.INTERNO, 
					null, 
					null, 
					null, 
					"User 1", 
					"user1", 
					"administrador@test.com", 
					"1234", 
					Boolean.FALSE, 
					Boolean.TRUE, 
					Collections.singletonList(INTERNO_ADMIN_SAVED), 
					null);
			
			usuario.setCriadoPor(env.getProperty("default.system.username"));
			usuario.setDtCriacao(LocalDateTime.now());
			usuarioRepository.save(usuario);
			
			logger.info("######################################################################");
			logger.info("############# FINISH INITIALINZING ESSENTIAL DATABASE ################");
			logger.info("######################################################################");
			
		}
		
	}
	
	public void initialMockDataBase() {
		
		if(empresaRepository.count() == 0) {
		
			logger.info("######################################################################");
			logger.info("################## INITIALINZING MOCK DATABASE #######################");
			logger.info("######################################################################");
			
			logger.info("Creating mock datas for Empresa e Acessos...");
			
			List<Papel> papeisEmpresa = papelRepository.findByNome("ROLE_EMPRESA_ADMIN");
			
			this.criarEmpresa("EMPRESA TESTE 1 LTDA", "21.111.111/0001-80", "empresa1", "1234", papeisEmpresa);
			
			this.criarEmpresa("EMPRESA TESTE 2 LTDA", "20.000.000/0001-90", "empresa2", "1234", papeisEmpresa);
			
			logger.info("######################################################################");
			logger.info("############### FINISH INITIALINZING MOCK DATABASE ###################");
			logger.info("######################################################################");
		}
	}
	
	private void criarEmpresa(String nome, String cnpj, String login, String senha,  List<Papel> papel) {
		
		logger.info("Creating mock datas for Empresa e Acessos...");
		
		Endereco endereco = new Endereco("Av. Paulista", "125", "Bela Vista", "São Paulo", "SP", "00000-555", null);
		endereco.setDtCriacao(LocalDateTime.now());
		endereco.setCriadoPor(env.getProperty("default.system.username"));
		
		Contato contato = new Contato("Teste", "(11) 87980-7879", "(11) 8798-7879", "teste@teste.com");
		contato.setDtCriacao(LocalDateTime.now());
		contato.setCriadoPor(env.getProperty("default.system.username"));
		
		Empresa empresaSave = this.criarEmpresa(nome, cnpj, endereco, contato);
		
		this.criarUsuario(nome, login, senha, papel, empresaSave);
		
		
		logger.info("Creating mock datas for Produto...");
		
		this.criarProduto("P001", "Macarrao parafuso", "Adria", MedidaEnum.UNIT, 50, 60, 20, "ATIVO", empresaSave);
		
		this.criarProduto("P002", "Oleo Soja", "Soya", MedidaEnum.UNIT, 5, 40, 20, "ATIVO", empresaSave);
		
		
	}
	
	private Produto criarProduto(String codigoProduto, String nomeProduto, String fabricante, MedidaEnum medida, Integer qtReal, Integer qtIdeal, 
			Integer percentualNotificacao, String status, Empresa empresa1Save) {
		Produto produto = new Produto(
				codigoProduto,
				nomeProduto, 
				fabricante, 
				medida,
				null, 
				null, 
				null, 
				qtReal, 
				qtIdeal, 
				percentualNotificacao, 
				status, 
				empresa1Save);
		
		produto.setCriadoPor(env.getProperty("default.system.username"));
		produto.setDtCriacao(LocalDateTime.now());
		return produtoRepository.save(produto);
	}
	
	private Empresa criarEmpresa(String nome, String cnpj, Endereco endereco, Contato contato) {
		Empresa empresa1 = new Empresa(
				cnpj, 
				nome, 
				nome, 
				LocalDate.now(), 
				LocalDate.now(), 
				"ATIVO", 
				endereco, 
				contato, 
				null);
		
		empresa1.setCriadoPor(env.getProperty("default.system.username"));
		empresa1.setDtCriacao(LocalDateTime.now());
		return empresaRepository.save(empresa1);
	}
	
	private Usuario criarUsuario(String nome, String login, String senha, List<Papel> papel, Empresa empresa1Save) {
		Usuario usuario = new Usuario(
				TipoAcessoEnum.EMPRESA, 
				null, 
				null, 
				null, 
				nome, 
				login, 
				login + "@test.com", 
				senha, 
				Boolean.FALSE, 
				Boolean.TRUE, 
				papel, 
				empresa1Save);
		
		usuario.setCriadoPor(env.getProperty("default.system.username"));
		usuario.setDtCriacao(LocalDateTime.now());
		return usuarioRepository.save(usuario);
	}
}
