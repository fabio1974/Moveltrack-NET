import React, {Component} from 'react'
import {connect} from 'react-redux'
import {Field, formValueSelector, initialize, reduxForm} from 'redux-form'
import {bindActionCreators} from 'redux'
import LabelAndInput from '../../common/form/LabelAndInput'
import formValidator from './escalaValidator'
import {cancelFormView, initAction, sugestEscalaTipo} from '../commons/crudActions'

import {getAreaOrganizacionals, getEscalaTipos} from '../commons/tabelaApoioActions'

import LabelAndSelect from "../../common/form/LabelAndSelect";
import LabelAndSelectAno from "../../common/form/LabelAndSelectAno";
import LabelAndSelectMes from "../../common/form/LabelAndSelectMes";
import LabelAndSelectHora from "../../common/form/LabelAndSelectHora";
import LabelAndSelectMinuto from "../../common/form/LabelAndSelectMinuto";
import consts from "../../common/consts";
import axios from "axios";
import {sendError} from "../../common/utils";
import {toastr} from "react-redux-toastr";
import _ from 'lodash'

class EscalaForm extends Component {



  constructor(props) {
    super(props);
    initialize('objectForm', this.props.initialFormValues)


}





  componentDidMount() {
    this.props.getAreaOrganizacionals()
    this.props.getEscalaTipos()
  }

  suggestEscalaEquipe(event) {
    const value = event.target.selectedOptions[0].value
    //const value = this.props.areaOrganizacionalSelectedId
    const areaOperacionalSelected = this.props.areaOrganizacionals.filter(entity=> value == entity.id)[0]
    this.props.change('escalaTipo.id',areaOperacionalSelected.escalaTipo.id)
    this.props.change('rendicaoHora',areaOperacionalSelected.rendicaoHora)
    this.props.change('rendicaoMinuto',areaOperacionalSelected.rendicaoMinuto)
  }

  beforeSubmit(){

  }


  render() {

    const areaOrganizacionals = this.props.areaOrganizacionals || []
    const escalaTipos = this.props.escalaTipos || []


    const {handleSubmit,readOnly} = this.props
    return (
      <form role='form' onSubmit={handleSubmit}>

        <div className='card'>
          <div className='card-header' ><strong>Cadastro de escalas de serviço</strong></div>

          <div className='card-body row' >
            <Field component={LabelAndSelect} label="Área Organizacional" readOnly={readOnly} onChange={(e)=> this.suggestEscalaEquipe(e)}  name='areaOrganizacional.id' cols='12 4'>
              <option></option>
              {areaOrganizacionals.map(areaOrganizacional => {
                 return (<option key={areaOrganizacional.id} value={areaOrganizacional.id}>{`${areaOrganizacional.descricao} (${areaOrganizacional.sigla})`}</option>)
                })
              }
            </Field>

            <Field name='ano' component={LabelAndSelectAno} readOnly={readOnly} label='Ano da Escala' cols='12 4'  />


            <Field name='mes' component={LabelAndSelectMes} readOnly={readOnly} label='Mês da Escala' cols='12 4'  />

            <Field component={LabelAndSelect}
                   label="Escala de Serviço" readOnly={readOnly} name="escalaTipo.id" cols='12 4' placeholder="Tipo de Escala">
              <option id={0} key={0}></option>
              {escalaTipos.map(o => {
                return (<option key={o.id} value={o.id}>{o.descricao} </option>)
                })
              }


            </Field>

            <Field name='rendicaoHora'  component={LabelAndSelectHora}   readOnly={readOnly} label='Rendição Hora' cols='12 2'  />
            <Field name='rendicaoMinuto'  component={LabelAndSelectMinuto}  readOnly={readOnly} label='Rendição Minuto' cols='12 2'  />




          </div>

          <div className='card-footer' >
                        <button type='submit' onClick={() => this.beforeSubmit()} className={`btn btn-${this.props.submitClass}`}>
                            {this.props.submitLabel}
                        </button>
                        <button type='button' className='btn btn-default' onClick={() => this.props.initAction(this.props.initialFormValues)}>Cancelar</button>
                    </div>
        </div>
      </form>
    )
  }
}



EscalaForm = reduxForm({ form: 'objectForm', destroyOnUnmount: false, validate: formValidator })(EscalaForm)
const selector = formValueSelector('objectForm')

const mapStateToProps = state => ({
  areaOrganizacionals: state.tabelaApoioReducer.areaOrganizacionals,
  escalaTipos: state.tabelaApoioReducer.escalaTipos,
  //areaOrganizacionalSelectedId: selector(state,'areaOrganizacional.id'),
})



const mapDispatchToProps = dispatch => bindActionCreators({  initAction, cancelFormView, getAreaOrganizacionals, getEscalaTipos }, dispatch)

export default connect(mapStateToProps,mapDispatchToProps)(EscalaForm)
