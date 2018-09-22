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
              <div className='card-header' ><strong>Arma Apreendida #{index + 1}</strong></div>
              <div className='card-body row' >
                <Field name={`${field}[${index}].serial`} component={LabelAndInput} readOnly={readOnly} label='Serial' cols='12 6' />
                <Field name={`${field}[${index}].registro`} component={LabelAndInput} readOnly={readOnly} label='Número do Registro' cols='12 6' />
                <Field name={`${field}[${index}].fabricante`} component={LabelAndInput} readOnly={readOnly} label='Fabricante' cols='12 6' />
                <Field name={`${field}[${index}].modelo`} component={LabelAndInput} readOnly={readOnly} label='Modelo' cols='12 6' />
                <Field name={`${field}[${index}].calibre`} component={LabelAndInput} readOnly={readOnly} label='Calibre' cols='12 6' />
              </div>
              <div className='card-footer'>
                <button title='Adicionar Arma' type='button' className='btn btn-primary btn-sm' onClick={() => this.add(index + 1)} >
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
          <button title='Adicionar Arma' type='button' className='btn btn-primary btn-sm' onClick={() => this.add(0)} >
            <i className="fa fa-plus">&nbsp;Adicionar arma apreendida</i>
          </button>
        </If>
        {this.renderRows()}
      </Grid>
    )
  }
}

const mapDispatchToProps = dispatch => bindActionCreators({ arrayInsert, arrayRemove, arrayShift }, dispatch)

export default connect(null, mapDispatchToProps)(ItemList)
