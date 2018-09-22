import axios from 'axios'
import consts from '../../common/consts'
import {Actions} from '../../main/Actions'
import {sendError} from './../../common/utils'
import _ from 'lodash'
export function getPageList() {

    return (dispatch, getState) => {

        const path = getState().crudReducer.path

        console.log("PATH2",path)

        const objectName = path.substring(1)
        const filters = getState().crudReducer.filters[objectName] || {}

        const query = buildQueryFromFilters(filters)
        const pageSize = getState().paginatorReducer.pageSize
        const currentPage = getState().paginatorReducer.currentPage

        const url = `${consts.API_URL}${path}?page=${currentPage}&size=${pageSize}${query}`

        console.log("URL",url)

        axios.get(url)
            .then(resp => {
                const count = resp.data.totalElements
                const lastPage = resp.data.totalPages - 1
                const list = resp.data.content
                if (lastPage < currentPage) {
                    dispatch({
                        type: Actions.GO_FIRST,
                        payload: currentPage - 1
                    })
                }
                dispatch({
                    type: Actions.BUILD_LIST,
                    payload: list
                })

                dispatch({
                    type: Actions.SET_COUNT_LAST_PAGE,
                    payload: { count, lastPage }
                })
            })
            .catch(e => {

                dispatch({
                    type: Actions.BUILD_LIST,
                    payload: []
                })

                dispatch({
                    type: Actions.SET_COUNT_LAST_PAGE,
                    payload: { count: 0, lastPage: 0 }
                })
                sendError(e)
            })
    }
}



function buildQueryFromFilters(filters) {

    if (Object.values(filters).length==0)
        return '';
    const fields = Object.keys(filters)
    let query = ''
    fields.map(field => {
        if (filters[field].length > 0)
            query = query + field + ':' + filters[field] + ','
    })

    query = '&search=' + query
    return query = query.slice(',', -1);
}
