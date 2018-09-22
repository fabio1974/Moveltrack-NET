import React, { Component } from   'react'
import { connect } from   'react-redux'
import { bindActionCreators } from 'redux'
import { Field, arrayInsert, arrayRemove, formValueSelector } from   'redux-form'
import Grid from '../../common/layout/Grid'
import Input from '../../common/form/Input'
import If from '../../common/operator/If'
import DateTimer from '../../common/form/DateTimer'
import Row from '../../common/layout/Row'
import labelAndSelect from '../../common/form/LabelAndSelect';
import SelectInputAsync from '../../common/form/SelectInputAsync'
import { setOptionState } from './../commons/crudActions'
import _ from 'lodash'

class ItemList extends Component {

  constructor(props){
    super(props)
    this.state = {pessoasSize: 0}
  }

  add(index, item = {}) {
    if (!this.props.readOnly) {
      this.props.arrayInsert('objectForm', this.props.field, index, item)
    }
  }

  submitRemoveAction(index) {
    if (!this.props.readOnly && _.has(this.props,'list.length') && this.props.list.length > 1) {
      this.props.arrayRemove('objectForm', this.props.field, index)
    }
  }


  renderRows() {

    const { readOnly, field } = this.props
    const list = this.props.list || [{}]

    this.state = {pessoasSize : list.length}

    return list.map((item, index) => (



      <div className="row form-group" key={index}>

        <Field name={`${field}[${index}].nome`} component={Input} readOnly={readOnly} cols='12 9' />
        <Field name={`${field}[${index}].id`} component='input' type='hidden' />

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

  adicionarPessoa(){
    const index = this.state.pessoasSize - 1
    this.add(index, this.props.item)
    this.state = {pessoasSize : index + 2}
    this.props.setOptionState({isLoading:false, value:''})
  }

  render() {

    return (
      <Grid cols={this.props.cols}>
        <div className='card'>
          <label>Pessoa</label>
          <div className='card-body' >
            <Row>

              <div className="input-group input-group-xs"  >
                <Field name='item' objectLabelProperty='nome' path='/pessoas' className='rs-sm' component={SelectInputAsync} cols='12 9' />
                <button type='button' className='btn btn-primary btn-xs' onClick={() => this.adicionarPessoa()} >Adicionar Pessoa </button>
              </div>

            </Row>

            {this.renderRows()}
          </div>
        </div>
      </Grid>

    )
  }
}

const values = formValueSelector('objectForm')
const mapStateToProps = state => ({
  item: values(state, 'item'),
})

const mapDispatchToProps = dispatch => bindActionCreators({ arrayInsert, arrayRemove , setOptionState}, dispatch)

export default connect(mapStateToProps, mapDispatchToProps)(ItemList)