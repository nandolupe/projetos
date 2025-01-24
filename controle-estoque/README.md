

# PROJETO CONTROLE DE ESTOQUE



#### Executando

Comando para executar o projeto local

	mvn spring-boot:run
	
#### Deploy on Docker

	mvn clean install -Pdeploy-docker
	docker run --name controle-estoque -p 8081:8081 -e PROFILE=dev -d controle-estoque:1.0.0

#### Informações de Banco de dados

	create database controle_estoque_dev;
	CREATE USER 'controle-estoque'@'%' IDENTIFIED BY 'XX';
	GRANT ALL ON controle_estoque_dev.* TO 'controle-estoque'@'%';

	
