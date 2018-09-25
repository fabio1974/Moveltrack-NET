import './auth.css'
import React, {Component} from 'react'
import {Field, reduxForm} from 'redux-form'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'

import {login} from './authActions'
import Grid from '../common/layout/Grid'
import Messages from '../common/msg/messages'
import InputAuth from '../common/form/InputAuth';
import Logo from '../../src/assets/img/brand/logo-sspds-4.png'
import Sygnet from '../../src/assets/img/brand/sygnet.jpeg'
import {createTextMask} from 'redux-form-input-masks'
import queryString from 'query-string';
import _ from "lodash"

const cpfMask = createTextMask({
  pattern: '999.999.999-99'
})

class AuthRemote extends Component {
  constructor(props) {
    super(props)
    let token = this.props.location.search
    if(token){
      try {
        const jwt = this.decodeToken(token)
        const values = {username: jwt.jti, password: jwt.iss}
        this.onSubmit(values)
        this.props.history.push("/");
      }catch (e) {
        window.location = "http://localhost:8080/moveltrack/login.xhtml"
      }
    }else{
      window.location = "http://localhost:8080/moveltrack/login.xhtml"
    }
    this.state = {loginMode: true}
  }

  decodeToken(token) {
    return  JSON.parse(atob(token.split('.')[1]));
  };

  changeMode() {
    this.setState({loginMode: !this.state.loginMode})
  }

  onSubmit(values) {
    console.log("values",values)
    this.props.login(values)
  }

  render() {
    return (
      <div></div>
    )
  }
}

AuthRemote = reduxForm({form: 'authForm'})(AuthRemote)
const mapDispatchToProps = dispatch => bindActionCreators({login}, dispatch)
export default connect(null, mapDispatchToProps)(AuthRemote)
