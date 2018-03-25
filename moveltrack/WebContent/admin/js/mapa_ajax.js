

function desenhaPontoDeVelocidade(loc){
	var title=  loc.velocidade+"Km/h" + " em "+ loc.dateLocation;
	var htmlContent = "<div id='apl_tabela_impressao_01'><table  border='1'>"
		+"<tr><td class='titimpressao01'>Placa</td><td class='dados'>"+placa+"</td></tr>"
		+"<tr><td class='titimpressao01'>Marca/Modelo</td><td class='dados'>"+marcaModelo+"</td></tr>"
	   	+"<tr><td class='titimpressao01'>Velocidade</td><td class='dados'>"+(loc.velocidade).toFixed(2)+"km/h</td></tr>"
	   	+"<tr><td class='titimpressao01'>Data da Leitura</td><td class='dados'>"+dateFormat(loc.dateLocation,"dd/mm/yyyy HH:MM")+"</td></tr>"
        +"</table></div>" ;

	if(loc.velocidade < 15) 
		addPontoDeVelocidade(loc.latitude,loc.longitude,"green",htmlContent,title);
	else if(loc.velocidade >= 60 && loc.velocidade< 80)
		addPontoDeVelocidade(loc.latitude,loc.longitude,"yellow",htmlContent,title);
	else if(loc.velocidade > 80)
		addPontoDeVelocidade(loc.latitude,loc.longitude,"red",htmlContent,title);
}


function desenhaPontoDeVelocidadeSm(loc){
	var title=  loc.velocidade+"Km/h" + " em "+ loc.dateLocation;
	var htmlContent = "<div id='apl_tabela_impressao_01'><table  border='1'>"
		+"<tr><td class='titimpressao01'>Placa</td><td class='dados'>"+placa+"</td></tr>"
		+"<tr><td class='titimpressao01'>Latitude</td><td class='dados'>"+(loc.latitude).toFixed(6)+"</td></tr>"
		+"<tr><td class='titimpressao01'>Longitude</td><td class='dados'>"+(loc.longitude).toFixed(6)+"</td></tr>"
		+"<tr><td class='titimpressao01'>Marca/Modelo</td><td class='dados'>"+marcaModelo+"</td></tr>"
	   	+"<tr><td class='titimpressao01'>Velocidade</td><td class='dados'>"+(loc.velocidade).toFixed(2)+"km/h</td></tr>"
	   	+"<tr><td class='titimpressao01'>Data da Leitura</td><td class='dados'>"+dateFormat(loc.dateLocation,"dd/mm/yyyy HH:MM:ss")+"</td></tr>"
        +"</table></div>" ;

	if(loc.velocidade < 15) 
		addPontoDeVelocidade(loc.latitude,loc.longitude,"green-sm",htmlContent,title);
	else if(loc.velocidade >= 15 && loc.velocidade< 60)
		addPontoDeVelocidade(loc.latitude,loc.longitude,"blue-sm",htmlContent,title);
	else if(loc.velocidade >= 60 && loc.velocidade< 90)
		addPontoDeVelocidade(loc.latitude,loc.longitude,"yellow-sm",htmlContent,title);
	else if(loc.velocidade >= 90)
		addPontoDeVelocidade(loc.latitude,loc.longitude,"red-sm",htmlContent,title);
}


function desenhaLastLoc(lastLoc){
	var htmlContent = "<div id='apl_tabela_impressao_01'><table  border='1'>"
						+"<tr><td class='titimpressao01'>Placa</td><td class='dados'>"+lastLoc.placa+"</td></tr>"
						+"<tr><td class='titimpressao01'>Marca/Modelo</td><td class='dados'>"+lastLoc.marcaModelo+"</td></tr>"
						+"<tr><td class='titimpressao01'>Velocidade</td><td class='dados'>"+(lastLoc.location.velocidade).toFixed(2)+"km/h</td></tr>"
						+"<tr><td class='titimpressao01'>Última Leitura</td><td class='titimpressao01'>"+dateFormat(lastLoc.location.dateLocation,"dd/mm/yyyy HH:MM")+"</td></tr>"
						+"<tr><td class='titimpressao01'>Endereço</td><td class='dados'>"+lastLoc.endereco+"</td></tr>"
						
						+"</table></div>" ;
	var title = lastLoc.location.velocidade+"Km/h" + " em "+lastLoc.location.dateLocation;
	addVeiculo(lastLoc.location.latitude,lastLoc.location.longitude,lastLoc.veiculoTipo,htmlContent,title);	
}


function desenhaLastLocXs(lastLoc){
	
	var link = lastLoc.modeloRastreador === 'SPOT_TRACE'? "<tr><td colspan='2' class='titimpressao01'><a href='"+webroot+"/admin/mapaVeiculoSpot.xhtml?id="+lastLoc.veiculoId+"'>RASTREAR VEÍCULO</a></td></tr>":
														 "<tr><td colspan='2' class='titimpressao01'><a href='"+webroot+"/admin/mapaVeiculoCompleto.xhtml?id="+lastLoc.veiculoId+"'>RASTREAR VEÍCULO</a></td></tr>"
	
	var htmlContent = "<div id='apl_tabela_impressao_01'><table  border='1'>"
						+"<tr><td class='titimpressao01'>Placa</td><td class='dados'>"+lastLoc.placa+"</td></tr>"
						+"<tr><td class='titimpressao01'>Marca/Modelo</td><td class='dados'>"+lastLoc.marcaModelo+"</td></tr>"
						+"<tr><td class='titimpressao01'>Velocidade</td><td class='dados'>"+(lastLoc.location.velocidade).toFixed(2)+"km/h</td></tr>"
						+"<tr><td class='titimpressao01'>Última Leitura</td><td class='titimpressao01'>"+dateFormat(lastLoc.location.dateLocation,"dd/mm/yyyy HH:MM")+"</td></tr>"
						+"<tr><td class='titimpressao01'>Endereço</td><td class='dados'>"+lastLoc.endereco+"</td></tr>"
						
						+link
						
						+"</table></div>" ;
	var title = lastLoc.location.velocidade+"Km/h" + " em "+lastLoc.location.dateLocation;
	addVeiculoXs(lastLoc.location.latitude,lastLoc.location.longitude,lastLoc.veiculoTipo,htmlContent,title);	
}


function desenhaVeiculo(lastLoc){
	var htmlContent = "<div id='apl_tabela_impressao_01'><table  border='1'>"
						+"<tr><td class='titimpressao01'>Placa</td><td class='dados'>"+placa+"</td></tr>"
						+"<tr><td class='titimpressao01'>Marca/Modelo</td><td class='dados'>"+marcaModelo+"</td></tr>"
						+"<tr><td class='titimpressao01'>Velocidade</td><td class='dados'>"+(lastLoc.velocidade).toFixed(2)+"km/h</td></tr>"
						+"<tr><td class='titimpressao01'>Distância Percorrida</td><td class='dados'>"+(distancia/1000).toFixed(2)+"km</td></tr>"
						+"<tr><td class='titimpressao01'>Última Leitura</td><td class='titimpressao01'>"+dateFormat(lastLoc.dateLocation,"dd/mm/yyyy HH:MM:ss")+"</td></tr>"
						+"<tr><td class='titimpressao01'>Endereço</td><td class='dados'>"+lastLoc.endereco+"</td></tr>"
						
						+"</table></div>" ;
	var title = (lastLoc.velocidade).toFixed(2)+"km/h" + " em "+dateFormat(lastLoc.dateLocation,"dd/mm/yyyy HH:MM:ss");
	addVeiculo(lastLoc.latitude,lastLoc.longitude,veiculoTipo,htmlContent,title);	
	
	$("#form\\:ultimoSinal").html(dateFormat(lastLoc.dateLocation,"dd/mm/yy HH:MM"));
	$("#form\\:velocidade").html((lastLoc.velocidade).toFixed(2)+ " km/h");
	$("#form\\:distanciaPercorrida").html((distancia/1000).toFixed(2)+"km");

	
}

function desenhaPartida(firstLoc){
	
	var htmlContent = "<div id='apl_tabela_impressao_01'><table  border='1'>"
		+"<tr><td class='titimpressao01'>Inicio de Viagem</td><td class='titimpressao01'>"+dateFormat(firstLoc.dateLocation,"dd/mm/yyyy   HH:MM")+"</td></tr>"
		+"<tr><td class='titimpressao01'>Velocidade</td><td class='dados'>"+firstLoc.velocidade+"km/h</td></tr>"

		+"</table></div>" ;


	var title = firstLoc.velocidade+"Km/h" + " em "+firstLoc.dateLocation;
	var latLng = new google.maps.LatLng(firstLoc.latitude,firstLoc.longitude);
	
	var imageIcon = new StyledIcon(StyledIconTypes.BUBBLE,{color:"#6f3",text:"inicio"});

	var  marker = new StyledMarker({
	    	styleIcon:imageIcon,
	    	position:latLng,
	    	map: map,
	    	title:title
	    });
	
	pontosDeParada.push(marker);
	
   	google.maps.event.addListener(marker,'click',function(){
		  infowindow.setContent(htmlContent);
		  infowindow.setPosition(latLng);
		  infowindow.open(map);
 	});
	
}




function desenhaCaminho(){
    caminho = new google.maps.Polyline({
        path:  pontosCaminho,
        icons: [{
                   	icon: seta,
   	     	   		offset: '0',
               		repeat: '200px'
	             }],
        strokeColor: "#0000FF",
        strokeOpacity: 0.8,
	    strokeWeight: 3
   });
   caminho.setMap(map);
   return google.maps.geometry.spherical.computeLength(caminho.getPath().getArray());
}

var lineSymbol = {
        path: 'M 0,-1 0,1',
        strokeOpacity: 0.6,
        strokeWeight: 2,
        scale: 3
      };

var seta2 = {
		    path: google.maps.SymbolPath.FORWARD_OPEN_ARROW,
		    strokeOpacity: 0.8,
		    strokeWeight: 2,
		    scale: 2
 };

function desenhaCaminhoSpot(){
    caminho = new google.maps.Polyline({
        path:  pontosCaminho,
        icons: [{
                   	icon: seta2,
   	     	   		offset: '0',
               		repeat: '200px'
	             },
        
        {
            icon: lineSymbol,
            offset: '0',
            repeat: '15px'
          }
        
        
        ],
        strokeColor: "#0000FF",
        strokeOpacity: 0
	    
   });
   caminho.setMap(map);
   return google.maps.geometry.spherical.computeLength(caminho.getPath().getArray());
}



function desenhaParada(loc,k){
	 var title=  addLeadingZeros(k+1,2)+" "+ datediff(new Date(loc.dateLocation),new Date(loc.dateLocationInicio));
	 var htmlContent = "<div id='apl_tabela_impressao_01'><table width='350'  border='1'>"
		    +"<tr><td align='center' class='titimpressao01'>"+addLeadingZeros(k+1,2)+"ª Parada</td><td class='dados'></td></tr>"
		   	+"<tr><td class='titimpressao01'>Início de Parada</td><td class='dados'>"+dateFormat(loc.dateLocationInicio,"dd/mm/yyyy HH:MM")+"</td></tr>"
		   	+"<tr><td class='titimpressao01'>Fim de Parada</td><td class='dados'>"+dateFormat(loc.dateLocation,"dd/mm/yyyy HH:MM")+"</td></tr>"
		   	+"<tr><td class='titimpressao01'>Permanência (hora:min:seg)</td><td class='dados'>"+datediff(new Date(loc.dateLocation),new Date(loc.dateLocationInicio))+"</td></tr>"
     +"</table></div>" ;
	 showMarkerDeParada(loc.latitude,loc.longitude,htmlContent,title,addLeadingZeros(k+1,2));
}


function addVeiculo(lat,lon,iconText,htmlContent,title){
	var latLng = new google.maps.LatLng(lat,lon);
	var marker = new google.maps.Marker({
	      position: latLng,
	      map: map,
	      title:title,
	      icon: getIcon(iconText),
	      optimized: false
	});
	veiculosArray.push(marker);
	google.maps.event.addListener(marker,'click',function(){
		  infowindow.setContent(htmlContent);
		  infowindow.setPosition(latLng);
		  infowindow.open(map);
	});
}

function addVeiculoXs(lat,lon,iconText,htmlContent,title){
	var latLng = new google.maps.LatLng(lat,lon);
	var marker = new google.maps.Marker({
	      position: latLng,
	      map: map,
	      title:title,
	      icon: getIconXs(iconText),
	      optimized: false
	});
	veiculosArray.push(marker);
	google.maps.event.addListener(marker,'click',function(){
		  infowindow.setContent(htmlContent);
		  infowindow.setPosition(latLng);
		  infowindow.open(map);
	});
}


function showMarkerDeParada(lat,lon,htmlContent,title,indice){
	var latLng = new google.maps.LatLng(lat,lon);
    var imageIcon = new StyledIcon(StyledIconTypes.MARKER,{color:"#FF5500",text:indice});
    marker = new StyledMarker({
    	styleIcon:imageIcon,
    	position:latLng,
    	map: map,
    	title:title
    });
    pontosDeParada.push(marker);
   	google.maps.event.addListener(marker,'click',function(){
		  infowindow.setContent(htmlContent);
		  infowindow.setPosition(latLng);
		  infowindow.open(map);
   	});
}



function addPontoDeVelocidade(lat,lon,iconText,htmlContent,title){
	var latLng = new google.maps.LatLng(lat,lon);
	var marker = new google.maps.Marker({
	      position: latLng,
	      map: map,
	      title:title,
	      icon: getIcon(iconText),
	      optimized: false
	});
	pontosDeVelocidade.push(marker);
	google.maps.event.addListener(marker,'click',function(){
		  infowindow.setContent(htmlContent);
		  infowindow.setPosition(latLng);
		  infowindow.open(map);
	});
}



function clearVeiculos() {
	if (veiculosArray){
	    for (i in veiculosArray) {
	    	veiculosArray[i].setMap(null);
	    }
    }
}

function clearPontosDeVelocidade(){
	if (pontosDeVelocidade){
	    for (i in pontosDeVelocidade) {
	    	pontosDeVelocidade[i].setMap(null);
	    }
    }
}

function clearPontosDeParada(){
	if (pontosDeParada){
	    for (i in pontosDeParada) {
	    	pontosDeParada[i].setMap(null);
	    }
    }
}

function clearPontosCaminho(){
	if(caminho)
	    	caminho.setMap(null);
}

function clearAll(){
	clearPontosDeVelocidade();
	clearPontosDeParada();
	clearPontosCaminho();
}



function printStatus(loc){
	
	var amarelo = "background-color:#ff0; opacity:1;color:#000";
	var laranja = "background-color:#fc0; opacity:1;color:#000";
	var verdelouro = "background-color:#6f3; opacity:1;color:#000";
	var vermelho = "background-color:#f00; opacity:1;color:#fff";
	var verde = "background-color:#0f0; opacity:1;color:#000";
	var padrao = "background-color:#eee; opacity:1;color:#000";
	
	
	
	
	
	var newClass = loc!=null && loc.battery!=null?(loc.battery=="1"?verde:loc.battery=="0"?vermelho:padrao):padrao;
    var newValue = loc!=null && loc.battery!=null?(loc.battery=="1"?"Lig.":loc.battery=="0"?"Desl.":loc.battery):"-";
    $("#sinal\\:bateria").attr('style',newClass);
	$("#sinal\\:bateria").html(newValue);
	
	//-------------------------------------------
	// Indicadores do Mapa do Spot
    //$("#form\\:bateria").attr('style',newClass);
	$("#form\\:bateria").html(newValue);

	//s$("#form\\:comando").attr('style',padrao);
	$("#form\\:comando").html(loc.comando);
	//-------------------------------------------
	
	
	var cor = "";

	if(loc.volt=="0%")
		cor = vermelho;
	else if(loc.volt=="<5%")
		cor = vermelho;
	else if(loc.volt=="<10%")
		cor = vermelho;
	else if(loc.volt=="<30%")
		cor = laranja;
	else if(loc.volt=="~50%")
		cor = amarelo;
	else if(loc.volt==">70%")
		cor = verdelouro;
	else if(loc.volt==">90%")
		cor = verde;
	
	$("#sinal\\:voltagem").attr('style',cor);
	$("#sinal\\:voltagem").html(loc.volt);
	
	
	
	
	var gsm = "";
    cor = "";
	if(loc!=null && loc.gsm!=null){
		if(loc.gsm=="0"){
			cor = vermelho;
			gsm = "0%";       // );// - No signal");
		}else if(loc.gsm=="1"){
			cor = laranja;
			gsm = "<20%";//Muito fraco");// - Extremely weak signal");
		}else if(loc.gsm=="2"){
			cor = amarelo;
			gsm = "~50%";// - Very weak signal");
		}else if(loc.gsm=="3"){
			cor = verdelouro;
			gsm = "~75%";//Bom");// - Good signal");
		}else if(loc.gsm=="4"){
			cor = verde;
			gsm = ">90%";//Muito Bom");// - Strong signal");
		}	
	};
	
	$("#sinal\\:gsm").attr('style',cor);
	$("#sinal\\:gsm").html(gsm);
	
	$("#sinal\\:gps").attr('style',loc!=null && loc.gps!=null?(loc.gps=="On"?verde:vermelho):padrao);
	$("#sinal\\:gps").html(loc!=null && loc.gps!=null?loc.gps:"-");

	$("#sinal\\:satelites").attr('style',loc!=null?(loc.satelites<"3"?vermelho:verde):padrao);
	$("#sinal\\:satelites").html(loc!=null?(loc.satelites>="0"?loc.satelites:"-"):"-");
	
	$("#sinal\\:bloqueio").attr('style',loc!=null && loc.bloqueio!=null?(loc.bloqueio=="Sim"?vermelho:verde):padrao);
	$("#sinal\\:bloqueio").html(loc!=null && loc.bloqueio!=null?(loc.bloqueio=="Sim"?"On":"Off"):"-");
	
	$("#sinal\\:ignicao").attr('style',loc!=null && loc.ignition!=null?(loc.ignition=="0"?amarelo:verde):padrao);
	$("#sinal\\:ignicao").html(loc!=null && loc.ignition!=null?(loc.ignition=="0"?"Off":"On"):"-");
	
		
	var alarmType="";
	cor = padrao;
	
	if(loc!=null && loc.alarmType!=null){
		if(loc.alarmType=="100"){
			alarmType = "Pânico!";
			cor = vermelho;
		}	
		else if(loc.alarmType=="011"){
			alarmType = "Bateria Fraca!";
			cor = laranja;
		}	
		else if(loc.alarmType=="010"){
			alarmType = "Desconectando Bateria!";
			cor = laranja;
		}	
		else if(loc.alarmType=="001"){
			alarmType = "Shock";
			cor = vermelho;
		}	
		else if(loc.alarmType=="000"){
			alarmType = "Off";
		}	
	};
	
	$("#sinal\\:alarme").attr('style',cor);
	$("#sinal\\:alarme").html(alarmType);

	$("#sinal\\:dataStatus").html(dateFormat(loc.dateLocation,"HH:MM"));
	
	
	
	
	
}


function getIcon(object){
	var size = '';
	var offset = '';
	var origin = new google.maps.Point(0,0);
	if(object=='AUTOMOVEL'){
		size = new google.maps.Size(48,25);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return new google.maps.MarkerImage('images/carro.gif',size,origin,offset);
	}else if(object=='MOTOCICLETA'){
		size = new google.maps.Size(48,36);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return  new google.maps.MarkerImage('images/moto.gif',size,origin,offset);
	}else if(object=='TRATOR'){
		size = new google.maps.Size(48,29);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return new google.maps.MarkerImage('images/trator.gif',size,origin,offset);
	}else if(object=='PICKUP'){
		size = new google.maps.Size(48,31);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return  new google.maps.MarkerImage('images/pickup.gif',size,origin,offset);
	}else if(object=='CAMINHAO'){
		size = new google.maps.Size(48,36);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return  new google.maps.MarkerImage('images/truck.gif',size,origin,offset);
	}else if(object=='yellow-sm'){
		size = new google.maps.Size(32,32);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return new google.maps.MarkerImage('images/bullet-yellow-sm.png',size,origin,offset);
	}else if(object=='blue-sm'){
		size = new google.maps.Size(32,32);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return new google.maps.MarkerImage('images/bullet-blue-sm.png',size,origin,offset);
	}else if(object=='red-sm'){
		size = new google.maps.Size(32,32);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return new google.maps.MarkerImage('images/bullet-red-sm.png',null,origin,offset);
	}else if(object=='green-sm'){
		size = new google.maps.Size(32,32);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return new google.maps.MarkerImage('images/bullet-green-sm.png',size,origin,offset);
	}else if(object=='yellow'){
		size = new google.maps.Size(8,8);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return new google.maps.MarkerImage('images/bullet-yellow.png',size,origin,offset);
	}else if(object=='blue'){
		size = new google.maps.Size(8,8);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return new google.maps.MarkerImage('images/bullet-blue.png',size,origin,offset);
	}else if(object=='red'){
		size = new google.maps.Size(8,8);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return new google.maps.MarkerImage('images/bullet-red.png',null,origin,offset);
	}else if(object=='green'){
		size = new google.maps.Size(8,8);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return new google.maps.MarkerImage('images/bullet-green.png',size,origin,offset);
	}else if(object=='map_red'){
		size = new google.maps.Size(32,32);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return new google.maps.MarkerImage('images/map_end.png',size,origin,offset);
	}  
}



function getIconXs(object){
	var size = '';
	var offset = '';
	var origin = new google.maps.Point(0,0);
	if(object=='AUTOMOVEL'){
		size = new google.maps.Size(30,16);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return new google.maps.MarkerImage('images/carro-xs.gif',size,origin,offset);
	}else if(object=='MOTOCICLETA'){
		size = new google.maps.Size(30,23);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return  new google.maps.MarkerImage('images/moto-xs.gif',size,origin,offset);
	}else if(object=='TRATOR'){
		size = new google.maps.Size(30,18);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return new google.maps.MarkerImage('images/trator-xs.gif',size,origin,offset);
	}else if(object=='PICKUP'){
		size = new google.maps.Size(30,19);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return  new google.maps.MarkerImage('images/pickup-xs.gif',size,origin,offset);
	}else if(object=='CAMINHAO'){
		size = new google.maps.Size(30,23);
		offset =new google.maps.Point(size.width/2,size.height/2);
		return  new google.maps.MarkerImage('images/truck-xs.gif',size,origin,offset);
	}  
} 



function geocodePosition(pos) {
	  geocoder.geocode({
	    latLng: pos
	  }, function(responses) {
	    if (responses && responses.length > 0) {
	      updateMarkerAddress(responses[0].formatted_address);
	    } else {
	      updateMarkerAddress('Impossível determinar o endereço deste local!');
	    }
	  });
	}

	//function updateMarkerStatus(str) {
	  //document.getElementById('markerStatus').innerHTML = '<b>Status:&nbsp;</b>'+str;
	//}

	function updateMarkerPosition(latLng) {
	  document.getElementById('info').innerHTML = '<b>Lat,Long:&nbsp;</b>'+[
	    latLng.lat(),
	    latLng.lng()
	  ].join(', ');
	}

	function updateMarkerAddress(str) {
	  document.getElementById('address').innerHTML = '<b>Endereço:&nbsp;</b>'+str;
	}

	function closeAddressFinder(){
		markerInfo.setMap(null);
	}

	function showAddressFinder(){
		if(document.getElementById("btFindAddress").value == "Buscar Endereço"){
			if(count<3){
			    alert("Arraste o balão amarelo para procurar um endereço qualquer no mapa.")
			    count++;
			}    
			markerInfo.setMap(map);
			markerInfo.setPosition(map.getCenter());
			document.getElementById("btFindAddress").value = "Esconder Busca";
		    updateMarkerPosition(map.getCenter());
		    geocodePosition(map.getCenter());
		}else{
			markerInfo.setMap(null);
			document.getElementById("btFindAddress").value = "Buscar Endereço";
			updateMarkerAddress("");
			document.getElementById('info').innerHTML = '<b>Lat,Long:&nbsp;</b>';
		}
	}
	
	
	function isParada(loc){
		if(loc.velocidade > 0)
			return false;
		return (new Date(loc.dateLocation)).getTime() - (new Date(loc.dateLocationInicio)).getTime() > 3*60*1000;
	}



	function addLeadingZeros(num, size) {
	    var s = num+"";
	    while (s.length < size) s = "0" + s;
	    return s;
	}


	function datediff(date2,date1) {
		  //Get 1 day in milliseconds
		  var one_day=1000*60*60*24;

		  // Convert both dates to milliseconds
		  var date1_ms = date1.getTime();
		  var date2_ms = date2.getTime();

		  // Calculate the difference in milliseconds
		  var difference_ms = date2_ms - date1_ms;
		  //take out milliseconds
		  difference_ms = difference_ms/1000;
		  var seconds = Math.floor(difference_ms % 60);
		  difference_ms = difference_ms/60; 
		  var minutes = Math.floor(difference_ms % 60);
		  difference_ms = difference_ms/60; 
		  var hours = Math.floor(difference_ms % 24);  
		  var days = Math.floor(difference_ms/24);
		  
		  return (days>0?addLeadingZeros(days,2):'') + addLeadingZeros(hours,2) + ':' + addLeadingZeros(minutes,2) + ':' + addLeadingZeros(seconds,2);
	}

