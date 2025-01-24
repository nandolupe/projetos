package com.exercicios.exerciciocrud.forms;

import com.exercicios.exerciciocrud.model.Usuario;

public class UsuarioSearchForm{
	
	private Usuario data;
	private	Integer pageNumber; 
	private Integer size;
	
	public UsuarioSearchForm() {}
	
	public UsuarioSearchForm(Usuario data, Integer pageNumber, Integer size) {
		this.data = data;
		this.pageNumber  = pageNumber;
		this.size = size;
	}
	
	public Usuario getData() {
		return data;
	}

	public void setData(Usuario data) {
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
