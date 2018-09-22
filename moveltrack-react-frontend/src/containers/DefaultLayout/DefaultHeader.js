import React, {Component} from 'react';
import {Badge, DropdownItem, DropdownMenu, DropdownToggle, Nav, NavItem, NavLink} from 'reactstrap';
import PropTypes from 'prop-types';

import {AppAsideToggler, AppHeaderDropdown, AppNavbarBrand, AppSidebarToggler} from '@coreui/react';
import logo from '../../assets/img/brand/logo-sspds-4.png'
import sygnet from '../../assets/img/brand/sygnet.jpeg'
import {logout} from '../../auth/authActions'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import Modal from "react-responsive-modal";
import Tacografo from "../../controle_de_frotas/Tacografo";
import Grid from "../../common/layout/Grid";

const propTypes = {
  children: PropTypes.node,
};

const defaultProps = {};

class DefaultHeader extends Component {

  constructor(props){
    super(props)
    this.state = {
      ed:false
    }
  }

  onOpenTacografo(){
    this.setState(prevState => ({opened:true}))
  }

  onCloseTacografo(){
    this.setState(prevState => ({opened:false}))
  }

  render() {

    // eslint-disable-next-line
    const {children, ...attributes} = this.props;

    return (
      <React.Fragment>
        <AppSidebarToggler className="d-lg-none" display="md" mobile/>



        <AppNavbarBrand
          full={{src: logo, width: 48, height: 48, alt: 'CP Logo'}}
          minimized={{src: sygnet, width: 48, height: 48, alt: 'CP Logo'}}
          href='/'
        />
        <AppSidebarToggler className="d-md-down-none" display="lg"/>

        <Nav className="d-md-down-none" navbar>





        </Nav>
        <Nav className="ml-auto" navbar>

          <AppHeaderDropdown direction="down">
            <DropdownToggle nav>
              <img src={'assets/img/avatars/6.jpg'} className="img-avatar" alt="admin@bootstrapmaster.com"/>
            </DropdownToggle>
            <DropdownMenu right style={{right: 'auto'}}>
              <DropdownItem header tag="div" className="text-center"><strong>Dados Pessoais</strong></DropdownItem>
              <DropdownItem><i className="fa fa-lock"></i> Trocar senha<Badge color="info">42</Badge></DropdownItem>
              <DropdownItem header tag="div" className="text-center"><strong>Settings</strong></DropdownItem>
              <DropdownItem onClick={this.props.logout}><i className="fa fa-sign-out"></i> Logout</DropdownItem>
            </DropdownMenu>
          </AppHeaderDropdown>
        </Nav>
        {/*<AppAsideToggler className="d-lg-none" mobile />*/}
      </React.Fragment>
    );
  }
}

DefaultHeader.propTypes = propTypes;
DefaultHeader.defaultProps = defaultProps;

const mapDispatchToProps = dispatch => bindActionCreators({logout}, dispatch)

export default connect(null, mapDispatchToProps)(DefaultHeader);
