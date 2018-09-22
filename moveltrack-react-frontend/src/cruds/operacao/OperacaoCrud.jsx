import React, { Component } from   'react'


import OperacaoForm from './OperacaoForm'
import Crud from '../commons/Crud';

class OperacaoCrud extends Component {

    render() {

        const operacao = {
            nome: { type: String, required: true },
            local: { type: String, required: true },
            inicio: { type: Date, required: true },
            fim: { type: Date, required: true },
            status: { type: String, required: true, uppercase: true },  //AGENDADA, EM_EXECUCAO, ENCERRADA, CANCELADA
        }


        const INITIAL_VALUES = {}

        return (
            <Crud  object={operacao} 
                   path={'/operacaos'} 
                   secao=''
                   titulo='Operações'
                   formComponent={OperacaoForm} 
                   initialFormValues={INITIAL_VALUES}
                   paginated={true} />
        )
    }

}


export default OperacaoCrud
