import React, { Component } from   'react'


import EscalaTipoForm from './EscalaTipoForm'
import Crud from '../commons/Crud';

class EscalaTipoCrud extends Component {

    render() {

        const escalaTipo = {
            descricao: {type: String, required: true},
        }


        const INITIAL_VALUES = {}

        return (
            <Crud  object={escalaTipo} 
                   path={'/escalaTipos'} 
                   secao='Cadastro'
                   titulo='Modelo de Escalas'
                   formComponent={EscalaTipoForm} 
                   initialFormValues={INITIAL_VALUES}
                   paginated={true} />
        )
    }
}


export default EscalaTipoCrud
