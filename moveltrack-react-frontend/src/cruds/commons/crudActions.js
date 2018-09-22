import axios from 'axios'
import { toastr } from 'react-redux-toastr'
import { initialize } from 'redux-form'
import { showTabsAction, selectTabAction } from '../../common/tab/tabActions'
import { getPageList } from './listAction'
import _ from 'lodash'
import consts from '../../common/consts'
import {Actions} from '../../main/Actions'
import { sendError } from './../../common/utils'


export function setPathAction(path) {
    return {
        type: Actions.SET_PATH,
        payload: path
    }
}

export function addFilterAction(filter) {
    return {
        type: Actions.ADD_FILTER,
        payload: filter,
    }
}

export function initAction(initialFormValues) {
    const actions = []
    actions.push(getPageList())  //Inicia a lista junto com as tabs
    actions.push(showTabsAction('tabList', 'tabCreate'))
    actions.push(selectTabAction('tabList'))
    actions.push(initialize('objectForm', initialFormValues))
    actions.push(setOptionState({}))
    return actions
}

export function cancelFormView(initialFormValues) {
    const actions = []
    actions.push(showTabsAction('tabList', 'tabCreate'))
    actions.push(selectTabAction('tabList'))
    actions.push(initialize('objectForm', initialFormValues))
    actions.push(setOptionState({}))
    return actions
}



export function setOptionState(optionState) {
    return {
        type: Actions.SET_OPTION_STATE,
        payload: optionState,
    }
}

export function showUpdateFormAction(object) {
    console.log("OBJECT",object)
    return [
        showTabsAction('tabUpdate'),
        selectTabAction('tabUpdate'),
        initialize('objectForm', object)

    ]
}

export function insereCurriculoPessoaAction(pessoa, history) {
    return [

        history.push('/curriculos', { from: "cross-page-action" }),
        showTabsAction('tabList', 'tabCreate'),
        selectTabAction('tabCreate'),
        initialize('objectForm', { "data": new Date(), "pessoa": pessoa }),
    ]
}


export function showDeleteFormAction(object) {
    return [
        showTabsAction('tabDelete'),
        selectTabAction('tabDelete'),
        initialize('objectForm', object)
    ]
}

export function submitCreateAction(values, initialFormValues) {
    return submit(values, 'post', initialFormValues)
}

export function submitUpdateAction(values, initialFormValues) {
    return submit(values, 'patch', initialFormValues)
}

export function submitRemoveAction(values, initialFormValues) {
    //values = JSON.stringify(values)
    return submit(values, 'delete', initialFormValues)
}

function submit(values, method, initialFormValues) {
    return (dispatch, getState) => {

      console.log("SUBMIT",values)

        const path = getState().crudReducer.path
        const id = values.id ? values.id : ''
        delete values.item


        if (path !== '/operacaos' && path!== '/escalas') {
            Object.keys(values).forEach(
                key => {
                    const field = values[key]
                    const fieldArray = []

                    if (Array.isArray(field)) {

                        field.forEach(
                            obj => {
                                let newFieldValue
                                if (_.has(obj, 'id'))
                                    newFieldValue = consts.API_URL + "/" + key + "/" + obj.id
                                else
                                    newFieldValue = obj

                                fieldArray.push(newFieldValue)
                            }
                        )
                        values = { ...values, [key]: fieldArray }

                    } else if (_.has(field, 'id')) {
                        console.log(key, values[key])
                        const newFieldValue = consts.API_URL + "/" + key + "s/" + field['id']
                        values = { ...values, [key]: newFieldValue }
                    }
                }
            )
        }

        const url = `${consts.API_URL}${path}/${id}`
        console.log("values",values)
        axios[method](url, values)
            .then(() => {
                toastr.success('Sucesso', 'Operação Realizada com sucesso.');
                dispatch(initAction(initialFormValues));
            })
            .catch(e => {
                sendError(e)
            })
    }
}




