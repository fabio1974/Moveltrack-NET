import {Actions} from '../../main/Actions';

const INITIAL_STATE = {perfis:[]}

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
       

        case Actions.PERFIS_FETCHED :
            return { ...state, perfis: action.payload }

        case Actions.ESCALA_TIPOS_FETCHED :
            return { ...state, escalaTipos: action.payload }

        case Actions.AREA_ORGANIZACIONALS_FETCHED :
            return { ...state, areaOrganizacionals: action.payload }

        case Actions.OPERACAO_STATUS_FETCHED :
            return { ...state, operacaoStatuss: action.payload }

      case Actions.AFASTAMENTO_TIPO_FETCHED :
        return { ...state, afastamentoTipos: action.payload }

        case Actions.DROGA_APREENDIDA_UNIDADES_FETCHED :
            return { ...state, drogaApreendidaUnidades: action.payload }

        case Actions.DROGA_APREENDIDA_TIPOS_FETCHED :
            return { ...state, drogaApreendidaTipos: action.payload }



        default:
            return state
    }
}
