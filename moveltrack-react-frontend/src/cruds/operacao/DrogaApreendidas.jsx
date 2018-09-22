import React, { Component } from 'react'
import { connect } from 'react-redux'
import { arrayInsert, arrayRemove, arrayShift, Field, formValueSelector } from 'redux-form'
import { bindActionCreators } from 'redux'
import LabelAndInput from '../../common/form/LabelAndInput'
import LabelAndSelect from '../../common/form/LabelAndSelect'
import Grid from '../../common/layout/Grid'
import _ from 'lodash'
import If from '../../common/operator/If';
import { getDrogaApreendidaUnidades, getDrogaApreendidaTipos } from '../../cruds/commons/tabelaApoioActions'

class ItemList extends Component {

  constructor(props) {
    super(props)
    this.props.getDrogaApreendidaUnidades()
    this.props.getDrogaApreendidaTipos()
  }

  add(index) {
    if (!this.props.readOnly) {
      this.props.arrayInsert('objectForm', this.props.field, index, {})
    }
  }

  buildDrogaApreendidaUnidade() {
    const drogaApreendidaUnidades = this.props.drogaApreendidaUnidades || []
    return drogaApreendidaUnidades.map(unidade => (
      <option key={unidade} value={unidade}>{unidade}</option>
    ))
  }

  buildDrogaApreendidaTipo() {
    const drogaApreendidaTipos = this.props.drogaApreendidaTipos || []
    return drogaApreendidaTipos.map(tipo => (
      <option key={tipo} value={tipo}>{tipo}</option>
    ))
  }


  submitRemoveAction(index) {
    if (!this.props.readOnly && _.has(this.props, 'list.length')) {
      this.props.arrayRemove('objectForm', this.props.field, index)
    }
  }

  renderRows() {

    const { readOnly, field } = this.props

    if (_.has(this, 'props.list.length')) {
      return this.props.list.map((item, index) => (
        <div className='card' key={index} >
          <div className='card-header' ><strong>Droga Apreendida #{index + 1}</strong></div>
          <div className='card-body row' >

           <Field name={`${field}[${index}].tipo`} component={LabelAndSelect} readOnly={readOnly} label='Tipo de Droga' cols='12 4'>
              <option></option>
              {this.buildDrogaApreendidaTipo()}
            </Field>

            <Field name={`${field}[${index}].quantidade`} component={LabelAndInput} readOnly={readOnly} label='Quantidade' cols='12 4' />


            <Field name={`${field}[${index}].unidade`} component={LabelAndSelect} readOnly={readOnly} label='Unidade de Medida' cols='12 4'>
              <option></option>
              {this.buildDrogaApreendidaUnidade()}
            </Field>

            <Field name={`${field}[${index}].observacoes`} component={LabelAndInput} readOnly={readOnly} label='Observacoes' cols='12 12' />

          </div>
          <div className='card-footer'>
            <button title='Adicionar Droga Apreendida' type='button' className='btn btn-primary btn-sm' onClick={() => this.add(index + 1)} >
              <i className="fa fa-plus">&nbsp;Adicionar Novo Item</i>
            </button>
            <button type='button' className='btn btn-danger btn-sm' onClick={() => this.submitRemoveAction(index)} >
              <i className="fa fa-trash">&nbsp;Remover Este Item</i>
            </button>
          </div>
        </div>
      ))
    }

  }

  render() {
    return (
      <Grid cols={this.props.cols}>
        <If test={!_.has(this, 'props.list.length') || this.props.list.length == 0}>
          <button title='Adicionar Droga Apreendida na Operação' type='button' className='btn btn-primary btn-sm' onClick={() => this.add(0)} >
            <i className="fa fa-plus">&nbsp;Adicionar droga apreendida</i>
          </button>
        </If>
        {this.renderRows()}
      </Grid>
    )
  }
}

const mapStateToProps = state => ({
  drogaApreendidaTipos: state.tabelaApoioReducer.drogaApreendidaTipos,
  drogaApreendidaUnidades: state.tabelaApoioReducer.drogaApreendidaUnidades,
})

const mapDispatchToProps = dispatch => bindActionCreators({ arrayInsert, arrayRemove, arrayShift, getDrogaApreendidaUnidades , getDrogaApreendidaTipos}, dispatch)

export default connect(mapStateToProps, mapDispatchToProps)(ItemList)
