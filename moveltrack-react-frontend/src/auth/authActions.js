import { toastr } from 'react-redux-toastr'
import axios from 'axios'
import consts from '../common/consts'
import querystring from 'querystring'

import {Actions} from '../main/Actions'
import _ from 'lodash'
import { sendError } from '../common/utils';



const key = "cp_access_token";

export function login(values) {
    return dispatch => {
        const url = `${consts.OAPI_URL}/oauth/token`
        const data = querystring.stringify({ ...values, grant_type: "password" })
        

        
        //evita erros de adição de undefined autorization header
        delete axios.defaults.headers.common["authorization"];

        axios(url, {
            method: 'POST',
            headers: {
                //Cli Username react-web, Password: re@ct2018
                                //'Basic '+btoa('username:password'), 
                'authorization': 'Basic cmVhY3Qtd2ViOnJlQGN0MjAxOA==',
                'Content-Type': 'application/x-www-form-urlencoded'
            }, data: data,
            withCredentials: false,
        }).then(resp => {
            const access_token = resp.data.access_token
            localStorage.setItem(key, JSON.stringify(access_token))
            dispatch([
                { type: Actions.LOGIN, payload: access_token }
            ])
        }).catch(e => {
            sendError(e)
        })
    }
}

export function logout() {
    return dispatch => {
        const url = `${consts.OAPI_URL}/tokens/revoke`
        axios(url, {
            method: 'DELETE',
            withCredentials: false,
        }).then(resp => {
            localStorage.removeItem(key)
            dispatch([
                { type: Actions.LOGOUT, payload: null }
            ])
        })
            .catch(e => {
                sendError(e)
            })
    }
}


export function refreshToken() {
    return dispatch => {
        const url = `${consts.OAPI_URL}/oauth/token`
        const data = `grant_type="refresh_token"`
        axios.post(url, data, {
            headers: {
                'Authorization': 'Basic YW5ndWxhcjoxMjM0',
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then(resp => {
            dispatch([
                { type: 'LOGOUT', payload: null }
            ])
        }).catch(e => {
            sendError(e)
        })
    }
}
