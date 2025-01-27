package com.exercicios.exerciciocrud.forms;

import com.exercicios.exerciciocrud.model.Orcamento;

public class OrcamentoSearchForm{
	
	private Orcamento data;
	private	Integer pageNumber; 
	private Integer size;
	
	public OrcamentoSearchForm() {}
	
	public OrcamentoSearchForm(Orcamento data, Integer pageNumber, Integer size) {
		this.data = data;
		this.pageNumber  = pageNumber;
		this.size = size;
	}
	
	public Orcamento getData() {
		return data;
	}

	public void setData(Orcamento data) {
		this.data = data;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
}
