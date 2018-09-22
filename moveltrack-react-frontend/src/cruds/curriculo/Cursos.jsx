import React, { Component } from   'react'
import { connect } from   'react-redux'
import { bindActionCreators } from 'redux'
import { Field, arrayInsert, arrayRemove } from   'redux-form'
import Grid from '../../common/layout/Grid'
import Input from '../../common/form/Input'
import If from '../../common/operator/If'
import DateTimer from '../../common/form/DateTimer'
import Row from '../../common/layout/Row'
import labelAndSelect from '../../common/form/LabelAndSelect';


class ItemList extends Component {

    add(index, item = {}) {
        if (!this.props.readOnly) {
            this.props.arrayInsert('objectForm', this.props.field, index, item)
        }
    }

    submitRemoveAction(index) {
        if (!this.props.readOnly && this.props.list.length > 1) {
            this.props.arrayRemove('objectForm', this.props.field, index)
        }
    }

    idiomas() {
        const idiomas = ['INGLES', 'ESPANHOL', 'FRANCES', 'ALEMAO', 'ARABE', 'ITALIANO', 'JAPONES', 'CHINES', 'RUSSO']
        return idiomas.map(idioma => (
            <option key={idioma} value={idioma}>{idioma}</option>
        ))
    }

    niveis() {
        const niveis = ['BASICO', 'INTERMEDIARIO', 'AVANCADO']
        return niveis.map(nivel => (
            <option key={nivel} value={nivel}>{nivel}</option>
        ))
    }





    renderRows() {

        const { readOnly, field } = this.props
        const list = this.props.list || [{}]

        return list.map((item, index) => (

            <Row key={index}>
                <div className='tool-bar'>
                    <button type='button' className='btn btn-primary btn-sm' onClick={() => this.add(index + 1)} >
                        <i className="fa fa-plus"></i>
                    </button>
                    <button type='button' className='btn btn-danger btn-sm' onClick={() => this.submitRemoveAction(index)} >
                        <i className="fa fa-trash-o"></i>
                    </button>
                </div>


                <If test={field === 'idiomas'}>
                    <Field name={`${field}[${index}].descricao`} component={labelAndSelect} readOnly={readOnly} cols='12 4' >
                        <option></option>
                        {this.idiomas()}
                    </Field>
                    <Field name={`${field}[${index}].nivel`} component={labelAndSelect} readOnly={readOnly} cols='12 4' >
                        <option></option>
                        {this.niveis()}
                    </Field>
                </If>

                <If test={field !== 'idiomas'}>
                    <Field name={`${field}[${index}].descricao`} component={Input} placeholder={field === 'formacoes' ? 'descricao' : field === 'cursos' ? 'descricao do curso' : 'atividade'} readOnly={readOnly} cols='12 4' />

                    <If test={field === 'cursos' || field === 'formacoes'}>
                        <Field name={`${field}[${index}].instituicao`} component={Input} placeholder='instituicao' readOnly={readOnly} cols='12 3' />
                    </If>

                    <If test={field === 'experiencias'}>
                        <Field name={`${field}[${index}].empresa`} component={Input} placeholder='empresa' readOnly={readOnly} cols='12 3' />
                    </If>

                    <Field name={`${field}[${index}].inicio`} className='rdt-sm' component={DateTimer} placeholder='início' readOnly={readOnly} cols='12 2' />
                    <Field name={`${field}[${index}].fim`} className='rdt-sm' component={DateTimer} placeholder='fim' readOnly={readOnly} cols='12 2' />
                </If>
            </Row>

        ))
    }

    render() {

        const index = 0


        return (
            <Grid cols={this.props.cols}>
                <div className='card'>
                    <div className='card-header'>{this.props.titulo}</div>
                    <div className='card-body' >

                        {this.renderRows()}



                    </div>
                </div>
            </Grid>

        )
    }
}
const mapDispatchToProps = dispatch => bindActionCreators({ arrayInsert, arrayRemove}, dispatch)
export default connect(null, mapDispatchToProps)(ItemList)
