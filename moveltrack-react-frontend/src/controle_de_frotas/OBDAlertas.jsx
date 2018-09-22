import React, {Component} from 'react'
import consts from "../common/consts";
import axios from "axios";
import {sendError} from "../common/utils";
import TableArray from "../common/layout/TableArray";
import Grid from "../common/layout/Grid";
import loading_icon from "../assets/img/loading_icon.gif";


export default class OBDSensores extends Component {
  constructor(props) {
    super(props);

    this.state = {
      st500alts : [],
      map: {},
      google:{},
    }
  }



  componentDidMount() {
    const url = `${consts.API_URL}/alertas/${this.props.placa}`

    axios.get(url)
      .then(resp => {
        this.setState(prevState => ({st500alts:resp.data}))
      })
      .catch(e => {
        sendError(e)
      })
  }

  onMapReady(mapProps, map) {
    this.setState(prevState => ({map: map, google: mapProps.google}))
  }

  render() {

    if(this.state.st500alts.length==0)
      return (<div className="text-center">
        <img src={loading_icon}/>
      </div>)





    return (
        <Grid cols="12 12">
          <TableArray label="Alertas dos Sensores" list={this.state.st500alts}></TableArray>
        </Grid>
    )





  }


}


