import React, {Component} from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'

import ContentHeader from '../../common/template/contentHeader'
import Content from '../../common/template/content'
import Tabs from '../../common/tab/tabs'
import TabsHeader from '../../common/tab/tabsHeader'
import TabsContent from '../../common/tab/tabsContent'
import TabHeader from '../../common/tab/tabHeader'
import TabContent from '../../common/tab/tabContent'
import Paginator from '../../common/paginator/paginator'
import If from '../../common/operator/If'
import {withRouter} from 'react-router-dom'
import {initialize} from 'redux-form'
import {initAction, submitCreateAction, submitUpdateAction, submitRemoveAction, setPathAction} from './crudActions'
import {LOCATION_CHANGE} from 'react-router-redux'
import {withLastLocation} from 'react-router-last-location';
import {goAction} from '../../common/paginator/paginatorActions'
import List from './List'
import _ from 'lodash'
import {Actions} from '../../main/Actions';
import MapContainer from "../../controle_de_frotas/MapComponent";

class Crud extends Component {

  componentWillMount() {
    this.props.setPathAction(this.props.path)
    const location = this.props.location
    const state = location.state
    if (!(_.has(state, 'from') && (state.from === 'cross-page-action'))) {
      this.props.goAction(Actions.GO_FIRST, 0)
      this.props.initAction(this.props.initialFormValues)
    }
    if (state && state.from) {
      delete state.from
      this.props.history.replace(...location, state)
    }

  }


  getFieldsFromObject() {
    const fields = Object.keys(this.props.object)
    if (this.props.onlyRequireds) {

      var riquiredFields = []
      fields.map(field => {
        if (this.props.object[field].required)
          this.props.riquiredFields.push(field)
      });
      return this.props.riquiredFields
    } else
      return fields
  }

  render() {

    const Form = this.props.formComponent;

    return (
      <div>
        <ContentHeader path={this.props.path} secao={this.props.secao} titulo={this.props.titulo}/>
        <Content>
          <Tabs>
            <TabsHeader>
              <TabHeader label='Listar' icon='bars' target='tabList'/>
              <TabHeader label='Incluir' icon='plus' target='tabCreate'/>
              <TabHeader label='Alterar' icon='pencil' target='tabUpdate'/>
              <TabHeader label='Excluir' icon='trash-o' target='tabDelete'/>
            </TabsHeader>
            <TabsContent>
              <TabContent id='tabList'>
                <If test={this.props.paginated}>
                  <Paginator path={this.props.path}>
                    <List object={this.props.object} fields={this.getFieldsFromObject()} path={this.props.path}/>
                  </Paginator>
                </If>
                <If test={!this.props.paginated}>
                  <List object={this.props.object} fields={this.getFieldsFromObject()} path={this.props.path}/>
                </If>
              </TabContent>
              <TabContent id='tabCreate'>

                <Form path={this.props.path}
                      onSubmit={(values) => this.props.submitCreateAction(values, this.props.initialFormValues)}
                      initialFormValues={this.props.initialFormValues} submitLabel='Incluir' submitClass='primary'/>
              </TabContent>
              <TabContent id='tabUpdate'>
                <Form path={this.props.path}
                      onSubmit={(values) => this.props.submitUpdateAction(values, this.props.initialFormValues)}
                      initialFormValues={this.props.initialFormValues} submitLabel='Alterar' submitClass='info'/>
              </TabContent>
              <TabContent id='tabDelete'>
                <Form path={this.props.path}
                      onSubmit={(values) => this.props.submitRemoveAction(values, this.props.initialFormValues)}
                      initialFormValues={this.props.initialFormValues} readOnly={true} submitLabel='Excluir'
                      submitClass='danger'/>
              </TabContent>
            </TabsContent>
          </Tabs>



        </Content>
      </div>
    )
  }


}

const mapStateToProps = state => (
  {
    pageSize: state.paginatorReducer.pageSize,
    currentPage: state.paginatorReducer.currentPage,

  })

const mapDispatchToProps = dispatch => bindActionCreators({
  goAction,
  initAction,
  submitCreateAction,
  submitUpdateAction,
  submitRemoveAction,
  setPathAction
}, dispatch)
export default withLastLocation(withRouter(connect(mapStateToProps, mapDispatchToProps)(Crud)))
