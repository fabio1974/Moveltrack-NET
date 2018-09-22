import React, { Component } from   'react'


import AreaOrganizacionalForm from './AreaOrganizacionalForm'
import Crud from '../commons/Crud';

class AreaOrganizacionalCrud extends Component {

    render() {

        const areaOrganizacional = {
            descricao: { type: String, required: true, unique: true}, //RONDA DE AÇÕES INTENSIVAS E OSTENSIVAS 
            sigla: { type: String, required: true, uppercase: true, unique: true},     //BPRAIO
            tipo: { type: String, required: true,  uppercase: true},  //OPERACIONAL ou ADMINISTRATIVA
           // escalaTipo: { type: String, required: true, uppercase: true },  //12x36
        }


        const INITIAL_VALUES = {}

        return (
            <Crud  object={areaOrganizacional} 
                   path={'/areaOrganizacionals'} 
                   secao='Cadastro'
                   titulo='Área Organizacional'
                   formComponent={AreaOrganizacionalForm} 
                   initialFormValues={INITIAL_VALUES}
                   paginated={true} />
        )
    }

}


export default AreaOrganizacionalCrud
