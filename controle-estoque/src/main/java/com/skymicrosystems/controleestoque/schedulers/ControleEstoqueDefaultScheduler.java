package com.skymicrosystems.controleestoque.schedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.skymicrosystems.controleestoque.services.ProdutoServiceImpl;

@Component
public class ControleEstoqueDefaultScheduler {
	
	private final static Logger logger = LoggerFactory.getLogger(ControleEstoqueDefaultScheduler.class);
	
	@Autowired
	private ProdutoServiceImpl produtoServiceImpl;
	
	@Scheduled(fixedDelay = 300000)
	public void notificarProdutosVencidos() {
		
		logger.debug("Iniciando processo de notificacao de produtos vencidos");
		
		int totalProdutosVencidos = produtoServiceImpl.notificarProdutosVencidos();
		
		logger.debug("Produtos vencidos e notificados: " + totalProdutosVencidos);
		
		logger.debug("Finalizando processo de notificacao de produtos vencidos");

	}
	
	@Scheduled(fixedDelay = 300000)
	public void notificarProdutosProximoVencimento() {
		
		logger.debug("Iniciando processo de notificacao de produtos proximo ao vencimento");
		
		int totalProdutosVencidos = produtoServiceImpl.notificarProdutosProximoVencimento();
		
		logger.debug("Produtos vencidos e notificados: " + totalProdutosVencidos);
		
		logger.debug("Finalizando processo de notificacao de produtos proximo ao vencimento");

	}
}
