import { combineReducers } from 'redux'
import { reducer as formReducer } from 'redux-form'
import { reducer as toastrReducer } from 'react-redux-toastr'

import DashboardReducer from '../dashboard/dashboardReducer'
import TabReducer from '../common/tab/tabReducer'
import SubTabReducer from '../common/tab/subTabReducer'
import AuthReducer from '../auth/authReducer'
import CrudReducer from '../cruds/commons/crudReducer'
import PaginatorReducer from '../common/paginator/paginatorReducer'
import TabelaApoioReducer from '../cruds/commons/tabelaApoioReducer'
import HistoryReducer from '../cruds/commons/historyReducer'
import configReducer from "./configReducer";



const rootReducer = combineReducers({
    configReducer: configReducer,
    dashboard: DashboardReducer,
    tab: TabReducer,
    subTab: SubTabReducer,
    form: formReducer,
    toastr: toastrReducer,
    auth: AuthReducer,
    crudReducer: CrudReducer,
    tabelaApoioReducer: TabelaApoioReducer,
    paginatorReducer: PaginatorReducer,
    historyReducer: HistoryReducer
})

export default rootReducer
