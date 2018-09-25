import React, {Component} from 'react'
import {GoogleApiWrapper, Map, Marker, Polyline} from 'google-maps-react';
import If from "../common/operator/If";


export class MapComponent extends Component {

  constructor(props) {
    super(props);
  }



  componentDidMount() {
  }




  render() {


    const posicaoVeiculo = this.props.polyline[this.props.polyline.length-1]

    return (
      <Map onReady={this.props.onMapReady} google={this.props.google} zoom={14} initialCenter={{lat: -3.802774,lng: -38.536376}} style={{width:  '100%',height: '100%'}}>




       <Marker
          title={'Última posição do Veículo.'}
          visible={this.props.polyline.length > 0}
          icon={{
            url: "assets/img/mapa/pickup.gif"
          }}
          name={'SOMA'}
          position={posicaoVeiculo} />


        <Polyline
          fillColor="#0000FF"
          fillOpacity={1}
          path={this.props.polyline}
          strokeColor="#0000FF"
          strokeOpacity={0.8}
          strokeWeight={3}
        />

      </Map>
    );
  }
}

export default GoogleApiWrapper({apiKey: 'AIzaSyCLUnXtJRRchJd7ue5aLrFO4y-PMxlXq9w'})(MapComponent)








