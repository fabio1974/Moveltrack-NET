import React, {Component} from 'react'
import {GoogleApiWrapper, Map, Marker} from 'google-maps-react';


export class MapWidget extends Component {

  constructor(props) {
    super(props);
  }



  componentDidMount() {



  }




  render() {




    return (
      <Map onReady={this.props.onMapReady} google={this.props.google} zoom={this.props.zoom} initialCenter={this.props.center} style={this.props.style}>


        <Marker
          title={'Última posição do Veículo.'}
          visible={true}
          icon={{
            url: "assets/img/mapa/pickup.gif"
          }}
          name={'SOMA'}
           />





      </Map>
    );
  }
}

export default GoogleApiWrapper({apiKey: 'AIzaSyCLUnXtJRRchJd7ue5aLrFO4y-PMxlXq9w'})(MapWidget)








