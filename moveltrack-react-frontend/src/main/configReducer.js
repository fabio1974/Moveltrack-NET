import {Actions} from "./Actions";

const INITIAL_STATE = {sideBarMinimized:false}



export default (state = INITIAL_STATE, actions) => {




    switch (actions.type) {

        case Actions.SIDEBAR_MINIMIZED:{
            return { ...state, sideBarMinimized: actions.payload, sideBarHidden: !actions.payload}
        }

      default:{
            return state
        }    
    }
}


