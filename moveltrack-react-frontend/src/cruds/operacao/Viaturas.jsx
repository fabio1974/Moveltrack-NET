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
import SelectInputAsync from '../../common/form/SelectInputAsync';
import _ from 'lodash'

class ItemList extends Component {

    constructor(props){
        super(props)
        this.state = {listSize: 0}
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

        this.state = {listSize : list.length}

        return list.map((item, index) => (



            <div className="row form-group" key={index}>

                <Field name={`${field}[${index}].placa`} component={Input} readOnly={readOnly} cols='12 3' />
                <Field name={`${field}[${index}].marcaModelo`} component={Input} readOnly={readOnly} cols='12 3' />
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

    adicionarItem(){
        const index = this.state.listSize - 1
        const item = {...this.props.item, placa: this.props.item.placa.substring(0,7), marcaModelo: this.props.item.placa.substring(8)}
        this.add(index, item)
        this.state = {listSize : index + 2}
    }

    render() {

        return (
            <Grid cols={this.props.cols}>
                <div className='card'>
                    <div className='card-body' >
                        <Row>

                            <div className="input-group input-group-xs"  >
                                <Field name='item' objectLabelProperty='placa' objectLabelProperty2='marcaModelo' path='/viaturas' className='rs-sm' component={SelectInputAsync} cols='12 6' />
                                <button type='button' className='btn btn-primary btn-xs' onClick={() => this.adicionarItem()} >Adicionar Viatura </button>
                            </div>





                        </Row>

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
    item: values(state, 'item'),
})
export default connect(mapStateToProps, mapDispatchToProps)(ItemList)
