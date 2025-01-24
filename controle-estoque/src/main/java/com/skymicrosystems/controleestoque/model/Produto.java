package com.skymicrosystems.controleestoque.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.skymicrosystems.controleestoque.enums.MedidaEnum;
import com.skymicrosystems.controleestoque.utils.BuildManagementUtils;

@Entity
@Table(name = "PRODUTO")
@DynamicUpdate
public class Produto extends AuditModel {
	
	/*
	 * FIELDS
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProduto;

	private String codigo;
	
	private String nomeProduto;
	
	private String fabricante;
	
	private MedidaEnum medida;
	
	@Column(scale = 2)
	private BigDecimal valorCusto;
	
	@Column(scale = 2)
	private BigDecimal valorVenda;
	
	@Column(columnDefinition = "DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimento;
	
	private Integer quantidadeRealEstoque;
	
	private Integer quantidadeIdealEstoque;
	
	private Integer percentualNotificacao;
	
	private String status;
	
	@Transient
	private Integer situacaoPercentual;
	
	@Transient
	private long situacaoDataVencimento;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idEmpresa", nullable = false)
    private Empresa empresa;
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy="produto")
    private List<EstoqueProduto> estoqueProdutos;
	
	/*
	 * CONSTRUCTORS
	 */
	public Produto() {}
	
	public Produto(Long idProduto) {
		super();
		this.idProduto = idProduto;
	}

	public Produto(String codigo, String nomeProduto, String fabricante, MedidaEnum medida, BigDecimal valorCusto,
			BigDecimal valorVenda, LocalDate dataVencimento, Integer quantidadeRealEstoque,
			Integer quantidadeIdealEstoque, Integer percentualNotificacao, String status, Empresa empresa) {
		super();
		this.codigo = codigo;
		this.nomeProduto = nomeProduto;
		this.fabricante = fabricante;
		this.medida = medida;
		this.valorCusto = valorCusto;
		this.valorVenda = valorVenda;
		this.dataVencimento = dataVencimento;
		this.quantidadeRealEstoque = quantidadeRealEstoque;
		this.quantidadeIdealEstoque = quantidadeIdealEstoque;
		this.percentualNotificacao = percentualNotificacao;
		this.status = status;
		this.empresa = empresa;
	}

	/*
	 * GETTERS AND SETTERS
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public Integer getQuantidadeRealEstoque() {
		return quantidadeRealEstoque;
	}

	public void setQuantidadeRealEstoque(Integer quantidadeRealEstoque) {
		this.quantidadeRealEstoque = quantidadeRealEstoque;
	}

	public Integer getQuantidadeIdealEstoque() {
		return quantidadeIdealEstoque;
	}

	public void setQuantidadeIdealEstoque(Integer quantidadeIdealEstoque) {
		this.quantidadeIdealEstoque = quantidadeIdealEstoque;
	}

	public Integer getPercentualNotificacao() {
		return percentualNotificacao;
	}

	public void setPercentualNotificacao(Integer percentualNotificacao) {
		this.percentualNotificacao = percentualNotificacao;
	}

	public MedidaEnum getMedida() {
		return medida;
	}

	public void setMedida(MedidaEnum medida) {
		this.medida = medida;
	}

	public Integer getSituacaoPercentual() {
		return (quantidadeRealEstoque * 100) / quantidadeIdealEstoque;
	}

	public void setSituacaoPercentual(Integer situacaoPercentual) {
		this.situacaoPercentual = situacaoPercentual;
	}
	
	public long getSituacaoDataVencimento() {
		if (this.dataVencimento != null) {
				return BuildManagementUtils.diferencaDatas(this.dataVencimento);
			}
		
		return situacaoDataVencimento;
	}

	public void setSituacaoDataVencimento(long situacaoDataVencimento) {
		this.situacaoDataVencimento = situacaoDataVencimento;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public List<EstoqueProduto> getEstoqueProdutos() {
		return estoqueProdutos;
	}

	public void setEstoqueProdutos(List<EstoqueProduto> estoqueProdutos) {
		this.estoqueProdutos = estoqueProdutos;
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

	@Override
	public String toString() {
		return "Produto [idProduto=" + idProduto + ", codigo=" + codigo + ", nomeProduto=" + nomeProduto
				+ ", fabricante=" + fabricante + ", medida=" + medida + ", valorCusto=" + valorCusto + ", valorVenda="
				+ valorVenda + ", dataVencimento=" + dataVencimento + ", quantidadeRealEstoque=" + quantidadeRealEstoque
				+ ", quantidadeIdealEstoque=" + quantidadeIdealEstoque + ", percentualNotificacao="
				+ percentualNotificacao + ", status=" + status + ", situacaoPercentual=" + situacaoPercentual
				+ ", empresa=" + empresa + "]";
	}
}
