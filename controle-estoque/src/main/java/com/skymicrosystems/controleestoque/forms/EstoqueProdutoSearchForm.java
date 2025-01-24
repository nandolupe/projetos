package com.skymicrosystems.controleestoque.forms;

import com.skymicrosystems.controleestoque.model.EstoqueProduto;

public class EstoqueProdutoSearchForm extends DefaultSearchForm<EstoqueProduto> {

	public EstoqueProdutoSearchForm(EstoqueProduto data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
