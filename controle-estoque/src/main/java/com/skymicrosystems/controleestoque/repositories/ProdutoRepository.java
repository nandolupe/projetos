package com.skymicrosystems.controleestoque.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.skymicrosystems.controleestoque.model.Empresa;
import com.skymicrosystems.controleestoque.model.Produto;


@Component
public interface ProdutoRepository extends JpaRepository<Produto, Long> , JpaSpecificationExecutor<Produto>, CrudRepository<Produto, Long> {
	public Optional<Produto> findByCodigoAndEmpresa(String codigo, Empresa empresa);
	
	@Modifying
	@Query("update Produto p set p.quantidadeRealEstoque = :quantidadeRealEstoque where p.idProduto = :idProduto")
	void updateQuantidadeRealEstoque(@Param(value = "idProduto") Long idProduto, @Param(value = "quantidadeRealEstoque") Integer quantidadeRealEstoque);
	
	@Query("SELECT p.idProduto FROM Produto p ORDER BY p.idProduto DESC")
	public List<Long> findLastIdProduto(Pageable pageable);
	
	@Query("select p from Produto p, Empresa e where p.empresa = e and p.dataVencimento <= :dataVencimento")
    public List<Produto> findAllWithDataVencimentoBefore(@Param("dataVencimento") LocalDate creationDateTime);
	
	@Query(value = "select p from Produto p where p.dataVencimento BETWEEN :startDate AND :endDate")
	public List<Produto> findAllProdutosEntreDatas(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
}
