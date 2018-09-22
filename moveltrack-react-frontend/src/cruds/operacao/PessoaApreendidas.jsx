import React, { Component } from 'react'
import { connect } from 'react-redux'
import { arrayInsert, arrayRemove, arrayShift, Field, formValueSelector } from 'redux-form'
import { bindActionCreators } from 'redux'
import LabelAndInput from '../../common/form/LabelAndInput'
import Grid from '../../common/layout/Grid'
import _ from 'lodash'
import If from '../../common/operator/If';

class ItemList extends Component {

  constructor(props) {
    super(props)
  }

  add(index) {
    if (!this.props.readOnly) {
      this.props.arrayInsert('objectForm', this.props.field, index, {})
    }
  }

  submitRemoveAction(index) {
    if (!this.props.readOnly && _.has(this.props, 'list.length')) {
      this.props.arrayRemove('objectForm', this.props.field, index)
    }
  }

  renderRows() {

    const { readOnly, field } = this.props

    if (_.has(this, 'props.list.length')){
        return this.props.list.map((item, index) => (
            <div className='card' key={index} >
              <div className='card-header' ><strong>Pessoa Apreendida #{index + 1}</strong></div>
              <div className='card-body row' >
                <Field name={`${field}[${index}].nome`} component={LabelAndInput} readOnly={readOnly} label='Nome' cols='12 6' />
                <Field name={`${field}[${index}].cpf`} component={LabelAndInput} readOnly={readOnly} label='CPF' cols='12 6' />
                <Field name={`${field}[${index}].rg`} component={LabelAndInput} readOnly={readOnly} label='RG' cols='12 6' />
                <Field name={`${field}[${index}].outroDoc`} component={LabelAndInput} readOnly={readOnly} label='Outro Documento' cols='12 6' />
                <Field name={`${field}[${index}].alcunha`} component={LabelAndInput} readOnly={readOnly} label='Alcunha' cols='12 6' />
                <Field name={`${field}[${index}].altura`} component={LabelAndInput} readOnly={readOnly} label='Altura (m)' cols='12 6' />
                <Field name={`${field}[${index}].corOlhos`} component={LabelAndInput} readOnly={readOnly} label='Cor dos Olhos' cols='12 6' />
                <Field name={`${field}[${index}].corPele`} component={LabelAndInput} readOnly={readOnly} label='Cor da Pele' cols='12 6' />
                <Field name={`${field}[${index}].observacoes`} component={LabelAndInput} readOnly={readOnly} label='Observacoes' cols='12 6' />

              </div>
              <div className='card-footer'>
                <button title='Adicionar Pessoa' type='button' className='btn btn-primary btn-sm' onClick={() => this.add(index + 1)} >
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
          <button title='Adicionar Pessoa Apreendida na Operação' type='button' className='btn btn-primary btn-sm' onClick={() => this.add(0)} >
            <i className="fa fa-plus">&nbsp;Adicionar pessoa apreendida</i>
          </button>
        </If>
        {this.renderRows()}
      </Grid>
    )
  }
}

const mapDispatchToProps = dispatch => bindActionCreators({ arrayInsert, arrayRemove, arrayShift }, dispatch)

export default connect(null, mapDispatchToProps)(ItemList)
