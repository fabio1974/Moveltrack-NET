import _ from   'lodash'
import { toastr } from   'react-redux-toastr'

export function sendError(e) {
    if (_.has(e, 'response.data.errors')) {
        e.response.data.errors.forEach(error => toastr.error('Erro', error))
        e.response.data.errors_description.forEach(error => console.log("Erro desenvolvedor: ", error))
    } else if (_.has(e, 'response.data.error')) {
        toastr.error('Erro', e.response.data.error)
        console.log("Erro desenvolvedor: ", e.response.data.error_description)
    } else {
        toastr.error('Erro', e.message)
        console.log("Erro desenvolvedor: ",e.stack || e.message)
    }
}

export function pad(size, s) {
  while (s.length < (size || 2)) {s = "0" + s;}
  return s;
}


export function buildQueryFromFilter(filters) {
  if (Object.values(filters).length==0)
    return '';
  const fields = Object.keys(filters)
  let query = ''
  fields.forEach(field => {
    if (filters[field].length > 0)
      query = query + field + ':' + filters[field] + ','
  })
  query = '&search=' + query
  return query = query.slice(',', -1);
}

export function decodeToken(token) {
  return  JSON.parse(atob(token.split('.')[1]));
};

export function getDecodedToken(){
  const access_token = JSON.parse(localStorage.getItem("cp_access_token"))
  const decodedToken = decodeToken(access_token, { complete: true })
  return decodedToken
}
