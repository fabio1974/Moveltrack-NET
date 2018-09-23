import React, { Component } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import './../App.css';
// Styles
// CoreUI Icons Set
import '@coreui/icons/css/coreui-icons.min.css';
// Import Flag Icons Set
import 'flag-icon-css/css/flag-icon.min.css';
// Import Font Awesome Icons Set
import 'font-awesome/css/font-awesome.min.css';
// Import Simple Line Icons Set
import 'simple-line-icons/css/simple-line-icons.css';
// Import Main styles for this application

import 'govicons/css/govicons.min.css'
import './../scss/style.css'
import './custom.css'
import './rdt.css'


import { DefaultLayout } from '../containers';
import { Login, Page404, Page500, Register } from '../views/Pages';
import axios from 'axios'
import Auth from '../auth/auth'
//import { isLoggedIn } from './../auth/authActions'


import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import AuthRemote from "../auth/AuthRemote";
// import { renderRoutes } from 'react-router-config';

class AuthOrApp extends Component {

    componentWillMount() { }



    isLoggedIn() {
        const access_token = JSON.parse(localStorage.getItem("cp_access_token"))
        return access_token && !this.isExpired(access_token)
    }

    isExpired(token) {
        const decodedToken = this.decodeToken(token, { complete: true })
        console.log("TOKEN",decodedToken)
        const expiringDate = new Date(1000 * decodedToken.exp)
        return expiringDate < new Date()
    }

    decodeToken(token) {
        return  JSON.parse(atob(token.split('.')[1]));
    };

    render() {
        if (this.isLoggedIn()) {
            const access_token = JSON.parse(localStorage.getItem("cp_access_token"))
            axios.defaults.headers.common['authorization'] = `Bearer ${access_token}`
            return (
                <BrowserRouter>
                    <Switch>
                        <Route path="/" name="Home" component={DefaultLayout} />
                    </Switch>
                </BrowserRouter>
            )
        } else

            return (
              <BrowserRouter>
                <Switch>
                  <Route path="/" name="Home" component={AuthRemote} />
                </Switch>
              </BrowserRouter>

            )
    }
}

const mapStateToProps = state => ({ auth: state.auth })
//const mapDispatchToProps = dispatch => bindActionCreators({ isLoggedIn }, dispatch)
export default connect(mapStateToProps, null)(AuthOrApp)
