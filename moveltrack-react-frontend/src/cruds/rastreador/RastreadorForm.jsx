import React, { Component } from   'react'
import { connect } from   'react-redux'
import { reduxForm, Field, formValueSelector} from   'redux-form'
import { bindActionCreators } from 'redux'
import LabelAndInput from '../../common/form/LabelAndInput'
import formValidator from './rastreadorValidator'
import { initialize } from   'redux-form'
import { initAction, cancelFormView } from '../commons/crudActions'
import LabelAndSelect from "../../common/form/LabelAndSelect";
import consts from "../../common/consts";
import axios from "axios";
import {sendError} from "../../common/utils";
import SelectInputAsync from "../../common/form/SelectInputAsync";
import labelAndCheckbox from "../../common/form/LabelAndCheckbox";

class RastreadorForm extends Component {

   

    constructor(props) {
        super(props);
        this.state={
          rastreadorTipos: []
        }
        initialize('objectForm', this.props.initialFormValues)
    }

    componentWillMount() {

    }

    componentDidMount() {
      const url = `${consts.API_URL}/rastreadorTipos`
      axios.get(url).then(resp => {
        this.setState(prevState => ({
          rastreadorTipos: resp.data.map(o => (
            <option key={o} value={o}>{o}</option>
          ))
        }))
        console.log("STATE",this.state)
      }).catch(e => sendError(e))
    }


    render() {

        const {handleSubmit,readOnly} = this.props

      console.log("HANDLE SUBMIT",handleSubmit)

      return (
            <form role='form' onSubmit={handleSubmit}>

                <div className='card'>
                    <div className='card-header' ><strong>Cadastro de Rastreadores de Viaturas</strong></div>

                    <div className='card-body row' >
                        <Field name='imei' component={LabelAndInput} readOnly={readOnly} label='Imei' cols='12 3'   />
                        <Field name='rastreadorTipo' component={LabelAndSelect} readOnly={readOnly} label='Tipo de Rastreador' cols='12 3'  >
                          <option></option>
                          {this.state.rastreadorTipos}
                        </Field>
                        <Field name='chip' objectLabelProperty='iccid' path='/chips' component={SelectInputAsync} className='rs-sm' readOnly={readOnly} label='Chip'  cols='12 3'  />

                      <Field name="ativoBotaoPanico" cols='12 3' label="Possui Botao de Pânico" readOnly={readOnly} component={labelAndCheckbox}  type="checkbox"/>
                      <Field name="ativoEscuta" cols='12 3' label="Possui Escuta" readOnly={readOnly} component={labelAndCheckbox}  type="checkbox"/>
                      <Field name="ativoCercaVirtual" cols='12 3' label="Possui Cerca Virtual" readOnly={readOnly} component={labelAndCheckbox}  type="checkbox"/>
                      <Field name="ativoBloqueio" cols='12 3' label="Possui Bloqueio" readOnly={readOnly} component={labelAndCheckbox}  type="checkbox"/>

                    </div>

                    <div className='card-footer' >
                        <button type='submit'  className={`btn btn-${this.props.submitClass}`}>
                            {this.props.submitLabel}
                        </button>
                        <button type='button' className='btn btn-default' onClick={() => this.props.initAction(this.props.initialFormValues)}>Cancelar</button>
                    </div>
                </div>
            </form>
        )
    }
}

RastreadorForm = reduxForm({ form: 'objectForm', destroyOnUnmount: false , validate: formValidator})(RastreadorForm)
/*const values = formValueSelector('objectForm')
const mapStateToProps = state => ({
  serial: values(state, 'serial'),
  registro: values(state, 'registro'),
  fabricante: values(state, 'fabricante'),
  modelo: values(state, 'modelo'),
  calibre: values(state, 'calibre')
})*/
const mapDispatchToProps = dispatch => bindActionCreators({ initAction, cancelFormView }, dispatch)
export default connect(null,mapDispatchToProps)(RastreadorForm)
