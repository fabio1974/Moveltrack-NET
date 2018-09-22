import React, { Component } from   'react'
import { connect } from   'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, formValueSelector } from   'redux-form'
import LabelAndInput from '../../common/form/LabelAndInput'
import LabelAndSelect from '../../common/form/LabelAndSelect'
import formValidator from './areaOrganizacionalValidator'
import { initAction } from '../commons/crudActions'
import { getEscalaTipos } from '../commons/tabelaApoioActions'
import { initialize } from   'redux-form'
import LabelAndSelectHora from "../../common/form/LabelAndSelectHora";
import LabelAndSelectMinuto from "../../common/form/LabelAndSelectMinuto";

class AreaOrganizacionalForm extends Component {

    constructor(props) {
        super(props);
        initialize('objectForm', this.props.initialFormValues)
        this.props.getEscalaTipos()
    }

    componentWillMount() {

    }

    componentDidMount() {
    }


    buildEscalaTiposOptions() {
        const escalaTipos = this.props.escalaTipos || []
        return escalaTipos.map(escalaTipo => (
                <option key={escalaTipo.id} value={escalaTipo.id}>{escalaTipo.descricao}</option>
            )
        )
    }

    render() {

        const { handleSubmit, readOnly } = this.props
        return (
            <form role='form' onSubmit={handleSubmit}>

                <div className='card'>
                    <div className='card-header' ><strong>Dados de AreaOrganizacional</strong></div>

                    <div className='card-body row' >
                        <Field name='descricao' normalize={v => v && v.toUpperCase()} component={LabelAndInput} readOnly={readOnly} label='Descrição da Área Organizacional' cols='12 6' placeholder='Informe a descrição da Àrea' />
                        <Field name='sigla' normalize={v => v && v.toUpperCase()} component={LabelAndInput} readOnly={readOnly} label='Sigla' cols='12 3' placeholder='Informe a Sigla descritiva da Área' />

                        <Field component={LabelAndSelect}  label="Tipo" readOnly={readOnly} name="tipo" cols='12 3' placeholder="Tipo">
                            <option></option>
                            <option value='ADMINISTRATIVA'>ADMINISTRATIVA</option>
                            <option value='OPERACIONAL'>OPERACIONAL</option>
                        </Field>


                        <Field component={LabelAndSelect}  label="Escala de Serviço" readOnly={readOnly} name="escalaTipo.id" cols='12 2' placeholder="Tipo de Escala">
                            <option></option>
                            {this.buildEscalaTiposOptions()}
                        </Field>

                      <Field name='rendicaoHora'  component={LabelAndSelectHora}   readOnly={readOnly} label='Rendição Hora' cols='12 2'  />
                      <Field name='rendicaoMinuto'  component={LabelAndSelectMinuto}  readOnly={readOnly} label='Rendição Minuto' cols='12 2'  />

                    </div>

                    <div className='card-footer' >
                        <button type='submit' className={`btn btn-${this.props.submitClass}`}>
                            {this.props.submitLabel}
                        </button>
                        <button type='button' className='btn btn-default' onClick={() => this.props.initAction(this.props.initialFormValues)}>Cancelar</button>
                    </div>
                </div>
            </form>
        )
    }
}

AreaOrganizacionalForm = reduxForm({ form: 'objectForm', destroyOnUnmount: false, validate: formValidator })(AreaOrganizacionalForm)
const mapStateToProps = state => ({ escalaTipos: state.tabelaApoioReducer.escalaTipos })
const mapDispatchToProps = dispatch => bindActionCreators({ getEscalaTipos, initAction }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(AreaOrganizacionalForm)
