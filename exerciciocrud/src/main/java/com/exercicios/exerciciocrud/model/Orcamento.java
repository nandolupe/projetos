package com.exercicios.exerciciocrud.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ORCAMENTO")
public class Orcamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idOrcamento;

	private String nomeCliente;
	
	private String codigo;
	
	private LocalDate dataEmissao;
	
	private LocalDate dataExpiracao;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "OrcamentoProduto",
            joinColumns = {@JoinColumn(name = "idOrcamento")},
            inverseJoinColumns = {@JoinColumn(name = "idProduto")}
    )
	private List<Produto> produtos;
	
	/**
	 * CONTRUCTORS
	 * 
	 */
	
	public Orcamento() {}
	
	public Orcamento(Long idOrcamento) {
		super();
		this.idOrcamento = idOrcamento;
	}
	
	public Orcamento(String nomeCliente, String codigo, LocalDate dataEmissao, LocalDate dataExpiracao) {
		super();
		this.nomeCliente = nomeCliente;
		this.codigo = codigo;
		this.dataEmissao = dataEmissao;
		this.dataExpiracao = dataExpiracao;
	}

	/**
	 * GETTERS AND SETTERS
	 * 
	 */

	public Long getIdOrcamento() {
		return idOrcamento;
	}

	public void setIdOrcamento(Long idOrcamento) {
		this.idOrcamento = idOrcamento;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public LocalDate getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(LocalDate dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public LocalDate getDataExpiracao() {
		return dataExpiracao;
	}

	public void setDataExpiracao(LocalDate dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
}
