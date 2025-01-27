package com.exercicios.exerciciocrud.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUTO")
public class Produto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProduto;
	
	private String codigo;
	
	private String nomeProduto;
	
	@Column(scale = 2)
	private BigDecimal valorCusto;
	
	@Column(scale = 2)
	private BigDecimal valorVenda;
	
	@ManyToMany(mappedBy = "produtos", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<Orcamento> orcamentos;
	
	/**
	 * CONTRUCTORS
	 * 
	 */
	
	public Produto() {}
	
	public Produto(Long idProduto) {
		super();
		this.idProduto = idProduto;
	}

	public Produto(String codigo, String nomeProduto, BigDecimal valorCusto, BigDecimal valorVenda) {
		super();
		this.codigo = codigo;
		this.nomeProduto = nomeProduto;
		this.valorCusto = valorCusto;
		this.valorVenda = valorVenda;
	}

	/**
	 * GETTERS AND SETTERS
	 * 
	 */
	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public BigDecimal getValorCusto() {
		return valorCusto;
	}

	public void setValorCusto(BigDecimal valorCusto) {
		this.valorCusto = valorCusto;
	}

	public BigDecimal getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}

	public List<Orcamento> getOrcamentos() {
		return orcamentos;
	}

	public void setOrcamentos(List<Orcamento> orcamentos) {
		this.orcamentos = orcamentos;
	}
}
