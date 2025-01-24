package com.skymicrosystems.controleestoque.enums;

public enum EventoEnum {
	
	PRODUTO_DATA_VENCIMENTO_PROXIMA("PRODUTO DATA VENCIMENTO PROXIMO", 2), 
	PRODUTO_DATA_VENCIDA("PRODUTO COM DATA VENCIDA", 0), 
	PRODUTO_ESTOQUE_BAIXO("PRODUTO COM BAIXO ESTOQUE", 2), 
	PRODUTO_SEM_ESTOQUE("PRODUTO SEM ESTOQUE", 0);
	
	private EventoEnum(String name, Integer criticidade) {
		this.name = name;
		this.criticidade = criticidade;
	}
	
	private String name;
	private Integer criticidade;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getCriticidade() {
		return criticidade;
	}

	public void setCriticidade(Integer criticidade) {
		this.criticidade = criticidade;
	}

	public String[] toList() {
		return new String[]{
				PRODUTO_DATA_VENCIMENTO_PROXIMA.getName(), 
				PRODUTO_DATA_VENCIDA.getName(), 
				PRODUTO_ESTOQUE_BAIXO.getName(), 
				PRODUTO_SEM_ESTOQUE.getName()};
	}
}
