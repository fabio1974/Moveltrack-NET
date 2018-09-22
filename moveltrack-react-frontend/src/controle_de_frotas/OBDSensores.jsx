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
import loading_icon from "../assets/img/loading_icon.gif";


export default class OBDSensores extends Component {
  constructor(props) {
    super(props);

    this.state = {
      st500pid : {},
      pids:[],
      map: {},
      google:{},
    }
  }



  componentDidMount() {
    const url = `${consts.API_URL}/sensores/${this.props.placa}`

    axios.get(url)
      .then(resp => {
        this.setState(prevState => ({pids:resp.data.pids}))
        const deviceDate = moment(resp.data.deviceDate).format('DD/MM/YYYY HH:mm:ss')
        const storeDate = moment(resp.data.storeDate).format('DD/MM/YYYY HH:mm:ss')
        const clone = {...resp.data,deviceDate,storeDate}
        delete clone.pids
        this.setState(prevState => ({st500pid:clone}))
      })
      .catch(e => {
        sendError(e)
      })
  }

  onMapReady(mapProps, map) {
    this.setState(prevState => ({map: map, google: mapProps.google}))
  }

  render() {

    if(this.state.pids.length==0)
      return (<div className="text-center">
        <img src={loading_icon}/>
      </div>)

    const clone = {...this.state.st500pid}



    return (
      <Row>
        <Grid cols="12 4" className="maxHeight maxWidth padding-0">



          <TableObject label="Momento/Local da Captura" object={this.state.st500pid}></TableObject>
          <label htmlFor="mapa">Localização da Captura</label>
          <MapWidget id="mapa" onMapReady={(a,b)=>this.onMapReady(a,b)}
                         style={{width:  '100%',height: '150px'}}
                         zoom={18}
                         center={{lat:this.state.st500pid.latitude,lng: this.state.st500pid.longitude}}/>


        </Grid>

        <Grid cols="12 8">
          <TableArray label="Dados dos Sensores" list={this.state.pids}></TableArray>


        </Grid>
      </Row>

    )





  }


}


