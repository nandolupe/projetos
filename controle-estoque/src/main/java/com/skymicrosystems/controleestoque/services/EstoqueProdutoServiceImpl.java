package com.skymicrosystems.controleestoque.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.skymicrosystems.controleestoque.repositories.EstoqueProdutoRepository;
import com.skymicrosystems.controleestoque.repositories.ProdutoRepository;
import com.skymicrosystems.controleestoque.utils.BuildManagementUtils;

@Service
public class EstoqueProdutoServiceImpl {
	 
	@Autowired
    private EstoqueProdutoRepository estoqueProdutoRepository;

	@Autowired
    private ProdutoRepository produtoRepository;
    
	@Autowired
	private NotificacaoServiceImpl notificacaoServiceImpl;
	
    public Page<EstoqueProduto> searchPaginate(EstoqueProduto produto, Pageable pageable) {
    	
    	return estoqueProdutoRepository.findAll(
				this.defaultSearchSpec(produto, 
						BuildManagementUtils.getEmpresaAuthenticated()), 
				pageable);
	}
    
    public Optional<EstoqueProduto> findById(Long id) {
    	return estoqueProdutoRepository.findById(id);
	}
    
    public EstoqueProduto save(EstoqueProduto produto) {
        return estoqueProdutoRepository.save(produto);
	}
    
    public void registry(Long idProduto, Integer quantidade) {
    	
    	Produto produto = produtoRepository.getOne(idProduto);
    	produto.setQuantidadeRealEstoque(produto.getQuantidadeRealEstoque() - quantidade);
    	produtoRepository.save(produto);
    	
		estoqueProdutoRepository.save(new EstoqueProduto(
				quantidade * (-1), 
				LocalDateTime.now(), 
				produto, 
				BuildManagementUtils.getEmpresaAuthenticated()));
		
		
		if (produto.getQuantidadeRealEstoque() == BigDecimal.ZERO.intValue()) {
			notificacaoServiceImpl.save(new Notificacao(
					produto.getIdProduto().toString(),
					EventoEnum.PRODUTO_SEM_ESTOQUE,
					BuildManagementUtils.getStaticMessageFormatted(
							"manutencao.notificacao.produto.sem.estoque.message", 
							produto.getNomeProduto()), 
					"/produto/edit/" + produto.getIdProduto(), 
					EventoEnum.PRODUTO_SEM_ESTOQUE.getCriticidade(), 
					null));
		} else {
			if (((produto.getQuantidadeRealEstoque() * 100) / produto.getQuantidadeIdealEstoque()) < produto.getPercentualNotificacao()) {
				notificacaoServiceImpl.save(new Notificacao(
						produto.getIdProduto().toString(),
						EventoEnum.PRODUTO_ESTOQUE_BAIXO,
						BuildManagementUtils.getStaticMessageFormatted(
								"manutencao.notificacao.produto.estoque.baixo.message", 
								produto.getNomeProduto(), 
								produto.getPercentualNotificacao() + "%"), 
						"/produto/edit/" + produto.getIdProduto(), 
						EventoEnum.PRODUTO_ESTOQUE_BAIXO.getCriticidade(), 
						null));
				
			}
			
		}
		
	}
    
    public EstoqueProduto update(EstoqueProduto produto) {
        return estoqueProdutoRepository.saveAndFlush(produto);
	}
    
    public void delete(Long id) {
    	estoqueProdutoRepository.findById(id).ifPresent(u -> estoqueProdutoRepository.delete(u));
	}
    
    private Specification<EstoqueProduto> defaultSearchSpec(EstoqueProduto produto, Empresa empresa) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			predicate = criteriaBuilder.equal(root.get("empresa"), empresa);
			
			return predicate;
		};
	}
    
    /**
     * @return
     */
    public byte[] exportarMovimentacaoEstoque() {
    	List<EstoqueProduto> produtos = this.searchPaginate(null, PageRequest.of(0, 1000)).toList();
    	
    	List<String[]> dataLines = new ArrayList<>();
    	dataLines.add(new String[] { 
				"Cod Produto", 
				"Nome Produto",
				"Fabricante",
				"Quantidade", 
				"Data Execucao", 
				"Executado Por" 
				});
    	
    	produtos
	    	.stream()
	    	.map(
	    			t -> mapFieldsToCSV(t))
	    	.forEach(dataLines::add);

    	return new BuildManagementUtils().converListToCSV(dataLines).getBytes();
	}
    
    private String[] mapFieldsToCSV(EstoqueProduto t) {
		return new String[] { 
					t.getProduto().getCodigo(), 
					t.getProduto().getNomeProduto(),
					t.getProduto().getFabricante(),
					t.getQuantidade().toString(),
					t.getDataExecucao().toString(), 
					t.getCriadoPor()
				};
	}
}
