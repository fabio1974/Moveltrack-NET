import React, {Component} from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import {withRouter} from 'react-router-dom'
import {withLastLocation} from 'react-router-last-location';
import Content from "../common/template/content"
import MapComponent from "./MapComponent";
import Grid from "../common/layout/Grid";
import {minimizeSideBar} from "../main/configActions";
import DateTimer from "../common/form/DateTimer";
import moment from "moment";
import consts from "../common/consts";
import axios from "axios";
import {buildQueryFromFilter, sendError} from "../common/utils";
import Paginador from "./Paginador";
import ListViatura from "./ListViatura";
import {toastr} from "react-redux-toastr";
import Modal from 'react-responsive-modal';
import OBDSensores from "./OBDSensores";
import MapComponent2 from "../common/widget/MapWidget";
import OBDAlertas from "./OBDAlertas";
import RelatorioParadas from "./RelatorioParadas";
import RelatorioPercurso from "./RelatorioPercurso";
import Tacografo from "./Tacografo";

class DashboardRastrementoViatura extends Component {


  constructor(props) {
    super(props)

    //this.myRef.current

    this.executeFilter = this.executeFilter.bind(this)
    this.rastrearViatura = this.rastrearViatura.bind(this)
    this.tick = this.tick.bind(this)



    this.props.minimizeSideBar(true)

    this.state = {
      inicio: moment(moment().format("DD/MM/YYYY 00:00:00"), 'DD/MM/YYYY HH:mm:ss'),
      fim: moment(moment().format("DD/MM/YYYY 23:59:59"), 'DD/MM/YYYY HH:mm:ss'),
      placaRastreada: '',
      timer: null,
      historico: false,
      mapMessage:'Escolha o veículo para rastrear!',
      //mapa
      map: {},
      google:{},
      polyline: [],
      markers:[],

      //sensores e alerta do ODB modals
      sensoresOpened: false,
      alertasOpened: false,
      paradasOpened: false,
      percursoOpened: false,
      tacografoOpened: false,

      //lista
      objectName: 'viatura',
      path: '/viaturas',
      object: {
        placa: {type: String, descryption: 'Placa', show: true, filter: true},
        marcaModelo: {type: String, descryption: 'Marca/Modelo', show: true, filter: true},
        chassi: {type: String, descryption: 'Chassi', show: false, filter: false},
        cor: {type: String, descryption: 'Cor', show: false, filter: false}
      },
      filter: {},
      pageList: [],
      pageSize: 5,
      currentPage: 0,
      pageToGo: 0,
      lastPage: 0,
      count: 0
    }
  }

  componentDidMount() {
    this.updatePage()



  }

  componentWillUnmount() {
    clearInterval(this.state.timer);
    this.props.minimizeSideBar(false)
  }



  onMapReady(mapProps, map) {
    this.setState(prevState => ({map: map, google: mapProps.google}), () => this.drawMapElements())
  }

  drawMapElements(){

    /*const position1 = {lat: -3.802774,lng: -38.536376}
    const position2 = {lat: -3.812774,lng: -38.536376}
    const icon = ''
    this.addMarker(position1,icon,1)
    this.addMarker(position2,icon,2)*/

  }

  addMarker(position,icon,key){
    const{map,google} = this.state
    let marker = new google.maps.Marker({
      position: position ,
      map: map,
      key: key,
    })
    this.state.markers.push(marker)

    //this.setState(prevState => ({markers:this.state.markers.push(marker)}),()=> console.log("MARKERS",this.state.markers))
  }


  removeMarkers() {
    this.setState(prevState => ({markers:this.state.markers.map(marker=>{marker.setMap(null)})}))
    this.setState(prevState => ({markers:[]}))
  }







  tick() {
    this.removeMarkers()

    if (!this.state.historico && this.state.placaRastreada.length > 1) {
      this.rastrearViaturaPorPlaca(this.state.placaRastreada)
    }
  }


  updatePage() {
    const {path, filter, pageSize, currentPage} = this.state
    const query = buildQueryFromFilter(filter)
    const url = `${consts.API_URL}${path}?page=${currentPage}&size=${pageSize}${query}`

    axios.get(url)
      .then(resp => {
        const count = resp.data.totalElements
        const lastPage = resp.data.totalPages - 1
        const list = resp.data.content
        if (lastPage < currentPage)
          this.setState(prevState => ({currentPage: 0}))
        this.setState(prevState => ({pageList: list, lastPage: lastPage, count: count}))
      })
      .catch(e => {
        sendError(e)
      })
  }

  goFirst() {
    const {currentPage, filter} = this.state
    this.setState(prevState => ({currentPage: 0}), () => this.updatePage())
  }

  goNext() {
    const {currentPage, lastPage} = this.state
    if (currentPage == lastPage) return
    this.setState(prevState => ({currentPage: currentPage + 1}), () => this.updatePage())

  }

  goPrevious() {
    const {currentPage} = this.state
    if (currentPage == 0) return
    this.setState(prevState => ({currentPage: currentPage - 1}), () => this.updatePage())
  }

  goLast() {
    const {lastPage, currentPage} = this.state
    if (currentPage == lastPage) return
    this.setState(prevState => ({currentPage: lastPage}), () => this.updatePage())
  }

  executeFilter(field, e) {
    const {filter} = this.state
    const clone = {...filter, [field]: e.target.value}
    this.setState(prevState => ({filter: clone}), () => this.goFirst())
  }

  goToPage(e) {
    const {lastPage} = this.state
    const newPage = e.target.value - 1
    if (newPage < 0) {
      this.goFirst()
      e.target.value = ''
      return
    } else if (newPage > lastPage) {
      e.target.value = ''
      this.goLast(lastPage)
      return
    }
    this.setState(prevState => ({currentPage: newPage}), () => this.updatePage())
  }

  changePageSize(e) {
    const pageSize = e.target.value
    this.setState(prevState => ({pageSize: pageSize}), () => this.goFirst())
  }


  rastrearViatura(e) {
    const placa = e.target.value
    this.setState(prevState => ({placaRastreada: placa}))
    this.rastrearViaturaPorPlaca(placa)
  }

  startTimer(){
    this.stopTimer()
    const timer = setInterval(this.tick, 10000);
    this.setState(prevState => ({timer: timer}));
  }

  stopTimer(){
    clearInterval(this.state.timer);
  }

  rastrearViaturaPorPlaca(placa) {

    const veiculo = this.state.pageList.filter(veiculo=> placa===veiculo.placa)[0]



    if(veiculo.rastreador!=null) {

      const url = `${consts.API_URL}/locations/${placa}/${this.state.inicio.valueOf()}/${this.state.fim.valueOf()}`
      axios.get(url)
        .then(resp => {

          if (resp.data.length > 0) {

            const polyline = resp.data.map(loc => {
              return {
                lat: loc.latitude,
                lng: loc.longitude
              }
            })

            this.setState(prevState => ({polyline: []}))
            this.setState(prevState => ({polyline: polyline}))

            if(!this.state.historico)
              this.startTimer();
            this.updateMapMessage()

          } else {

            this.setState(prevState => ({polyline: []}))
            this.setState(prevState => ({placaRastreada:''}))
            toastr.warning('Aviso', 'Veículo sem rastreamento no período! Tente novamente');
            this.removeMapMessage()
            this.stopTimer();

          }

        })
        .catch(e => {
          this.setState(prevState => ({polyline: []}))
          this.setState(prevState => ({placaRastreada:''}))
          toastr.warning('Erro', 'Veículo sem rastreamento no período! tente novamente');
          this.removeMapMessage()
          this.stopTimer();

        })
    }else{
      this.setState(prevState => ({polyline: []}))
      this.setState(prevState => ({placaRastreada:''}))
      toastr.error('Aviso', 'Veículo não possui rastreador cadastrado!');
      this.removeMapMessage()
      this.stopTimer();
    }

  }


  onOpenSensores() {
    this.setState({sensoresOpened: true})
  }

  onCloseSensores() {
    this.setState({sensoresOpened: false})
  }


  onOpenAlertas() {
    this.setState({alertasOpened: true})
  }

  onCloseAlertas() {
    this.setState({alertasOpened: false})
  }

  onOpenParadas() {
    this.setState({paradasOpened: true})
  }

  onCloseParadas() {
    this.setState({paradasOpened: false})
  }

  onOpenPercurso() {
    this.setState({percursoOpened: true})
  }

  onClosePercurso() {
    this.setState({percursoOpened: false})
  }

  onOpenTacografo() {
    this.setState({tacografoOpened: true})
  }

  onCloseTacografo() {
    this.setState({tacografoOpened: false})
  }



  onChangeInicio(e){
    if(moment(e._d).isAfter(moment(this.state.fim)))
      toastr.error('Aviso', 'A data de início deve ser anterior à data final!');
    else
      this.setState(prevState => ({inicio: e._d}))
  }

  onChangeFim(e){

    if(moment(this.state.inicio).isAfter(moment(e._d)))
      toastr.error('Aviso', 'A data final deve ser maior que a data inicial!');
    else {
      this.setState(prevState => ({fim: e._d}))
      this.setState(prevState => ({historico: moment(e._d).isBefore(moment())}),()=>{
        if(this.state.historico)
          this.stopTimer()
        else
          this.startTimer()
        this.updateMapMessage()
      })
    }
  }


  render() {


    return (

      <Content className="maxHeight bg-white margin-0 padding-0 padding-l-15  padding-r-0 ">

        <div className="row maxHeight maxWidth padding-0 ">

          <Grid cols="12 2" className="bg-white padding-h-10 padding-v-10 ">

            <ListViatura state={this.state} executeFilter={this.executeFilter} rastrearViatura={this.rastrearViatura}/>


            <div className="h-divider"/>


            <Paginador goFirst1={() => this.goFirst()}
                       goPrevious={() => this.goPrevious()}
                       goNext={() => this.goNext()}
                       goLast={() => this.goLast()}
                       goToPage={() => this.goToPage()}
                       changePageSize={() => this.changePageSize()} hideB hideC state={this.state}
            />

            <DateTimer name='inicio' className='rdt-sm' value={this.state.inicio}
                       onChange={(e) => this.onChangeInicio(e)} label='Início do Período'  cols='12 12'/>
            <DateTimer name='fim' className='rdt-sm margin-bottom-10' value={this.state.fim}
                       onChange={(e) => this.onChangeFim(e)}    label='Fim do Período' cols='12 12'/>


            <div className="h-divider"/>




            <button disabled={this.state.placaRastreada===''} className="btn btn-primary btn-sm form-control padding-h-0 margin-0 margin-bottom-10 " onClick={()=> this.onOpenSensores()}>Sensores do OBD</button>
            <Modal  classNames={{ overlay: 'custom-overlay', modal: 'custom-modal' }}
                    open={this.state.sensoresOpened}
                    onClose={()=>this.onCloseSensores()}
                    closeOnOverlayClick={false} center>

              <OBDSensores placa={this.state.placaRastreada}></OBDSensores>
            </Modal>


            <button disabled={this.state.placaRastreada===''} className="btn btn-primary btn-sm form-control padding-h-0 margin-0  margin-bottom-10 " onClick={()=> this.onOpenAlertas()}>Alertas do OBD</button>
            <Modal classNames={{ overlay: 'custom-overlay', modal: 'custom-modal' }}
                   open={this.state.alertasOpened}
                   onClose={()=>this.onCloseAlertas()}
                   closeOnOverlayClick={false} center >
              <OBDAlertas placa={this.state.placaRastreada}></OBDAlertas>
            </Modal>


            <button disabled={this.state.placaRastreada===''} className="btn btn-primary btn-sm form-control padding-h-0 margin-0  margin-bottom-10 " onClick={()=> this.onOpenParadas()}>Relatório de Paradas</button>
            <Modal classNames={{ overlay:  {zIndex: 10}, modal: 'custom-modal' }}
                   open={this.state.paradasOpened}
                   onClose={()=>this.onCloseParadas()}
                   closeOnOverlayClick={false} center >
              <RelatorioParadas placa={this.state.placaRastreada} inicio={this.state.inicio} fim={this.state.fim} ></RelatorioParadas>
            </Modal>


            <button disabled={this.state.placaRastreada===''} className="btn btn-primary btn-sm form-control padding-h-0 margin-0  margin-bottom-10 " onClick={()=> this.onOpenPercurso()}>Rel. Analítico de Percurso</button>
            <Modal classNames={{ overlay: 'custom-overlay', modal: 'custom-modal' }}
                   open={this.state.percursoOpened}
                   onClose={()=>this.onClosePercurso()}
                   closeOnOverlayClick={false} center >
              <RelatorioPercurso placa={this.state.placaRastreada} inicio={this.state.inicio} fim={this.state.fim} ></RelatorioPercurso>
            </Modal>


            <button disabled={this.state.placaRastreada===''} className="btn btn-primary btn-sm form-control padding-h-0 margin-0  margin-bottom-10 " onClick={()=> this.onOpenTacografo()}>Tacógrafo Virtual</button>
            <Modal classNames={{ overlay: 'custom-overlay', modal: 'custom-modal' }}
                   open={this.state.tacografoOpened}
                   onClose={()=>this.onCloseTacografo()}
                   closeOnOverlayClick={false} center >
              <Tacografo placa={this.state.placaRastreada} inicio={this.state.inicio} fim={this.state.fim} ></Tacografo>

            </Modal>



          </Grid>

          <Grid cols="12 10" className="maxHeight maxWidth padding-0">
            <div id="floating-panel">{this.state.mapMessage}</div>
            <MapComponent onMapReady={(a,b)=>this.onMapReady(a,b)} polyline={this.state.polyline}/>
          </Grid>


        </div>


      </Content>
    )
  }

  updateMapMessage(){
    const{historico,placaRastreada} = this.state
    let mapMessage = 'Escolha o veículo para rastrear!'
    if(placaRastreada.length>1) {
      if (historico)
        mapMessage = "Histórico do Veículo " + placaRastreada
      else
        mapMessage = "Ratstreando Veículo " + placaRastreada
    }/*else
       mapMessage = "Nenhum veículo rastreando no momento"*/
    this.setState(prevState => ({mapMessage:mapMessage}))
  }

  removeMapMessage(){
    this.setState(prevState => ({mapMessage:'Escolha o veículo para rastrear!'}))
  }

}

const mapStateToProps = state => ({})
const mapDispatchToProps = dispatch => bindActionCreators({minimizeSideBar}, dispatch)

export default withLastLocation(withRouter(connect(mapStateToProps, mapDispatchToProps)(DashboardRastrementoViatura)))
