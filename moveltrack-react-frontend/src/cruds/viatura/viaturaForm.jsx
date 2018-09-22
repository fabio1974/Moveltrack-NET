import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import {reduxForm, Field, formValueSelector, initialize} from 'redux-form'
import LabelAndInput from '../../common/form/LabelAndInput'
import {initAction, cancelFormView} from '../commons/crudActions'
import formValidator from './ViaturaValidator'


class ViaturaForm extends Component {

  constructor(props) {
    super(props);
    initialize('objectForm', this.props.initialFormValues)
  }

  componentWillMount() {

  }


  render() {

    const {handleSubmit, readOnly} = this.props
    return (

      <form role='form' onSubmit={handleSubmit}>
        <div className='card'>
          <div className='card-header'>Dados da Viatura</div>

          <div className='card-body row'>
            <Field name='placa' component={LabelAndInput} readOnly={readOnly} label='Placa' cols='12 3'
                   placeholder='Informe a placa'/>
            <Field name='marcaModelo' component={LabelAndInput} readOnly={readOnly} label='Marca/Modelo' cols='12 3'
                   placeholder='Informe Marca/Modelo'/>
            <Field name='chassi' component={LabelAndInput} readOnly={readOnly} label='Chassi' cols='12 3'
                   placeholder='Informe o chassi'/>
            <Field name='cor' component={LabelAndInput} readOnly={readOnly} label='Cor' cols='12 3'
                   placeholder='Informe a cor'/>
          </div>

          <div className='card-footer' >
            <button type='submit' className={`btn btn-${this.props.submitClass}`}>
              {this.props.submitLabel}
            </button>
            <button type='button' className='btn btn-default'
                    onClick={() => this.props.cancelFormView(this.props.initialFormValues)}>Cancelar
            </button>
          </div>
        </div>
      </form>
    )
  }
}

ViaturaForm = reduxForm({form: 'objectForm', destroyOnUnmount: false, validate: formValidator})(ViaturaForm)
const values = formValueSelector('objectForm')
const mapStateToProps = state => ({
  placa: values(state, 'placa'),
  marcaModelo: values(state, 'marcaModelo'),
  chassi: values(state, 'chassi'),
  cor: values(state, 'cor')
})
const mapDispatchToProps = dispatch => bindActionCreators({ initAction, cancelFormView }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(ViaturaForm)
