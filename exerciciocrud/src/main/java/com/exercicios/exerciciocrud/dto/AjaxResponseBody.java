package com.exercicios.exerciciocrud.dto;

import java.util.List;

import com.exercicios.exerciciocrud.model.Produto;

public class AjaxResponseBody {
	
	private String msg;
	private List<Produto> result;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<Produto> getResult() {
		return result;
	}
	public void setResult(List<Produto> result) {
		this.result = result;
	}
}
