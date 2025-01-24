package com.skymicrosystems.controleestoque.forms;

import com.skymicrosystems.controleestoque.model.Produto;

public class ProdutoSearchForm extends DefaultSearchForm<Produto> {

	public ProdutoSearchForm(Produto data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
