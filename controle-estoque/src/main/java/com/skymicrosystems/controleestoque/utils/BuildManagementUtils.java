package com.skymicrosystems.controleestoque.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.skymicrosystems.controleestoque.enums.TipoAcessoEnum;
import com.skymicrosystems.controleestoque.model.Empresa;
import com.skymicrosystems.controleestoque.model.Usuario;

@Component
public class BuildManagementUtils {
	
	@Autowired
	private Environment env;
	
	public static Usuario getUserAuthenticated() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public static Optional<Usuario> getUserAuthenticatedOptional() {
		return Optional.of(getUserAuthenticated());
	}
	
	public static Empresa getEmpresaAuthenticated() {
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		if (context != null && context.getAuthentication() != null) { 
		
			Usuario usuario = (Usuario) context.getAuthentication().getPrincipal();
			
			if (usuario != null && usuario.getTipoAcesso().equals(TipoAcessoEnum.EMPRESA)) {
				return usuario.getEmpresa();
			}
		}
		return null;
	}
	
	public static Boolean isAdministrador() {
		
		Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return isAdministrador(usuario);
	}
	
	public static Boolean isAdministrador(Usuario usuario) {
		return usuario != null ? usuario.getTipoAcesso().equals(TipoAcessoEnum.INTERNO) : Boolean.FALSE;
	}
	
	public static String getUsername() {
		return getUserAuthenticated().getUsername();
	}
	
	public static String defaultEncoder(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}
	
	public static String getStaticMessage(String key) {
		return ResourceBundle.getBundle("messages").getString(key);
	}
	
	public static String getStaticMessageFormatted(String key, Object... args) {
		return String.format(getStaticMessage(key), args);
	}
	
	public Integer getPropertyInteger(String key) {
		return Integer.parseInt(env.getProperty(key));
		
	}

	public static String defaultCodeFormat(String sigla, Long id, Integer tamanho) {
		if (id == null) {
			throw new IllegalArgumentException("Id nao informado");
		}
		
		return sigla.concat(StringUtils.leftPad(id.toString(), tamanho, "0"));
	}
	
	
	
	/**
	 * @param data
	 * @return
	 * 
	 *  - Retorno negativo a data esta vencida
	 *  - entre que x esta perto do vencimento 
	 *  - maior que x esta maior que o vencimento
	 *  
	 */
	public static Boolean dataVencida(LocalDate data) {
		return LocalDate.now().isAfter(data);
	}
	
	public static void main(String[] args) {
		System.out.println(diferencaDatas(LocalDate.now().plusDays(-25)));
		
		System.out.println(ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.now().plusDays(-25)));
		
	}
	
	public static long diferencaDatas(LocalDate data) {
		return ChronoUnit.DAYS.between(LocalDate.now(), data);
	}
	
	/*
	 * EXPORT DE ARQUIVOS
	 * 
	 */
	public String converListToCSV(List<String[]> dataLines) {
        
    	StringWriter sw = new StringWriter();
    	
            dataLines.stream()
              .map(this::convertToCSV)
              .map(this::escapeToCaracter)
              .forEach(sw::write);
    	
    	return sw.toString();
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data)
        		.collect(Collectors.joining(","));
    }
	
    private String escapeToCaracter(String string1) {
		return string1+"\n";
	}
}
