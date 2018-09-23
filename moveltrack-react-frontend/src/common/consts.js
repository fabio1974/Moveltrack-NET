let HOST = 'localhost'

if (!process.env.NODE_ENV || process.env.NODE_ENV === 'development') {
    HOST = 'localhost'
    //HOST = '138.197.22.183' Yuri's enviroment
}else{
    HOST = '138.197.22.183'
}    



export default {
    API_URL: `http://${HOST}:8090/api`,
    OAPI_URL: `http://${HOST}:8090`
}
