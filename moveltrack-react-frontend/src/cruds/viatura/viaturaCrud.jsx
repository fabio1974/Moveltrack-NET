import React, { Component } from   'react'


import ViaturaForm from './viaturaForm'
import Crud from '../commons/Crud';

class ViaturaCrud extends Component {

    render() {

        const viatura = {
            placa: { type: String, required: true },
            marcaModelo: { type: String, required: true },
            chassi: { type: String, required: true },
            cor: { type: String, required: true }
        }

        const initialFormValues = {data: new Date().toJson}

        return (

            <Crud  object={viatura} 
                   path={'/viaturas'} 
                   secao='Cadastro'
                   titulo='Viaturas'
                   formComponent={ViaturaForm} 
                   initialFormValues= {initialFormValues}
                   paginated={true} />
        )
    }
}

export default ViaturaCrud
