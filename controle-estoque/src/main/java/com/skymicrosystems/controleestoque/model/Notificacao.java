package com.skymicrosystems.controleestoque.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.skymicrosystems.controleestoque.enums.EventoEnum;
import com.skymicrosystems.controleestoque.utils.BuildManagementUtils;

@Entity
@Table(name = "NOTIFICACAO")
public class Notificacao extends AuditModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idNotificacao;
	
	
	private String chave;
	
	private EventoEnum evento;
	
	private String descricaoResumida;
	private String acao;
	private Integer criticidade;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idEmpresa", nullable = false)
	private Empresa notificacoesEmpresa;
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Notificacao() {}
	
	public Notificacao(Long idNotificacao) {
		super();
		this.idNotificacao = idNotificacao;
	}

	public Notificacao(String chave, EventoEnum evento, String descricaoResumida, String acao, Integer criticidade,
			Empresa notificacoesEmpresa) {
		super();
		this.chave = chave;
		this.evento = evento;
		this.descricaoResumida = descricaoResumida;
		this.acao = acao;
		this.criticidade = criticidade;
		this.notificacoesEmpresa = notificacoesEmpresa;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */

	public Long getIdNotificacao() {
		return idNotificacao;
	}

	public void setIdNotificacao(Long idNotificacao) {
		this.idNotificacao = idNotificacao;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getDescricaoResumida() {
		return descricaoResumida;
	}

	public void setDescricaoResumida(String descricaoResumida) {
		this.descricaoResumida = descricaoResumida;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public Integer getCriticidade() {
		return criticidade;
	}

	public void setCriticidade(Integer criticidade) {
		this.criticidade = criticidade;
	}

	public EventoEnum getEvento() {
		return evento;
	}

	public void setEvento(EventoEnum evento) {
		this.evento = evento;
	}

	public Empresa getNotificacoesEmpresa() {
		return notificacoesEmpresa;
	}

	public void setNotificacoesEmpresa(Empresa notificacoesEmpresa) {
		this.notificacoesEmpresa = notificacoesEmpresa;
	}
	
	/*
	 * PROCESSORS
	 */
	
	@PrePersist
	@Override
	protected void prePersist() {
		super.prePersist();
		if (getNotificacoesEmpresa() == null)
			this.setNotificacoesEmpresa(BuildManagementUtils.getEmpresaAuthenticated());
	}

	@PreUpdate
	@Override
	protected void preUpdate() {
		super.preUpdate();
		
		if (getNotificacoesEmpresa() == null) 
			this.setNotificacoesEmpresa(BuildManagementUtils.getEmpresaAuthenticated());
	}
}
