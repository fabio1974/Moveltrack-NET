import React, {Component} from 'react'
import {connect} from 'react-redux'
import {reduxForm, Field, formValueSelector} from 'redux-form'
import {bindActionCreators} from 'redux'
import formValidator from './afastamentoValidator'
import {initialize} from 'redux-form'
import {initAction, cancelFormView} from '../commons/crudActions'
import DateTimer from "../../common/form/DateTimer";
import {selectSubTabAction, showSubTabsAction} from "../../common/tab/subTabActions";
import LabelAndInput from "../../common/form/LabelAndInput";
import {getAfastamentoTipo} from '../commons/tabelaApoioActions'
import LabelAndSelect from "../../common/form/LabelAndSelect";
import Pessoas from "./Pessoas";

class AfastamentoForm extends Component {

  constructor(props) {
    super(props)
    initialize('objectForm', this.props.initialFormValues)
    this.props.getAfastamentoTipo()
  }

  add(index, item = {}) {
    if (!this.props.readOnly) {
      this.props.arrayInsert('objectForm', this.props.field, index, item)
    }
  }

  componentWillMount() {

  }

  componentDidMount() {

  }

  buildAfastamentoTipo() {
    const afastamentoTipos = this.props.afastamentoTipos || []
    return afastamentoTipos.map(afastamentoTipo => (
      <option key={afastamentoTipo} value={afastamentoTipo}>{afastamentoTipo}</option>
    ))
  }

  render() {

    const {handleSubmit, readOnly} = this.props

    return (
      <form role='form' onSubmit={handleSubmit}>

        <div className='card'>
          <div className='card-header'><strong>Cadastro de afastamento</strong></div>

          <div className='card-body row'>
            <Field name='tipo' component={LabelAndSelect} readOnly={readOnly} label='Tipo do afastamento' cols='12 6'>
              <option></option>
              {this.buildAfastamentoTipo()}
            </Field>
            <Pessoas list={this.props.pessoa} field='pessoa' titulo='Pessoa' cols='12 6'
                     readOnly={readOnly} />
            <Field name='inicio' component={DateTimer} readOnly={readOnly} label='Inicio do afastamento' cols='12 6'/>
            <Field name='fim' component={DateTimer} readOnly={readOnly} label='Fim do afastamento' cols='12 6'/>
          </div>

        </div>

        <div className='card-footer'>
          <button type='submit' className={`btn btn-${this.props.submitClass}`}>
            {this.props.submitLabel}
          </button>
          <button type='button' className='btn btn-default'
                  onClick={() => this.props.initAction(this.props.initialFormValues)}>Cancelar
          </button>
        </div>
      </form>
    )

  }

}

AfastamentoForm = reduxForm({form: 'objectForm', destroyOnUnmount: false, validate: formValidator})(AfastamentoForm)
const values = formValueSelector('objectForm')
const mapStateToProps = state => ({
  afastamentoTipos: state.tabelaApoioReducer.afastamentoTipos,
  pessoa: values(state, 'pessoa'),
  inicio: values(state, 'inicio'),
  fim: values(state, 'fim')
})
const mapDispatchToProps = dispatch => bindActionCreators({
  showSubTabsAction,
  selectSubTabAction,
  initAction,
  cancelFormView,
  getAfastamentoTipo
}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(AfastamentoForm)
