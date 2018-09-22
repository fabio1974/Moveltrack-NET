import React, {Component} from 'react'
import consts from "../common/consts";
import axios from "axios";
import {sendError} from "../common/utils";
import TableArray from "../common/layout/TableArray";
import Grid from "../common/layout/Grid";
import Row from "../common/layout/Row";
import TableObject from "../common/layout/TableObject";
import MapWidget from "../common/widget/MapWidget";
import moment from "moment";
import {toastr} from "react-redux-toastr";
import loading_icon from "../assets/img/loading_icon.gif";


export default class RelatorioParadas extends Component {
  constructor(props) {
    super(props);

    this.state = {
      paradas : [],
      map: {},
      google:{},
    }
  }



  componentDidMount() {

    //const veiculo = this.props.pageList.filter(veiculo=> this.state.placaRastreada===veiculo.placa)[0]

    //if(veiculo.rastreador!=null) {

      const url = `${consts.API_URL}/paradas/${this.props.placa}/${this.props.inicio.valueOf()}/${this.props.fim.valueOf()}`
      axios.get(url)
        .then(resp => {
          this.setState(prevState => ({paradas:resp.data}))
        })
        .catch(e => {
          sendError(e)
        })

    /*}else{
      this.setState(prevState => ({placaRastreada:''}))
      toastr.error('Aviso', 'Veículo não possui rastreador cadastrado!');
      this.removeMapMessage()
    }*/

  }

  onMapReady(mapProps, map) {
    this.setState(prevState => ({map: map, google: mapProps.google}))
  }

  render() {

    if(this.state.paradas.length==0)
       return (<div className="text-center">
      <img src={loading_icon}/>
    </div>)





    return (
        <Grid cols="12 12">
          <TableArray label="Paradas do Veículo No Período" list={this.state.paradas}></TableArray>
        </Grid>
    )





  }


}


