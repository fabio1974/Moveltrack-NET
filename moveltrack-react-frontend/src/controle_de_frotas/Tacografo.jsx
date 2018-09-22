import React, {Component} from 'react'
import consts from "../common/consts";
import axios from "axios";
import {sendError} from "../common/utils"
import Grid from "../common/layout/Grid"

import {Chart} from "react-google-charts";
import loading_icon from "../assets/img/loading_icon.gif";


export default class Tacografo extends Component {
  constructor(props) {
    super(props);

    this.state = {
      data: [[]],
      options: {
        title: "Tacógrafo Virtual do Veículo no Período",
        hAxis: {title: "Tempo", viewWindow: {min: 0, max: 24}},
        vAxis: {title: "Velocidade (km/h)", viewWindow: {min: 0, max: 150}},
        legend: "none"
      }
    }
  }


  componentDidMount() {

    const url = `${consts.API_URL}/tacografo/${this.props.placa}/${this.props.inicio.valueOf()}/${this.props.fim.valueOf()}`
    axios.get(url)
      .then(resp => {

        const data1 = [
          ["Tempo", "Velocidade"]
        ]

        const data2 = resp.data.map((pt, index) => {
          return [new Date(pt.tempo), pt.velocidade]
        })

        const min = data2[0][0]
        const max = data2[data2.length - 1][0]

        const newOptions = {
          title: "Tacógrafo Virtual do Veículo no Período",
          hAxis: {title: "Tempo", viewWindow: {min, max}},
          vAxis: {title: "Velocidade (km/h)", viewWindow: {min: 0, max: 150}},
          legend: "none"
        }

        this.setState(prevState => ({options: newOptions}))
        const data = [...data1, ...data2]
        this.setState(prevState => ({data: data}))
      })
      .catch(e => {
        sendError(e)
      })
  }

  onMapReady(mapProps, map) {
    this.setState(prevState => ({map: map, google: mapProps.google}))
  }

  render() {

    if (this.state.data.length <= 1)
      return (<div className="text-center">
        <img src={loading_icon}/>
      </div>)


    return (

      <Grid cols="12 12">

        <Chart
          chartType="ScatterChart"
          data={this.state.data}
          options={this.state.options}
          width="100%"
          height="500px"
          loader={<div className="text-center"><img src={loading_icon}/></div>}
          legendToggle
        />

      </Grid>

    )


  }


}


