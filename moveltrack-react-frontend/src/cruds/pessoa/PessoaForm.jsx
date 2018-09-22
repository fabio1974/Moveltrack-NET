import React, { Component } from   'react'
import { connect } from   'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, formValueSelector } from   'redux-form'
import LabelAndInput from '../../common/form/LabelAndInput'
import LabelAndSelect from '../../common/form/LabelAndSelect'
import labelAndCheckbox from '../../common/form/LabelAndCheckbox'
import Input from '../../common/form/Input'
import formValidator from './pessoaValidator'
import {initAction, cancelFormView} from '../commons/crudActions'
import {getPerfis, getAreaOrganizacionals} from '../commons/tabelaApoioActions'
import {initialize} from 'redux-form'
import {createTextMask} from 'redux-form-input-masks'


const cpfMask = createTextMask({
  pattern: '999.999.999-99'
})

class PessoaForm extends Component {

  constructor(props) {
    super(props);
    initialize('objectForm', this.props.initialFormValues)
    this.props.getPerfis()
    this.props.getAreaOrganizacionals()
  }

  componentWillMount() {

  }

  componentDidMount() {
  }


  buildPerfisOptions() {
    const perfis = this.props.perfis || []
    return perfis.map(perfil => (
      <option key={perfil.id} value={perfil.id}>{perfil.descricao}</option>
    ))
  }

  buildAreaorganizacionalsOptions() {
    const areaOrganizacionals = this.props.areaOrganizacionals || []
    return areaOrganizacionals.map(areaOrganizacional => (
      <option key={areaOrganizacional.id}
              value={areaOrganizacional.id}>{`${areaOrganizacional.descricao} (${areaOrganizacional.sigla})`}</option>
    ))
  }


  render() {

    const {handleSubmit, readOnly} = this.props
    return (
      <form role='form' onSubmit={handleSubmit}>

        <div className='card'>
          <div className='card-header'><strong>Dados de Pessoa</strong></div>

          <div className='card-body row'>
            <Field name='nome' normalize={v => v && v.toUpperCase()} component={LabelAndInput} readOnly={readOnly}
                   label='Nome' cols='12 3' placeholder='Informe o nome'/>
            <Field name='cpf' component={LabelAndInput} readOnly={readOnly} label='CPF' cols='12 3' {...cpfMask}/>
            <Field name='matricula' component={LabelAndInput} readOnly={readOnly} label='Matrícula' cols='12 3'
                   placeholder='Informe a matrícula'/>
            <Field component={LabelAndInput} label="Email" readOnly={readOnly} name="email" cols='12 3'
                   placeholder="E-mail" icon='envelope'/>
            <Field component={LabelAndSelect} label="Área Organizacional" readOnly={readOnly}
                   name="areaOrganizacional.id" cols='12 6'>
              <option></option>
              {this.buildAreaorganizacionalsOptions()}
            </Field>


            <Field component={LabelAndSelect} label="Perfil" readOnly={readOnly} name="perfil.id" cols='12 3'>
              <option></option>
              {this.buildPerfisOptions()}
            </Field>


            <Field name="ativo" cols='12 3' label="Acesso ao Sistema" readOnly={readOnly} component={labelAndCheckbox}  type="checkbox"/>
            <Field component={Input} label="Senha " type="hidden" readOnly={readOnly} name="senha" cols='12 3'
                   placeholder="Senha" icon='lock'/>
          </div>

          <div className='card-footer'>
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

PessoaForm = reduxForm({form: 'objectForm', destroyOnUnmount: false, validate: formValidator})(PessoaForm)
const mapStateToProps = state => ({
  perfis: state.tabelaApoioReducer.perfis,
  areaOrganizacionals: state.tabelaApoioReducer.areaOrganizacionals
})
const mapDispatchToProps = dispatch => bindActionCreators({
  getPerfis,
  getAreaOrganizacionals,
  initAction,
  cancelFormView
}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(PessoaForm)
