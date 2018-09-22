import {Actions} from '../../main/Actions'



export function goAction(action, currentPage) {
    return {
        type: action,
        payload: currentPage
    }
}


export function changePageSizeAction(pageSize){
    return {
        type: Actions.CHANGE_PAGE_SIZE,
        payload: +pageSize
    }
}


export function goToPageAction(goToPage){
    return {
        type: Actions.GO_TO_PAGE,
        payload: +goToPage
    }
}
