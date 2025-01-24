package com.skymicrosystems.controleestoque.forms;

import com.skymicrosystems.controleestoque.model.Notificacao;

public class NotificacaoSearchForm extends DefaultSearchForm<Notificacao> {

	public NotificacaoSearchForm(Notificacao data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
