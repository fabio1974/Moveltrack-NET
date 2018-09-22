import {Actions} from '../../main/Actions'

const INITIAL_STATE = {pageSize:5,currentPage:0,pageToGo:1,lastPage:0,count:0}

export default (state = INITIAL_STATE, action) => {
    
    switch (action.type) {
        

        case Actions.GO_FIRST:
        case Actions.GO_NEXT:        
        case Actions.GO_PREVIOUS:
        case Actions.GO_LAST:
        case Actions.GO:

            return { ...state,currentPage: action.payload}

         case Actions.CHANGE_PAGE_SIZE:{
            return { ...state,pageSize:action.payload,currentPage:0}
        }    

        case Actions.SET_COUNT_LAST_PAGE:
            return { ...state, count: action.payload.count, lastPage: action.payload.lastPage}

        case Actions.FILTERING:
            return { ...state, page:action.payload.page, currentPage: action.payload.currentPage, count: action.payload.data }

        case Actions.GO_TO_PAGE:{
            return { ...state, pageTogo:action.payload,currentPage: action.payload}
        }

   

        default:
            return state
    }
}
