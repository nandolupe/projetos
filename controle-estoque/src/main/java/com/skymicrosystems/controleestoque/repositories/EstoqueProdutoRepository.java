package com.skymicrosystems.controleestoque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import com.skymicrosystems.controleestoque.model.EstoqueProduto;


@Component
public interface EstoqueProdutoRepository extends JpaRepository<EstoqueProduto, Long> , JpaSpecificationExecutor<EstoqueProduto> {
}
