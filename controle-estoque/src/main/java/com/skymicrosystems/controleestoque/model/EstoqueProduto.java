package com.skymicrosystems.controleestoque.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.skymicrosystems.controleestoque.utils.BuildManagementUtils;

@Entity
@Table(name = "ESTOQUE_PRODUTO")
public class EstoqueProduto extends AuditModel {
	
	/*
	 * FIELDS
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEstoqueProduto;

	private Integer quantidade;
	
	private LocalDateTime dataExecucao; 
	
	@ManyToOne
    @JoinColumn(name="idProduto", nullable=true)
    private Produto produto;

	@OneToOne(cascade = CascadeType.DETACH, optional = true)
    @JoinColumn(name="idEmpresa", nullable = false)
    private Empresa empresa;
	
	/*
	 * CONSTRUCTORS
	 */
	public EstoqueProduto() {}
	
	public EstoqueProduto(Long idEstoqueProduto) {
		super();
		this.idEstoqueProduto = idEstoqueProduto;
	}

	public EstoqueProduto(Integer quantidade, LocalDateTime dataExecucao, Produto produto, Empresa empresa) {
		super();
		this.quantidade = quantidade;
		this.dataExecucao = dataExecucao;
		this.produto = produto;
		this.empresa = empresa;
	}

	/*
	 * GETTERS AND SETTERS
	 */
	public Long getIdEstoqueProduto() {
		return idEstoqueProduto;
	}


	public void setIdEstoqueProduto(Long idEstoqueProduto) {
		this.idEstoqueProduto = idEstoqueProduto;
	}


	public Integer getQuantidade() {
		return quantidade;
	}


	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}


	public LocalDateTime getDataExecucao() {
		return dataExecucao;
	}


	public void setDataExecucao(LocalDateTime dataExecucao) {
		this.dataExecucao = dataExecucao;
	}


	public Produto getProduto() {
		return produto;
	}


	public void setProduto(Produto produto) {
		this.produto = produto;
	}


	public Empresa getEmpresa() {
		return empresa;
	}


	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	
	/*
	 * PROCESSORS
	 */
	
	@PrePersist
	@Override
	protected void prePersist() {
		super.prePersist();
		if (getEmpresa() == null)
			this.setEmpresa(BuildManagementUtils.getEmpresaAuthenticated());
	}

	@PreUpdate
	@Override
	protected void preUpdate() {
		super.preUpdate();
		
		if (getEmpresa() == null) 
			this.setEmpresa(BuildManagementUtils.getEmpresaAuthenticated());
	}
}
