import React, {Component} from "react";
import ContentHeader from "../../common/template/contentHeader";
import Content from "../../common/template/content";
import LabelAndSelect from "../../common/form/LabelAndSelect";
//import {Field, reduxForm} from "redux-form";
import Row from "../../common/layout/Row";
import Grid from "../../common/layout/Grid";
import consts from "../../common/consts";
import axios from "axios";
import {sendError} from "../../common/utils";
import {toastr} from "react-redux-toastr";
import {initAction} from "../commons/crudActions";
import LabelAndInput from "../../common/form/LabelAndInput";
import _ from 'lodash'
import moment from 'moment'
import {pad} from '../../common/utils'

class EscalaConfig extends Component {

  constructor(props) {
    super(props);
    this.state = {

      areaOrganizacionalsEntity: [],
      areaOrganizacionalsSelectedEntity:{},

      areaOrganizacionals: [],
      areaOrganizacionalsSelected: 0,

      agentes: [],
      agentesSelected: [],

      turno1: [],
      turno1Selected: [],

      turno2: [],
      turno2Selected: [],

      turno3: [],
      turno3Selected: [],

      turno4: [],
      turno4Selected: [],
    }
  }

  componentDidMount() {
    const url = `${consts.API_URL}/areaOrganizacionals`
    axios.get(url).then(resp => {

      this.setState(prevState => ({areaOrganizacionalsEntity: resp.data.content}))

      this.setState(prevState => ({
       areaOganizacionals: resp.data.content.map(o => (
          <option key={o.id} value={o.id}>{'('+o.sigla+') '+o.descricao}</option>
        ))
      }))
    }).catch(e => sendError(e))
  }

  selectAreaOrganizacional(event) {



    const value = event.target.selectedOptions[0].value



    this.setState(prevState => ({areaOrganizacionalsSelected: value}))

    const entity = this.state.areaOrganizacionalsEntity.filter(entity=> value == entity.id)

    console.log("AreaSelected",entity)

    this.setState(prevState => ({areaOrganizacionalsSelectedEntity: entity[0]}))




    const url2 = `${consts.API_URL}/escalaEquipes?search=areaOrganizacional:${value}`
    axios.get(url2).then(resp => {
      const agentesTurno1 = []
      const agentesTurno2 = []
      const agentesTurno3 = []
      const agentesTurno4 = []
      resp.data.content.forEach(escalaEquipe =>{
        if(escalaEquipe.sigla === 'TURNO1'){
          agentesTurno1.push(escalaEquipe.pessoa)
        }else if(escalaEquipe.sigla === 'TURNO2'){
          agentesTurno2.push(escalaEquipe.pessoa)
        }else if(escalaEquipe.sigla === 'TURNO3'){
          agentesTurno3.push(escalaEquipe.pessoa)
        }else if(escalaEquipe.sigla === 'TURNO4'){
          agentesTurno4.push(escalaEquipe.pessoa)
        }
      })
      this.setState(prevState=>({turno1: this.agentesToOptions(agentesTurno1)}))
      this.setState(prevState=>({turno2: this.agentesToOptions(agentesTurno2)}))
      this.setState(prevState=>({turno3: this.agentesToOptions(agentesTurno3)}))
      this.setState(prevState=>({turno4: this.agentesToOptions(agentesTurno4)}))

    }).catch(e => sendError(e))
      .then(()=>{
        const url = `${consts.API_URL}/pessoas?search=areaOrganizacional:${value}`
        axios.get(url).then(resp => {

          const agenteOptions = this.agentesToOptions(resp.data.content);


          const turnoOptions = this.state.turno1.concat(this.state.turno2).concat(this.state.turno3).concat(this.state.turno4)


          const sobra =  agenteOptions.filter(option=>!this.isOptionInOptions(option,turnoOptions))



          this.setState(prevState=>({agentes: sobra}))

        }).catch(e => sendError(e))
      })


  }

  selectAgentes(event) {

    const targetOptions = event.target.selectedOptions
    const reactOptions = Array.from(targetOptions).map(o => (<option value={o.value} key={o.value}>{o.text}</option>))
    this.setState(prevState=>({agentesSelected: reactOptions}))


  }

  addToTurno(turno) {
    let sobra = this.state.agentes.filter(option => !this.isOptionInOptions(option, this.state.agentesSelected))
    this.setState(prevState => ({agentes: sobra, [turno]: this.state[turno].concat(this.state.agentesSelected)}))
    this.setState(prevState => ({agentesSelected:[]}))
  }

  selectTurno(turnoSelected, event) {
    const targetOptions = event.target.selectedOptions
    const reactOptions = Array.from(targetOptions).map(o => (<option value={o.value} key={o.value}>{o.text}</option>))
    this.setState(prevState => ({[turnoSelected]: reactOptions}))
  }

  removeFromTurno(turnoSelecteds, turno) {
    let sobra = this.state[turno].filter(option => !this.isOptionInOptions(option, turnoSelecteds))
    this.setState(prevState=>({agentes: this.state.agentes.concat(turnoSelecteds), [turno]: sobra}))
    this.setState(prevState => ({[`${turno}Selected`]:[]}))
  }


  agentesToOptions(agentes) {
    return agentes.map(agente => (
      <option value={agente.id} key={agente.id}>
        {"  "} {agente.nome}{" - Matricula: "} {agente.matricula}
      </option>))
  }

  isOptionInOptions(option, options) {
    for (let i = 0, l = options.length; i < l; i++) {
      if (option.props.value == options[i].props.value)
        return true;
    }
    return false
  }



  render() {

    console.log("ESTADO DA APLICAAO",this.state)

    return (
      <div>
        <ContentHeader path={"/escalaConfig"} secao={"Escalas de Serviço"} titulo={"Planejamento de Escala"}/>
        <Content className="bg-blue">
          <div className="card">
       <div className="card-body">
          <form >
            <Row>
              <Grid cols="12 6">
                <LabelAndSelect name="areaOrganizacional"  placeholder="title" className="form-group"
                       label="Unidade Operacional"
                       value={this.state.areaOrganizacionalsSelected}
                       onChange={(e) => this.selectAreaOrganizacional(e)}>
                  >
                  <option key={0}>Escolha a Unidade Operacional</option>
                  {this.state.areaOganizacionals}
                </LabelAndSelect>
              </Grid>

              <Grid cols="12 3">
                <LabelAndInput label="Escala de Serviço" readOnly
                               value={_.has(this.state.areaOrganizacionalsSelectedEntity,"escalaTipo.descricao")?this.state.areaOrganizacionalsSelectedEntity.escalaTipo.descricao:''}
                />
              </Grid>

              <Grid cols="12 2">
                <LabelAndInput label="Rendição" readOnly
                               value={_.has(this.state.areaOrganizacionalsSelectedEntity,"escalaTipo")?
                                 (`${pad(2,''+this.state.areaOrganizacionalsSelectedEntity.rendicaoHora)}:${pad(2,''+this.state.areaOrganizacionalsSelectedEntity.rendicaoMinuto)}`):''} />
              </Grid>




            </Row>

            <Row>
              <Grid cols="12 4">
                <LabelAndSelect name="agentes" placeholder="title" className="form-group" size="10" multiple
                                       label="Agentes da Unidade Não Distribuídos"
                       value={this.state.agentesSelected}
                       onChange={(e) => this.selectAgentes(e)}

                >
                  {this.state.agentes}
                </LabelAndSelect>
              </Grid>

              <Grid cols="12 2">
                <div className="form-group height-24"></div>
                <button name="" onClick={() => this.addToTurno("turno1")} type="button"
                        className="btn btn-sm btn-secondary form-group form-control">Adicionar p/ Eq. ALPHA
                </button>
                <button name="" onClick={() => this.addToTurno("turno2")} type="button"
                        className="btn btn-sm btn-secondary form-group form-control" type="button">Adicionar p/ Eq. BRAVO
                </button>
                <button name="" onClick={() => this.addToTurno("turno3")} className="btn btn-sm btn-secondary form-group form-control"
                        type="button">Adicionar p/ Eq. CHARLIE
                </button>
                <button name="" onClick={() => this.addToTurno("turno4")} className="btn btn-sm btn-secondary form-group form-control"
                        type="button">Adicionar p/ Eq. DELTA
                </button>
              </Grid>
            </Row>

            <Row>
              <Grid cols="12 3">
                <LabelAndSelect name="turno1"  placeholder="title" label="Equipe ALPHA"
                       size={10}
                       value={this.state.turno1Selected}
                       onChange={(event) => this.selectTurno("turno1Selected", event)}
                       multiple>
                  {this.state.turno1}
                </LabelAndSelect>
              </Grid>

              <Grid cols="12 3">
                <LabelAndSelect name="turno2"  placeholder="title" label="Equipe BRAVO"
                       size={10}
                       value={this.state.turno2Selected}
                       onChange={(event) => this.selectTurno("turno2Selected", event)}
                       multiple>
                  {this.state.turno2}
                </LabelAndSelect>
              </Grid>

              <Grid cols="12 3">
                <LabelAndSelect name="turno3" placeholder="title" label="Equipe CHARLIE"
                       size={10}
                       value={this.state.turno3Selected}
                       onChange={(event) => this.selectTurno("turno3Selected", event)}
                       multiple>
                  {this.state.turno3}
                </LabelAndSelect>
              </Grid>

              <Grid cols="12 3">
                <LabelAndSelect name="turno4"  placeholder="title" label="Equipe DELTA"
                       size={10}
                       value={this.state.turno4Selected}
                       onChange={(event) => this.selectTurno("turno4Selected", event)}
                       multiple>
                  {this.state.turno4}
                </LabelAndSelect>
              </Grid>
            </Row>

            <Row className="form-group">
              <Grid cols="12 3">
                <button name="" onClick={() => this.removeFromTurno(this.state.turno1Selected, "turno1")} type="button"
                        className="btn btn-sm btn-secondary form-control">Remover Selecionado
                </button>
              </Grid>

              <Grid cols="12 3">
                <button name="" onClick={() => this.removeFromTurno(this.state.turno2Selected, "turno2")} type="button"
                        className="btn btn-sm btn-secondary  form-control">Remover Selecionado
                </button>
              </Grid>

              <Grid cols="12 3">
                <button name="" onClick={() => this.removeFromTurno(this.state.turno3Selected, "turno3")} type="button"
                        className="btn btn-sm btn-secondary  form-control">Remover Selecionado
                </button>
              </Grid>

              <Grid cols="12 3 ">
                <button name="" onClick={() => this.removeFromTurno(this.state.turno4Selected, "turno4")} type="button"
                        className="btn btn-sm btn-secondary form-control">Remover Selecionado
                </button>
              </Grid>
            </Row>

            <Row className="form-group justify-content-end">
              <Grid cols="12 3 ">
              <button type="button" onClick={()=>this.handleSubmit()}  className="btn btn-md btn-primary form-control">Salvar Configurações
              </button>
              </Grid>
            </Row>

          </form>
       </div>
          </div>
        </Content>
      </div>
    );
  }


  handleSubmit() {
    const escalaEquipes = []
    for(let i=1;i<=4;i++) {
      this.state[`turno${i}`].forEach(option => {
        escalaEquipes.push({
          "pessoa": {id: option.props.value},
          "areaOrganizacional": {id: this.state.areaOrganizacionalsSelected},
          "sigla": `TURNO${i}`
        });
      })
    }

    const url = `${consts.API_URL}/escalaEquipes`
    axios['post'](url, escalaEquipes)
      .then(() => {
        toastr.success('Sucesso', 'Operação Realizada com sucesso.');
      })
      .catch(e => {
        sendError(e)
      })
  }
}

export default EscalaConfig


