import React, { Component } from   'react'
import { connect } from   'react-redux'
import { reduxForm, Field, formValueSelector} from   'redux-form'
import { bindActionCreators } from 'redux'
import LabelAndInput from '../../common/form/LabelAndInput'
import formValidator from './armaValidator'
import { initialize } from   'redux-form'
import { initAction, cancelFormView } from '../commons/crudActions'

class ArmaForm extends Component {

   

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
                    <div className='card-header' ><strong>Cadastro de armas não acauteladas para uso restrito em operações</strong></div>

                    <div className='card-body row' >
                        <Field name='serial' component={LabelAndInput} readOnly={readOnly} label='Serial' cols='12 6'   />
                        <Field name='registro' component={LabelAndInput} readOnly={readOnly} label='Número do Registro' cols='12 6'  />
                        <Field name='fabricante' component={LabelAndInput} readOnly={readOnly} label='Fabricante' cols='12 6'  />
                        <Field name='modelo' component={LabelAndInput} readOnly={readOnly} label='Modelo' cols='12 6'  />
                        <Field name='calibre' component={LabelAndInput} readOnly={readOnly} label='Calibre' cols='12 6'  />
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

ArmaForm = reduxForm({ form: 'objectForm', destroyOnUnmount: false, validate: formValidator })(ArmaForm)
const values = formValueSelector('objectForm')
const mapStateToProps = state => ({
  serial: values(state, 'serial'),
  registro: values(state, 'registro'),
  fabricante: values(state, 'fabricante'),
  modelo: values(state, 'modelo'),
  calibre: values(state, 'calibre')
})
const mapDispatchToProps = dispatch => bindActionCreators({ initAction, cancelFormView }, dispatch)
export default connect(mapStateToProps,mapDispatchToProps)(ArmaForm)
