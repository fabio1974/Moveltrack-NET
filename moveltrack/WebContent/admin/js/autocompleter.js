
$(document).ready(function() {
	$(function() {
		
		$("#form\\:cliente,#form\\:table\\:cliente").autocomplete({
			minLength: 3,
			source : function(request, response) {
				$.ajax({
					url : webContext+"/Clientes",
					type : "POST",
					data : {
						term : request.term
					},
					dataType : "json",
					success : function(data) {
						response($.map(data, function(item) {
                            return {
                                label: item.nome + "-" +  item.cpf,
                                value: item.nome
                                };
                        }));
					}
				});
			}
		});
	});
	
	

	
	
	
	
	$(function() {	
		$("#form\\:vendedor,#form\\:table\\:vendedor").autocomplete({
			minLength: 3,
			source : function(request, response) {
				$.ajax({
					url : webContext+"/Pessoas",
					type : "POST",
					data : {
						term : request.term
					},
					dataType : "json",
					success : function(data) {
						response($.map(data, function(item) {
	                        return {
	                            label: item.nome + "-" + item.cpf,
	                            value: item.nome
	                            };
	                    }));
					}
				});
			}
		});
	});
	
	
	
	$(function() {	
		$("#form\\:veiculo,#form\\:table\\:veiculo").autocomplete({
			minLength: 3,
			source : function(request, response) {
				$.ajax({
					url : webContext+"/Veiculos",
					type : "POST",
					data : {
						term : request.term
					},
					dataType : "json",
					success : function(data) {
						response($.map(data, function(item) {
	                        return {
	                            label: item.placa + "-" + item.marcaModelo,
	                            value: item.placa
	                            };
	                    }));
					}
				});
			}
		});
	});	
	
	
	
	$(function() {	
		$("#form\\:empregado,#form\\:table\\:empregado,#form\\:instalador,#form\\:table\\:instalador,#form\\:emissor,#form\\:table\\:emissor," +
		  "#form\\:possuidor,#form\\:table\\:possuidor,#form\\:solicitante,#form\\:table\\:solicitante,#form\\:operador,#form\\:table\\:operador").autocomplete({
			minLength: 3,
			source : function(request, response) {
				$.ajax({
					url : webContext+"/Empregados",
					type : "POST",
					data : {
						term : request.term
					},
					dataType : "json",
					success : function(data) {
						response($.map(data, function(item) {
	                        return {
	                            label: item.nome + "-" + item.cpf,
	                            value: item.nome
	                            };
	                    }));
					}
				});
			}
		});
	});

	
	
	$(function() {	
		$("#form\\:chip,#form\\:table\\:chip").autocomplete({
			minLength: 3,
			source : function(request, response) {
				$.ajax({
					url : webContext+"/Chips",
					type : "POST",
					data : {
						term : request.term
					},
					dataType : "json",
					success : function(data) {
						response($.map(data, function(item) {
	                        return {
	                            label: item.iccid + "-" + item.operadora,
	                            value: item.iccid
	                            };
	                    }));
					}
				});
			}
		});
	});
	
	
	$(function() {	
		$("#form\\:equipamento,#form\\:table\\:equipamento,#form\\:imei,#form\\:table\\:imei").autocomplete({
			minLength: 3,
			source : function(request, response) {
				$.ajax({
					url : webContext+"/Equipamentos",
					type : "POST",
					data : {
						term : request.term
					},
					dataType : "json",
					success : function(data) {
						response($.map(data, function(item) {
	                        return {
	                            label: item.imei + "-" + item.modelo,
	                            value: item.imei + "-" + item.modelo
	                            };
	                    }));
					}
				});
			}
		});
	});
	
	
	$(function() {	
		$("#form\\:municipio,#form\\:table\\:municipio,#form\\:cidadeDestino,#form\\:table\\:cidadeDestino").autocomplete({
			minLength: 3,
			source : function(request, response) {
				$.ajax({
					url : webContext+"/Municipios",
					type : "POST",
					data : {
						term : request.term
					},
					dataType : "json",
					success : function(data) {
						response($.map(data, function(item) {
							console.log(item.descricao);
	                        return {
	                            label: item.descricao + "-" + item.uf.sigla,
	                            value: item.descricao + "-" + item.uf.sigla
	                            };
	                    }));
					}
				});
			}
		});
	});
	
	
	$(function() {	
		$("#form\\:viagem,#form\\:table\\:viagem").autocomplete({
			minLength: 3,
			source : function(request, response) {
				$.ajax({
					url : webContext+"/Viagens",
					type : "POST",
					data : {
						term : request.term
					},
					dataType : "json",
					success : function(data) {
						response($.map(data, function(item) {
							console.log(item.placa);
	                        return {
	                            label: item.numeroViagem,
	                            value: item.numeroViagem
	                            };
	                    }));
					}
				});
			}
		});
	});

	
	
	$(function() {	
		$("#form\\:contrato,#form\\:table\\:contrato").autocomplete({
			minLength: 3,
			source : function(request, response) {
				$.ajax({
					url : webContext+"/Contratos",
					type : "POST",
					data : {
						term : request.term
					},
					dataType : "json",
					success : function(data) {
						response($.map(data, function(item) {
							console.log(item.descricao);
	                        return {
	                            label: item.numeroContrato + "-" + item.cliente.nome,
	                            value: item.numeroContrato + "-" + item.cliente.nome
	                            };
	                    }));
					}
				});
			}
		});
	});
	
	
	
	
	
});
