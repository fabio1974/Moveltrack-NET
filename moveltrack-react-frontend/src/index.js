import './polyfill'
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import AuthOrApp from './main/AuthOrApp';
import App from './App';
// disable ServiceWorker
// import registerServiceWorker from './registerServiceWorker';

import { applyMiddleware, createStore } from 'redux'
import { Provider } from 'react-redux'
import {BrowserRouter,Route,Link,Switch,Redirect} from 'react-router-dom'
import promise from 'redux-promise'
import multi from 'redux-multi'
import thunk from 'redux-thunk'
import logger from 'redux-logger'

import reducers from './main/reducers'


const devTools = window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
const store = applyMiddleware(multi, thunk, promise)(createStore)(reducers,devTools) 

ReactDOM.render(<Provider store={store}>
            <AuthOrApp />
</Provider> , document.getElementById('root'));
