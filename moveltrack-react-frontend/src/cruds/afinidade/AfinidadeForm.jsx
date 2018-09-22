import React, { Component } from   'react'
import { connect } from   'react-redux'
import { reduxForm, Field} from   'redux-form'
import LabelAndInput from '../../common/form/LabelAndInput'
import formValidator from './afinidadeValidator'
import { initialize } from   'redux-form'

class AfinidadeForm extends Component {



  constructor(props) {
    super(props);
    initialize('objectForm', this.props.initialFormValues)
  }

  componentWillMount() {

  }

  componentDidMount() {
  }


  render() {

    const {handleSubmit,readOnly} = this.props
    return (
      <form role='form' onSubmit={handleSubmit}>

        <div className='card'>
          <div className='card-header' ><strong>Cadastro de afinidade dos agentes</strong></div>

          

          <div className='card-body row' >
            <Field name='descricao' component={LabelAndInput} readOnly={readOnly} label='Descrição' cols='12 6'   />
            <Field name='agente' component={LabelAndInput} readOnly={readOnly} label='ID do agente' cols='12 6'  />
            <Field name='pessoas' component={LabelAndInput} readOnly={readOnly} label='Afinidades do agente' cols='12 6'  />
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

ArmaForm = reduxForm({ form: 'objectForm', destroyOnUnmount: false, validate: formValidator })(AfinidadeForm)
export default connect(null,null)(AfinidadeForm)
