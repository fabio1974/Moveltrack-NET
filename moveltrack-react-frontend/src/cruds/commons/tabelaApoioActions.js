import axios from 'axios'
import consts from '../../common/consts'
import {Actions} from '../../main/Actions';
import { sendError } from '../../common/utils';


export function getPerfis() {
    return dispatch => {
        const url = `${consts.API_URL}/perfils`
        axios.get(url).then(resp =>
            dispatch({
                type: Actions.PERFIS_FETCHED,
                payload: resp.data.content
            })
        ).catch(e=>sendError(e))
    }
}

export function getEscalaTipos() {
    return dispatch => {
        const url = `${consts.API_URL}/escalaTipos`
        axios.get(url).then(resp => {
            dispatch({
                type: Actions.ESCALA_TIPOS_FETCHED,
                payload: resp.data.content
            })
        }).catch(e=>sendError(e))
    }
}

export function getOperacaoStatuss() {
  return dispatch => {
      const url = `${consts.API_URL}/operacaoStatuss`
      axios.get(url).then(resp => {
          dispatch({
              type: Actions.OPERACAO_STATUS_FETCHED,
              payload: resp.data
          })
      }).catch(e=>sendError(e))
  }
}

export function getAreaOrganizacionals() {
    return dispatch => {
        const url = `${consts.API_URL}/areaOrganizacionals`
        axios.get(url).then(resp => {
            dispatch({
                type: Actions.AREA_ORGANIZACIONALS_FETCHED,
                payload: resp.data.content
            })
        }).catch(e=>sendError(e))
    }
}

export function getEscalas() {
    return dispatch => {
        const url = `${consts.API_URL}/escalas`
        axios.get(url).then(resp => {
            dispatch({
                type: Actions.ESCALAS_FETCHED,
                payload: resp.data.content
            })
        }).catch(e=>sendError(e))
    }
}

export function getPessoas() {
  return dispatch => {
    const url = `${consts.API_URL}/pessoas`
    axios.get(url).then(resp =>
      dispatch({
        type: Actions.PESSOAS_FETCHED,
        payload: resp.data.content
      })
    ).catch(e=>sendError(e))
  }
}

export function getViaturas() {
  return dispatch => {
    const url = `${consts.API_URL}/viaturas`
    axios.get(url).then(resp =>
      dispatch({
        type: Actions.VIATURAS_FETCHED,
        payload: resp.data.content
      })
    ).catch(e=>sendError(e))
  }
}

export function getArmas() {
  return dispatch => {
    const url = `${consts.API_URL}/armas`
    axios.get(url).then(resp =>
      dispatch({
        type: Actions.ARMAS_FETCHED,
        payload: resp.data.content
      })
    ).catch(e=>sendError(e))
  }
}

export function getDrogaApreendidaUnidades() {
    return dispatch => {
      const url = `${consts.API_URL}/drogaApreendidaUnidades`
      axios.get(url).then(resp =>
        dispatch({
          type: Actions.DROGA_APREENDIDA_UNIDADES_FETCHED,
          payload: resp.data
        })
      ).catch(e=>sendError(e))
    }
  }

  export function getDrogaApreendidaTipos() {
    return dispatch => {
      const url = `${consts.API_URL}/drogaApreendidaTipos`
      axios.get(url).then(resp =>
        dispatch({
          type: Actions.DROGA_APREENDIDA_TIPOS_FETCHED,
          payload: resp.data
        })
      ).catch(e=>sendError(e))
    }
  }

export function getAfastamentoTipo() {
  return dispatch => {
    const url = `${consts.API_URL}/afastamentoTipos`
    axios.get(url).then(resp => {
      dispatch({
        type: Actions.AFASTAMENTO_TIPO_FETCHED,
        payload: resp.data
      })
    }).catch(e=>sendError(e))
  }
}
