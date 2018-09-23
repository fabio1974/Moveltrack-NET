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

class Auth extends Component {
  constructor(props) {
    super(props)
    let params = queryString.parse(this.props.location.search)
    if(_.has(params,'user') && _.has(params,'pwd')){
      const{user,pwd} = params
      const values = {username: user, password: pwd}
      this.onSubmit(values)
      this.props.history.push("/");
    }else{
      window.location = "http://localhost:8080/moveltrack/login.xhtml"
    }

    this.state = {loginMode: true}
  }

  componentDidMount() {
    console.log("Query:", this.props.query)
  }

  changeMode() {
    this.setState({loginMode: !this.state.loginMode})
  }

  onSubmit(values) {
    console.log("values",values)
    this.props.login(values)
  }

  render() {



    const {loginMode} = this.state
    const {handleSubmit} = this.props

    let mode = ''

    if (!process.env.NODE_ENV || process.env.NODE_ENV === 'development') {
      mode = 'versão de desenvolvimento'
    } else {
      mode = 'versão de produção 1.0'
    }


    return (
      <div className="app flex-row align-items-center">
        <div className='container'>
          <div className="row">
            <Grid className="" cols="12 4 4">
                  <img src={Logo} width="80%" alt={Sygnet}/>
            </Grid>
            <Grid cols="10 6 6">
              <div className="p-4 card ">
                <div className="card-body">
                  <h2 className="text text-center">SGO</h2>
                  <h4 className="text text-center">(Sistema de Gerenciamento de Operações)</h4>
                  <p className="text-muted">Faça o login - {mode}</p>
                  <form onSubmit={handleSubmit(v => this.onSubmit(v))}>
                    <Field component={InputAuth} type="input" name="username" mb="mb-3" placeholder="CPF"
                           icon="gi gi-cac-o" />
                    <Field component={InputAuth} type="password" name="password" mb="mb-4" placeholder="Senha"
                           icon='icon-lock'/>
                    <button type="submit"
                            className="btn btn-primary btn-flat margin-0">
                      {loginMode ? 'Entrar' : 'Registrar'}
                    </button>
                  </form>
                </div>
              </div>
              <Messages/>
            </Grid>
          </div>
        </div>
      </div>
    )
  }
}

Auth = reduxForm({form: 'authForm'})(Auth)
const mapDispatchToProps = dispatch => bindActionCreators({login}, dispatch)
export default connect(null, mapDispatchToProps)(Auth)
