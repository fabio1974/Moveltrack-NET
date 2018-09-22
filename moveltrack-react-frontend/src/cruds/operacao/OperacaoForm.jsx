import React, { Component } from 'react'
import { connect } from 'react-redux'
import { reduxForm, Field, formValueSelector } from 'redux-form'
import LabelAndInput from '../../common/form/LabelAndInput'
import LabelAndSelect from '../../common/form/LabelAndSelect'
import { bindActionCreators } from 'redux'
import DateTimer from '../../common/form/DateTimer'
import formValidator from './operacaoValidator'
import { initialize } from 'redux-form'
import { showSubTabsAction, selectSubTabAction } from '../../common/tab/subTabActions'
import { initAction, cancelFormView } from '../commons/crudActions'
import TabsHeader from '../../common/tab/tabsHeader'
import TabsContent from '../../common/tab/tabsContent'
import SubTabHeader from '../../common/tab/subTabHeader'
import SubTabContent from '../../common/tab/subTabContent'
import Tabs from '../../common/tab/tabs'
import Row from '../../common/layout/Row'
import Pessoas from './Pessoas'
import Viaturas from './Viaturas'
import ArmaApreendidas from './ArmaApreendidas'
import Armas from './Armas';
import EquipamentoDeApoios from './EquipamentoDeApoios'
import PessoaApreendidas from './PessoaApreendidas'
import DrogaApreendidas from './DrogaApreendidas'
import Crimes from './Crimes'
import Acidentes from './Acidentes'


import { getOperacaoStatuss } from '../../cruds/commons/tabelaApoioActions'

class OperacaoForm extends Component {

  constructor(props) {
    super(props);
    initialize('objectForm', this.props.initialFormValues)
    this.props.showSubTabsAction('tabGerais', 'tabViaturas', 'tabPessoas', 'tabArmas', 'tabEquipamentoDeApoios', 
                                 'tabArmaApreendidas', 'tabPessoaApreendidas', 'tabDrogaApreendidas', 'tabCrimes', 'tabAcidentes')
    this.props.selectSubTabAction('tabGerais')
    this.props.getOperacaoStatuss()
  }

  componentWillMount() {

  }

  componentDidMount() {
  }

  buildOperacaoStatuss() {
    const operacaoStatuss = this.props.operacaoStatuss || []
    return operacaoStatuss.map(operacaoStatus => (
      <option key={operacaoStatus} value={operacaoStatus}>{operacaoStatus}</option>
    ))
  }


  render() {

    const { handleSubmit, readOnly } = this.props
    return (
      <form role='form' onSubmit={handleSubmit}>

        <div className="card">
          <div className="card-header"><i className="gi gi-gears"></i><strong>Dados da Operação</strong></div>

          <div className="card-body">

            <Tabs>

              <TabsHeader>
                <SubTabHeader label='Dados Gerais' icon='gi gi-gears' target='tabGerais' />
                <SubTabHeader label='Pessoas' icon='gi gi-users' target='tabPessoas' />
                <SubTabHeader label='Viaturas' icon='fa fa-cab' target='tabViaturas' />
                <SubTabHeader label='Armas Utilizadas' icon='gi gi-gun' target='tabArmas' />
                <SubTabHeader label='Equipamentos de Apoio' icon='gi gi-tools' target='tabEquipamentoDeApoios' />
                <SubTabHeader label='Armas Apreendidas' icon='gi gi-tools' target='tabArmaApreendidas' />
                <SubTabHeader label='Pessoas Apreendidas' icon='gi gi-tools' target='tabPessoaApreendidas' />
                <SubTabHeader label='Drogas Apreendidas' icon='gi gi-tools' target='tabDrogaApreendidas' />
                <SubTabHeader label='Crimes Ocorridos' icon='gi gi-tools' target='tabCrimes' />
                <SubTabHeader label='Acidentes Ocorridos' icon='gi gi-tools' target='tabAcidentes' />
              </TabsHeader>

              <TabsContent>
                <SubTabContent id='tabGerais'>
                  <Row>
                    <Field name='nome' component={LabelAndInput} readOnly={readOnly} label='Nome da Operacao' cols='12 4' />
                    <Field name='inicio' className='rdt-sm' component={DateTimer} readOnly={readOnly} label='Início da Operação' cols='12 4' />
                    <Field name='fim' className='rdt-sm' component={DateTimer} readOnly={readOnly} label='Fim da Operação' cols='12 4' />
                    <Field name='local' component={LabelAndInput} readOnly={readOnly} label='Local da Operação' cols='12 4' />
                    <Field name='status' component={LabelAndSelect} readOnly={readOnly} label='Status da Operação' cols='12 4'>
                      <option></option>
                      {this.buildOperacaoStatuss()}
                    </Field>
                  </Row>
                </SubTabContent>

                <SubTabContent id='tabPessoas'>
                  <Row>
                    <Pessoas list={this.props.pessoas} field='pessoas' titulo='Pessoas' cols='12 12 12'
                      readOnly={readOnly} />
                  </Row>
                </SubTabContent>

                <SubTabContent id='tabViaturas'>
                  <Row>
                    <Viaturas list={this.props.viaturas} field='viaturas' titulo='Viaturas' cols='12 12 12'
                      readOnly={readOnly} />
                  </Row>
                </SubTabContent>

                <SubTabContent id='tabArmas'>
                  <Row>
                    <Armas list={this.props.armas} field='armas' titulo='Armas' cols='12 12 12' readOnly={readOnly} />
                  </Row>
                </SubTabContent>

                <SubTabContent id='tabEquipamentoDeApoios'>
                  <Row>
                    <EquipamentoDeApoios list={this.props.equipamentoDeApoios} field='equipamentoDeApoios' titulo='Equipamentos de Apoio' cols='12 12 12' readOnlu={readOnly} />
                  </Row>
                </SubTabContent>

                <SubTabContent id='tabArmaApreendidas'>
                  <Row>
                    <ArmaApreendidas list={this.props.armaApreendidas} field='armaApreendidas' titulo='Armas Apreendidas' cols='12 12 12' readOnlu={readOnly} />
                  </Row>
                </SubTabContent>

                <SubTabContent id='tabPessoaApreendidas'>
                  <Row>
                    <PessoaApreendidas list={this.props.pessoaApreendidas} field='pessoaApreendidas' titulo='Pessoas Apreendidas' cols='12 12 12' readOnlu={readOnly} />
                  </Row>
                </SubTabContent>

                <SubTabContent id='tabDrogaApreendidas'>
                  <Row>
                    <DrogaApreendidas list={this.props.drogaApreendidas} field='drogaApreendidas' titulo='Drogas Apreendidas' cols='12 12 12' readOnlu={readOnly} />
                  </Row>
                </SubTabContent>

                <SubTabContent id='tabCrimes'>
                  <Row>
                    <Crimes list={this.props.crimes} field='crimes' titulo='Crimes Ocorridos' cols='12 12 12' readOnlu={readOnly} />
                  </Row>
                </SubTabContent>

                <SubTabContent id='tabAcidentes'>
                  <Row>
                    <Acidentes list={this.props.acidentes} field='acidentes' titulo='Acidentes' cols='12 12 12' readOnlu={readOnly} />
                  </Row>
                </SubTabContent>



              </TabsContent>
            </Tabs>


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

OperacaoForm = reduxForm({ form: 'objectForm', destroyOnUnmount: false, validate: formValidator })(OperacaoForm)

const values = formValueSelector('objectForm')
const mapStateToProps = state => ({
  pessoas: values(state, 'pessoas'),
  viaturas: values(state, 'viaturas'),
  armas: values(state, 'armas'),
  buscador: values(state, 'buscador'),
  equipamentoDeApoios: values(state, 'equipamentoDeApoios'),
  armaApreendidas: values(state,'armaApreendidas'),
  pessoaApreendidas: values(state,'pessoaApreendidas'),
  drogaApreendidas: values(state,'drogaApreendidas'),
  crimes: values(state,'crimes'),
  acidentes: values(state,'acidentes'),
  operacaoStatuss: state.tabelaApoioReducer.operacaoStatuss,
})
const mapDispatchToProps = dispatch => bindActionCreators({
  initAction, cancelFormView, showSubTabsAction, selectSubTabAction, getOperacaoStatuss,
  
}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(OperacaoForm)
