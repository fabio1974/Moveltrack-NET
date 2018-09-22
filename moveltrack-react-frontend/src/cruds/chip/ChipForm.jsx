import React, { Component } from   'react'
import { connect } from   'react-redux'
import { reduxForm, Field, formValueSelector} from   'redux-form'
import { bindActionCreators } from 'redux'
import LabelAndInput from '../../common/form/LabelAndInput'
import formValidator from './chipValidator'
import { initialize } from   'redux-form'
import { initAction, cancelFormView } from '../commons/crudActions'
import LabelAndSelect from "../../common/form/LabelAndSelect";
import consts from "../../common/consts";
import axios from "axios";
import {sendError} from "../../common/utils";

class ChipForm extends Component {

   

    constructor(props) {
        super(props);
        this.state={
          operadoras: []
        }
        initialize('objectForm', this.props.initialFormValues)
    }

    componentWillMount() {

    }

    componentDidMount() {
      const url = `${consts.API_URL}/operadoras`
      axios.get(url).then(resp => {
        this.setState(prevState => ({
          operadoras: resp.data.map(o => (
            <option key={o} value={o}>{o}</option>
          ))
        }))
        console.log("STATE",this.state)
      }).catch(e => sendError(e))
    }


    render() {

        const {handleSubmit,readOnly} = this.props

      return (
            <form role='form' onSubmit={handleSubmit}>

                <div className='card'>
                    <div className='card-header' ><strong>Cadastro de chips não acauteladas para uso restrito em operações</strong></div>

                    <div className='card-body row' >
                        <Field name='numero' component={LabelAndInput} readOnly={readOnly} label='Número do Chip' cols='12 6'   />
                        <Field name='iccid' component={LabelAndInput} readOnly={readOnly} label='ICCID' cols='12 6'  />
                        <Field name='operadora' component={LabelAndSelect} readOnly={readOnly} label='Operadora' cols='12 6'  >
                          <option></option>
                          {this.state.operadoras}
                        </Field>

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

ChipForm = reduxForm({ form: 'objectForm', destroyOnUnmount: false, validate: formValidator })(ChipForm)
const values = formValueSelector('objectForm')
const mapStateToProps = state => ({
  serial: values(state, 'serial'),
  registro: values(state, 'registro'),
  fabricante: values(state, 'fabricante'),
  modelo: values(state, 'modelo'),
  calibre: values(state, 'calibre')
})
const mapDispatchToProps = dispatch => bindActionCreators({ initAction, cancelFormView }, dispatch)
export default connect(mapStateToProps,mapDispatchToProps)(ChipForm)
