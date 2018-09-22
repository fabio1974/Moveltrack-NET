import React, { Component } from   'react'
import { connect } from   'react-redux'
import { reduxForm, Field, formValueSelector} from   'redux-form'
import { bindActionCreators } from 'redux'
import LabelAndInput from '../../common/form/LabelAndInput'
import formValidator from './escalaTipoValidator'
import { initialize } from   'redux-form'
import { initAction, cancelFormView } from '../commons/crudActions'
import moment from "moment";
import LabelAndSelectHora from "../../common/form/LabelAndSelectHora";
import LabelAndSelectMinuto from "../../common/form/LabelAndSelectMinuto";
class EscalaTipoForm extends Component {


    constructor(props) {
        super(props);
        initialize('objectForm', this.props.initialFormValues)
    }

    componentWillMount() {

    }

    componentDidMount() {
    }


    beforeSubmit(){
        const {horasDeTrabalho1,horasDeDescanso1,horasDeTrabalho2,horasDeDescanso2} = this.props
        let descricao = `${horasDeTrabalho1}x${horasDeDescanso1}`
        if (horasDeTrabalho2)
            descricao = descricao + `-${horasDeTrabalho2}x${horasDeDescanso2}`
        this.props.change('descricao',descricao)
    }
  

    render() {

        const {handleSubmit,readOnly} = this.props
        return (
            <form role='form' onSubmit={handleSubmit}>

                <div className='card'>
                    <div className='card-header' ><strong>Novo Tipo de Escala</strong></div>

                    <div className='card-body row'>
                        <Field name='horasDeTrabalho1' component={LabelAndInput} readOnly={readOnly} label='Horas Trabalhadas 1º. turno' cols='12 6' type='Number'  />
                        <Field name='horasDeDescanso1' component={LabelAndInput} readOnly={readOnly} label='Horas de Descanso 1º. turno' cols='12 6' type='Number' />
                        <Field name='horasDeTrabalho2' component={LabelAndInput} readOnly={readOnly} label='Horas Trabalhadas 2º. turno' cols='12 6' type='Number' />
                        <Field name='horasDeDescanso2' component={LabelAndInput} readOnly={readOnly} label='Horas de Descanso 2º. turno' cols='12 6' type='Number' />
                        <Field name='descricao' component='input' type='hidden' />

                    </div>

                    <div className='card-footer' >
                        <button type='submit' onClick={() => this.beforeSubmit()} className={`btn btn-${this.props.submitClass}`}>
                            {this.props.submitLabel}
                        </button>
                        <button type='button' className='btn btn-default' onClick={() => this.props.cancelFormView(this.props.initialFormValues)}>Cancelar</button>
                    </div>
                </div>
            </form>
        )
    }
}

EscalaTipoForm = reduxForm({ form: 'objectForm', destroyOnUnmount: false, validate: formValidator })(EscalaTipoForm)

const values = formValueSelector('objectForm')
const mapStateToProps = state => ({
    horasDeTrabalho1: values(state,'horasDeTrabalho1'),
    horasDeDescanso1: values(state,'horasDeDescanso1'),
    horasDeTrabalho2: values(state,'horasDeTrabalho2'),
    horasDeDescanso2: values(state,'horasDeDescanso2'),
}) 
const mapDispatchToProps = dispatch => bindActionCreators({ initAction, cancelFormView }, dispatch)
export default connect(mapStateToProps,mapDispatchToProps)(EscalaTipoForm)
