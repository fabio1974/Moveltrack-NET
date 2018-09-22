const INITIAL_STATE = { selected: '', visible: {} }

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'SUB_TAB_SELECTED':
            return { ...state, selected: action.payload }
        case 'SUB_TAB_SHOWED':
            return { ...state, visible: action.payload }
        default:
            return state
    }
}