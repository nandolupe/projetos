package com.exercicios.exerciciocrud.forms;

import com.exercicios.exerciciocrud.model.Produto;

public class ProdutoSearchForm{
	
	private Produto data;
	private	Integer pageNumber; 
	private Integer size;
	
	public ProdutoSearchForm() {}
	
	public ProdutoSearchForm(Produto data, Integer pageNumber, Integer size) {
		this.data = data;
		this.pageNumber  = pageNumber;
		this.size = size;
	}
	
	public Produto getData() {
		return data;
	}

	public void setData(Produto data) {
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
