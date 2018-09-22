import {Actions} from "../main/Actions";

const userKey = 'cp_access_token'
const INITIAL_STATE = {
    access_token: JSON.parse(localStorage.getItem(userKey)),
}



export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
         case Actions.LOGIN:
            //localStorage.setItem(userKey, JSON.stringify(action.payload.access_token))
            return { ...state, access_token: action.payload}

        case Actions.LOGOUT:
            return { ...state, access_token: null} 


        default:
            return state
    }
}
