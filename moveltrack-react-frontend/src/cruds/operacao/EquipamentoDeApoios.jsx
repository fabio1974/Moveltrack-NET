import React, { Component } from 'react'
import { connect } from 'react-redux'
import { arrayInsert, arrayRemove, Field, formValueSelector } from 'redux-form'
import { bindActionCreators } from 'redux'
import LabelAndInput from '../../common/form/LabelAndInput'
import Grid from '../../common/layout/Grid'
import Row from '../../common/layout/Row'
import SelectInputAsync from "../../common/form/SelectInputAsync";
import Input from '../../common/form/Input'
import _ from 'lodash'

class ItemList extends Component {

  constructor(props) {
    super(props)
    this.state = { listSize: 0 }
  }

  add(index) {
    if (!this.props.readOnly) {
        this.props.arrayInsert('objectForm',this.props.field,index,{})
        this.state = { listSize: index }
    }
}


  submitRemoveAction(index) {
    if (!this.props.readOnly && _.has(this.props,'list.length') && this.props.list.length > 1) {
      this.props.arrayRemove('objectForm', this.props.field, index)
    }
  }

  renderRows() {

    const {readOnly,field} = this.props
    const list = this.props.list && this.props.list.length>0?this.props.list:[{}]


    this.state = { listSize: list.length }

    return list.map((item, index) => (

      <div className='row form-group' key={index}>

        <Field name={`${field}[${index}].descricao`} component={Input} readOnly={readOnly} cols='6 3' />
        <Field name={`${field}[${index}].quantidade`} component={Input} readOnly={readOnly} cols='6 3' />

        <div className='tool-bar'>
          <button title='Adicionar Afins' type='button' className='btn btn-primary btn-sm' onClick={() => this.add(index + 1)} >
            <i className="fa fa-plus"></i>
          </button>
          <button type='button' className='btn btn-danger btn-sm' onClick={() => this.submitRemoveAction(index)} >
            <i className="fa fa-trash-o"></i>
          </button>
        </div>

      </div>
    ))
  }

 

  render() {

    return (
      <Grid cols={this.props.cols}>
        <div className='card'>
          <div className='card-body'>
              <div className='row'>
                <div className="col-xs-3 col-sm-3"><label>Utensilios</label></div>
                <div className="col-xs-3 col-sm-3"><label>Quantidade</label></div>
              </div>

            {this.renderRows()}
          </div>
        </div>
      </Grid>
    )
  }
}

const mapDispatchToProps = dispatch => bindActionCreators({ arrayInsert, arrayRemove }, dispatch)
 const values = formValueSelector('objectForm')
const mapStateToProps = state => ({
  item: values(state, 'item')
}) 
export default connect(mapStateToProps, mapDispatchToProps)(ItemList)
