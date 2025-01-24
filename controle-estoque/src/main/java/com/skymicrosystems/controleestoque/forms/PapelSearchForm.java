package com.skymicrosystems.controleestoque.forms;

import com.skymicrosystems.controleestoque.model.Papel;

public class PapelSearchForm extends DefaultSearchForm<Papel> {

	public PapelSearchForm(Papel data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
