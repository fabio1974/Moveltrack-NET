import {Actions} from '../../main/Actions';
const INITIAL_STATE = {path:'/',list: [], filters:{}, optionState:{}}



export default (state = INITIAL_STATE, actions) => {




    switch (actions.type) {

        case Actions.BUILD_LIST:{
            return { ...state, list: actions.payload}
        }    

/*         case Actions.SET_INITIAL_STATE:{
            return { ...state, state: actions.payload}
        }
 */
        case Actions.ADD_FILTER:{
            return { ...state, filters: actions.payload}
        }

        case Actions.SET_PATH:{

            return { ...state, path:actions.payload}
        } 

        case Actions.SET_OPTION_STATE:{
            return { ...state, optionState: actions.payload}
        } 


        default:{
            return state
        }    
    }
}


