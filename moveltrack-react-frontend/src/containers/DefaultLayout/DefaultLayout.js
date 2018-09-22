import React, {Component} from 'react';
import {connect} from 'react-redux'
import {Redirect, Route, Switch} from 'react-router-dom';
import Messages from '../../common/msg/messages'
import MaterialIcon, {colorPalette} from 'material-icons-react';


import {
  AppAside,
  AppBreadcrumb,
  AppFooter,
  AppHeader,
  AppSidebar,
  AppSidebarFooter,
  AppSidebarForm,
  AppSidebarHeader,
  AppSidebarMinimizer,
  /*AppSidebarNav,*/
} from '@coreui/react';
// sidebar nav config
import navigation from '../../_nav';
// routes config
import routes from '../../main/routes';
import DefaultAside from './DefaultAside';
import DefaultHeader from './DefaultHeader';


import Dashboard from '../../dashboard/dashboard'
import PessoaCrud from '../../cruds/pessoa/PessoaCrud'
import ViaturaCrud from '../../cruds/viatura/viaturaCrud'
import CurriculoCrud from '../../cruds/curriculo/CurriculoCrud'
import EscalaTipoCrud from '../../cruds/escalaTipo/EscalaTipoCrud'
import ArmaCrud from '../../cruds/arma/ArmaCrud'
import OperacaoCrud from '../../cruds/operacao/OperacaoCrud'
import AreaOrganizacionalCrud from '../../cruds/areaOrganizacional/AreaOrganizacionalCrud'
import EscalaCrud from '../../cruds/escala/EscalaCrud'
import Calendar from '../../cruds/escala/EscalaCalendar'
import MapComponent from '../../controle_de_frotas/MapComponent'
import AfastamentoCrud from '../../cruds/afastamento/AfastamentoCrud'
import EscalaConfig, {meuSubmit} from "../../cruds/escala/EscalaConfig";
import MySidebarNav from "../../common/layout/MySidebarNav";
import ChipCrud from "../../cruds/chip/ChipCrud";
import RastreadorCrud from "../../cruds/rastreador/RastreadorCrud";
import DashboardRastrementoViatura from "../../controle_de_frotas/DashboardRastrementoViatura";
import {bindActionCreators} from "redux";
import {cancelFormView, initAction} from "../../cruds/commons/crudActions";
import configReducer from "../../main/configReducer";


class DefaultLayout extends Component {

  constructor(props) {
    super(props)
    this.url = this.props.url
  }


  render() {


  console.log("this.props.sideBarMinimized",this.props.sideBarMinimized)



    return (
      <div className="app">
        <AppHeader fixed>
          <DefaultHeader/>
        </AppHeader>
        <div className={`app-body ${this.props.sideBarMinimized?'sidebar-minimized':''}`}>
          <AppSidebar  display="lg">
            <AppSidebarHeader/>
            <AppSidebarForm/>
            <MySidebarNav  navConfig={navigation} {...this.props} />
            <AppSidebarFooter/>
            <AppSidebarMinimizer  />
          </AppSidebar>
          <main className="main">


             <Switch>
              <Route exact path='/' component={Dashboard} />
              <Route path='/operacaos' component={OperacaoCrud} />
              <Route path='/areaOrganizacionals' component={AreaOrganizacionalCrud} />
              <Route path='/escalaTipos' component={EscalaTipoCrud} />
              <Route path='/pessoas' component={PessoaCrud} />
              <Route path='/viaturas' component={ViaturaCrud} />
              <Route path='/curriculos' component={CurriculoCrud} />
              <Route path='/armas' component={ArmaCrud} />
              <Route path='/calendar' component={Calendar} />
              <Route path='/escalaConfig' component={EscalaConfig}/>
              <Route path='/escalas' component={EscalaCrud}/>
              <Route path='/chips' component={ChipCrud}/>
              <Route path='/rastreadors' component={RastreadorCrud}/>
              <Route path='/mapa' component={DashboardRastrementoViatura} />
              <Route path='/afastamentos' component={AfastamentoCrud}/>
              <Redirect from='*' to='/' />

              <Route path='/mapa' component={Map}/>
              <Redirect from='*' to='/'/>
            </Switch>


          </main>
          <AppAside fixed hidden>
            <DefaultAside/>
          </AppAside>
        </div>
        <Messages/>
      </div>
    );
  }
}



const mapStateToProps = state => ({
  sideBarMinimized: state.configReducer.sideBarMinimized,
  sideBarHidden: state.configReducer.sideBarHidden

})
const mapDispatchToProps = dispatch => bindActionCreators({ initAction, cancelFormView }, dispatch)
export default connect(mapStateToProps, null)(DefaultLayout)
