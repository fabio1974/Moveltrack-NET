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
  document.getElementById('address').innerHTML = '<b>&nbsp;&nbsp;Cerca Virtual:&nbsp;</b>'+str;
}


/*function drawRectangle(sw,ne){
	rectangle = new google.maps.Rectangle({
	    bounds: new google.maps.LatLngBounds(sw,ne),
	    editable: true,
	    draggable: true
	});
	google.maps.event.addListener(rectangle,'bounds_changed',updateRectangle);
	rectangle.setMap(map);
	updateRectangleLabel();
}*/

function drawCircle(centro,raio){
	var options = {
		      //strokeColor: '#FF0000',
		      //strokeOpacity: 0.8,
		      //strokeWeight: 2,
		      //fillColor: '#FF0000',
		      //fillOpacity: 0.35,
		      center: centro,
		      radius: 1000 * raio,
		      editable: true,
		      draggable: true
		    };
	circle = new google.maps.Circle(options);
	google.maps.event.addListener(circle,'bounds_changed',updateCircle);
	circle.setMap(map);
	updateCircleLabel();
}


function updateCircle(event) {
	updateCircle();
}
function updateCircle() {
	  var radius = circle.getRadius()/1000;
	  var center = circle.getCenter();
	  document.getElementById('form:lat').value = center.lat(); 
	  document.getElementById('form:lon').value = center.lng();
	  document.getElementById('form:raio').value = radius;
	  updateCircleLabel();
}


function updateCircleLabel(){
	  //var radiusKm = circle.getRadius()/1000;
	  //radiusKm = Math.floor(radiusKm * 10) / 10;
	  //updateMarkerAddress("Raio: "+radiusKm+"km");
	
}


function clearFence(){
	if(circle!=null)
		circle.setMap(null);
	if(rectangle!=null)
		rectangle.setMap(null);
	document.getElementById('lat1').value = null; 
	document.getElementById('long1').value = null;
	document.getElementById('lat2').value = null; 
	document.getElementById('long2').value = null;
	document.getElementById('raio').value = 0;
    document.getElementById('addCircle').disabled = false;
    document.getElementById('addRectangle').disabled = false;
    document.getElementById('tipo').value = null;
    document.getElementById('address').innerHTML = '';
}

function addInitialCircle(){
	    document.getElementById('addCircle').disabled = true;
	    document.getElementById('addRectangle').disabled = false;
	    drawCircle(latlngCentro,1000);
		if(rectangle!=null)
			rectangle.setMap(null);
		document.getElementById('tipo').value = 'circle';
}


/*function addCircle(centro,raio){
	document.getElementById('addCircle').disabled = true;
	document.getElementById('addRectangle').disabled = false;
    drawCircle(centro,raio);
	if(rectangle!=null)
		rectangle.setMap(null);
	document.getElementById('tipo').value = 'circle';
}


function addInitialRectangle(){
    document.getElementById('addRectangle').disabled = true;
    document.getElementById('addCircle').disabled = false;
	drawRectangle(new google.maps.LatLng(latlngCentro.lat()-0.009,latlngCentro.lng()-0.009),new google.maps.LatLng(latlngCentro.lat()+0.009,latlngCentro.lng()+0.009));
	if(circle!=null)
		circle.setMap(null);
	document.getElementById('tipo').value = 'rectangle';
}


function addRectangle(sw,ne){
	document.getElementById('addRectangle').disabled = true;
	document.getElementById('addCircle').disabled = false;
	drawRectangle(sw,ne);
	if(circle!=null)
		circle.setMap(null);
	document.getElementById('tipo').value = 'rectangle';
}

function updateRectangle(event) {
	updateRectangle();
}
function updateRectangle(){
	  var ne = rectangle.getBounds().getNorthEast();
	  var sw = rectangle.getBounds().getSouthWest();
	  document.getElementById('lat1').value = sw.lat(); 
	  document.getElementById('long1').value = sw.lng();
	  document.getElementById('lat2').value = ne.lat(); 
	  document.getElementById('long2').value = ne.lng();
	  document.getElementById('raio').value = 0;
	  updateRectangleLabel();
}*/


/*function updateRectangleLabel(){
	  var ne = rectangle.getBounds().getNorthEast();
	  var sw = rectangle.getBounds().getSouthWest();
	  var nw = new google.maps.LatLng(ne.lat(),sw.lng());
	  var largura = (google.maps.geometry.spherical.computeDistanceBetween (nw,ne)/1000);
	  var altura =  (google.maps.geometry.spherical.computeDistanceBetween (nw,sw)/1000);
	  largura = Math.floor(largura * 10) / 10;
	  altura = Math.floor(altura * 10) / 10;
	  updateMarkerAddress("Retângulo:"+largura+"km x "+altura+"km.");
}*/




/*function formSubmit(){
	document.getElementById('count').value = '1';
	if(document.getElementById('tipo').value=='circle')
		updateCircle();
	else if(document.getElementById('tipo').value=='rectangle')
		updateRectangle();
	document.form.submit();
}*/


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