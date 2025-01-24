package com.skymicrosystems.controleestoque.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.skymicrosystems.controleestoque.enums.EventoEnum;
import com.skymicrosystems.controleestoque.model.Empresa;
import com.skymicrosystems.controleestoque.model.EstoqueProduto;
import com.skymicrosystems.controleestoque.model.Notificacao;
import com.skymicrosystems.controleestoque.model.Produto;
import com.skymicrosystems.controleestoque.repositories.ProdutoRepository;
import com.skymicrosystems.controleestoque.utils.BuildManagementUtils;

@Service
public class ProdutoServiceImpl {
	
	private final static Logger logger = LoggerFactory.getLogger(ProdutoServiceImpl.class);
	
	@Autowired
    private ProdutoRepository produtoRepository;
    
	@Autowired
	private EstoqueProdutoServiceImpl estoqueProdutoServiceImpl;
	
	@Autowired
	private NotificacaoServiceImpl notificacaoServiceImpl;
	
	@Autowired
	private Environment env; 
	
	
    public Page<Produto> searchPaginate(Produto produto, Pageable pageable) {
    	
    	return produtoRepository.findAll(
				this.defaultSearchSpec(produto, 
						BuildManagementUtils.getEmpresaAuthenticated()), 
				pageable);
	}
    
    public Optional<Produto> findById(Long id) {
    	return produtoRepository.findById(id);
	}
    
    public Produto save(Produto produto) {
    	
    	if (produto.getIdProduto() != null ) { 
    		Produto produtoBase = produtoRepository.getOne(produto.getIdProduto());
    		
    		if (produtoBase.getQuantidadeRealEstoque() != produto.getQuantidadeRealEstoque()) {
    			// gera registro de retirada ou reposicao
    			
    			Integer novaQuantidadeReal = produto.getQuantidadeRealEstoque() - produtoBase.getQuantidadeRealEstoque();
    			
    			estoqueProdutoServiceImpl.save(new EstoqueProduto(
    					novaQuantidadeReal, 
    					LocalDateTime.now(), 
    					produtoBase, 
    					null));
    			
    			// retira notificacoes em caso de reposicao
    			if (produto.getQuantidadeRealEstoque() > 0) {
    				notificacaoServiceImpl.deleteByKeyAndEvento(
    						produtoBase.getIdProduto().toString(), 
    						EventoEnum.PRODUTO_SEM_ESTOQUE);
    			}
    			
    			if (((produto.getQuantidadeRealEstoque() * 100) / produto.getQuantidadeIdealEstoque()) >= produto.getPercentualNotificacao()) {
    				notificacaoServiceImpl.deleteByKeyAndEvento(
    						produtoBase.getIdProduto().toString(), 
    						EventoEnum.PRODUTO_ESTOQUE_BAIXO);
    			}
    		}
    		
    		if (produtoBase.getDataVencimento() != null) {
	    		if (!produto.getDataVencimento().isEqual(produtoBase.getDataVencimento())) {
		    		// retira notificacoes em caso de data vencimento ajustada
					if(produto.getSituacaoDataVencimento() > 0) {
						notificacaoServiceImpl.deleteByKeyAndEvento(
								produtoBase.getIdProduto().toString(), 
								EventoEnum.PRODUTO_DATA_VENCIDA);
					}
					
					// retira notificacoes em caso de data vencimento ajustada
					if(produto.getSituacaoDataVencimento() > 20) {
						notificacaoServiceImpl.deleteByKeyAndEvento(
								produtoBase.getIdProduto().toString(), 
								EventoEnum.PRODUTO_DATA_VENCIMENTO_PROXIMA);
					}
	    		}
    		}
    	}
    	
        return produtoRepository.save(produto);
	}
    
    public Produto update(Produto produto) {
        return produtoRepository.saveAndFlush(produto);
	}
    
    public void delete(Long id) {
    	produtoRepository.findById(id).ifPresent(u -> produtoRepository.delete(u));
	}
    
    /**
     * @param idProduto
     * @param quantidadeRetirada
     */
    public void updateQuantidadeReal(Long idProduto, Integer quantidadeRetirada) {
    	Produto produto = produtoRepository.getOne(idProduto);
    	produto.setQuantidadeRealEstoque(produto.getQuantidadeRealEstoque() - quantidadeRetirada);
    	produtoRepository.save(produto);
	}
    
    /**
     * @param produto
     * @param empresa
     * @return
     */
    private Specification<Produto> defaultSearchSpec(Produto produto, Empresa empresa) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			predicate = criteriaBuilder.equal(root.get("empresa"), empresa);
			
			if (produto != null) {
			
				if (StringUtils.isNotBlank(produto.getCodigo())) {
					predicate = 
							criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("codigo")), 
											"%" + produto.getCodigo().toUpperCase() + "%"), 
									predicate);
				}
				
				if (StringUtils.isNotBlank(produto.getNomeProduto())) {
					predicate = criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("nomeProduto")), 
											"%" + produto.getNomeProduto().toUpperCase() + "%"), 
									predicate);
				}
			}
			return predicate;
		};
	}
    
    /**
     * @return
     * 
     * Obtem o proximo codigo com a sigla 
     * escolhida para produto = P000
     * 
     * 
     */
    public String proximoCodigo() {
    	
    	List<Long> findLastIdProduto = produtoRepository.findLastIdProduto(PageRequest.of(0, 1));
    	
        return BuildManagementUtils.defaultCodeFormat(
        		"P", 
        		findLastIdProduto.isEmpty() ? 1 : findLastIdProduto.get(0)+1, 
        		3);
	}
    
    /**
     * Busca produtos com a data vencida e 
     * registra notificacao para empresas
     */
    public int notificarProdutosVencidos() {
		List<Produto> produtosVencidos = produtoRepository.findAllWithDataVencimentoBefore(LocalDate.now());
		
		for (Produto produtoVencido : produtosVencidos) {
			
			Optional<Produto> produtoBase = findById(produtoVencido.getIdProduto());
			
			Notificacao notificacao = new Notificacao(
					produtoVencido.getIdProduto().toString(),
					EventoEnum.PRODUTO_DATA_VENCIDA,
					BuildManagementUtils.getStaticMessageFormatted(
							"manutencao.notificacao.produto.data.vencida.message", 
							produtoVencido.getNomeProduto()), 
					"/produto/edit/" + produtoVencido.getIdProduto(), 
					EventoEnum.PRODUTO_DATA_VENCIDA.getCriticidade(), 
					produtoBase.get().getEmpresa());
			
			notificacao.setCriadoPor(env.getProperty("default.system.username"));
			notificacao.setDtCriacao(LocalDateTime.now());
			
			notificacaoServiceImpl.save(notificacao);
		}
		
		return produtosVencidos.size();
	}
    
    /**
     * @return
     * 
     * Busca todos os produtos com 20 dias ou enos para vencer e notifica as empresas
     */
    public int notificarProdutosProximoVencimento() {
		List<Produto> produtosProximoVencimento = produtoRepository.findAllProdutosEntreDatas(LocalDate.now(), LocalDate.now().plusDays(20));
		
		for (Produto produtoProximoVencido : produtosProximoVencimento) {
			
			Optional<Produto> produtoBase = findById(produtoProximoVencido.getIdProduto());
			
			Notificacao notificacao = new Notificacao(
					produtoProximoVencido.getIdProduto().toString(),
					EventoEnum.PRODUTO_DATA_VENCIMENTO_PROXIMA,
					BuildManagementUtils.getStaticMessageFormatted(
							"manutencao.notificacao.produto.data.proximo.vencimento.message", 
							produtoProximoVencido.getNomeProduto()), 
					"/produto/edit/" + produtoProximoVencido.getIdProduto(), 
					EventoEnum.PRODUTO_DATA_VENCIMENTO_PROXIMA.getCriticidade(), 
					produtoBase.get().getEmpresa());
			
			notificacao.setCriadoPor(env.getProperty("default.system.username"));
			notificacao.setDtCriacao(LocalDateTime.now());
			
			notificacaoServiceImpl.save(notificacao);
		}
		
		return produtosProximoVencimento.size();
	}
    
    public byte[] exportarProdutos() {

    	List<Produto> produtos = this.searchPaginate(null, PageRequest.of(0, 1000)).toList();
    	
    	List<String[]> dataLines = new ArrayList<>();
    	dataLines.add(new String[] { 
					"Cod Produto", 
					"Nome Produto",
					"Fabricante",
					"Medida", 
					"Qtd Real Estoque", 
					"Qtd Ideal Estoque", 
					"Percentual Notificacao",
					"Situacao Percentual",
					"Data Vencimento",
					"Status",
					"Criado Por",
					"Data Criacao",
					"Alterador Por",
					"Data Alteracao"
				});
    	
    	produtos
	    	.stream()
	    	.map(
	    			t -> mapFieldsToCSV(t))
	    	.forEach(dataLines::add);

    	return new BuildManagementUtils().converListToCSV(dataLines).getBytes();
	}

	private String[] mapFieldsToCSV(Produto t) {
		return new String[] { 
					t.getCodigo(), 
					t.getNomeProduto(),
					t.getFabricante(),
					t.getMedida().getName(), 
					t.getQuantidadeRealEstoque().toString(), 
					t.getQuantidadeIdealEstoque().toString(), 
					t.getPercentualNotificacao().toString(),
					t.getSituacaoPercentual().toString(),
					t.getDataVencimento() != null ? t.getDataVencimento().toString() : "",
					t.getStatus(),
					t.getCriadoPor(),
					t.getDtCriacao().toString(),
					StringUtils.trimToEmpty(t.getAlteradoPor()),
					t.getDtAlteracao() != null ? t.getDtAlteracao().toString() : ""
				};
	}
	
	
}
