import React, { Component } from   'react'
import { connect } from   'react-redux'
import { bindActionCreators } from 'redux'
import { reduxForm, Field, formValueSelector } from   'redux-form'
import LabelAndInput from '../../common/form/LabelAndInput'
import formValidator from './curriculoValidator'
import { initAction } from '../commons/crudActions'
import { initialize } from   'redux-form'
import DateTimer from '../../common/form/DateTimer';
import SelectInputAsync from '../../common/form/SelectInputAsync'
import Cursos from './Cursos'
import Grid from '../../common/layout/Grid'
import Input from '../../common/form/Input'
import Row from '../../common/layout/Row'

class CurriculoForm extends Component {

    constructor(props) {
        super(props);
        initialize('objectForm', this.props.initialFormValues)
    }

    componentWillMount() {

    }



    render() {

        const { handleSubmit, readOnly } = this.props
        const index = 0
        

        return (
            <form role='form' onSubmit={handleSubmit}>

                <div className='card'>
                    <div className='card-header' >Dados do Curriculo</div>

                    <div className='card-body' >

                    <div className='row'>
                        <Field name="pessoa" objectLabelProperty='nome' path='/pessoas' component={SelectInputAsync} className='rs-sm' label='Agente Público' readOnly={readOnly} cols='12 4' />

                        <Field name='endereco' component={LabelAndInput} readOnly={readOnly} label='Endereco' cols='12 4' placeholder='Endereço' />
                        <Field name='data' className='rdt-sm' component={DateTimer} readOnly={readOnly} label='Data' cols='12 4' placeholder='Ultima Atualização' />
                        
                    </div> 


                    <Row>
                        <Cursos list={this.props.formacoes} field='formacoes' titulo='Formações Acadêmicas' cols='12 12 12' readOnly={readOnly}  />   
                    </Row>

                    <Row>
                        <Cursos list={this.props.cursos} field='cursos' titulo='Cursos Realizados' cols='12 12 12' readOnly={readOnly}  />   
                    </Row>

                    <Row>
                        <Cursos list={this.props.experiencias} field='experiencias' titulo='Trabalhos anteriores' cols='12 12 12' readOnly={readOnly}  />   
                    </Row>

                    <Row>
                        <Cursos list={this.props.idiomas} field='idiomas' titulo='Idiomas' cols='12 6 6' readOnly={readOnly}  />   
                    </Row>




                    
                    <div className='row'>
                    <Field name='observacao' component={LabelAndInput} readOnly={readOnly} label='Observacao' cols='12 6' placeholder='Deixe uma mensagem' />
                    <Field name='trabalhoAtual' component={LabelAndInput} type='string' readOnly={readOnly} label='Atividade Atual' cols='12 6' placeholder='Ultima ocupação' />
                    </div>

                    </div>

                    




               

                <div className='card-footer'>
                    <button type='submit' className={`btn btn-${this.props.submitClass}`}>
                        {this.props.submitLabel}
                    </button>
                    <button type='button' className='btn btn-default' onClick={() => this.props.initAction(this.props.initialFormValues)}>Cancelar</button>
                </div>
                </div>
            </form >
        )
    }
}

CurriculoForm = reduxForm({ form: 'objectForm', destroyOnUnmount: false, validate: formValidator })(CurriculoForm)

const values = formValueSelector('objectForm')
const mapStateToProps = state => ({
    formacoes: values(state,'formacoes'),
    cursos: values(state, 'cursos'),
    experiencias: values(state, 'experiencias'),
    idiomas: values(state, 'idiomas'),
    perfis: state.tabelaApoioReducer.perfis
})

//const mapStateToProps = state => ({ perfis: state.tabelaApoioReducer.perfis})
const mapDispatchToProps = dispatch => bindActionCreators({ initAction }, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(CurriculoForm)
