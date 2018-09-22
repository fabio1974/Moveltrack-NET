import React, { Component } from   'react'
import bcrypt from   'bcryptjs'

import PessoaForm from './PessoaForm'
import Crud from '../commons/Crud';

class PessoaCrud extends Component {

    render() {

        const pessoa = {
            nome: { type: String, required: true },
            cpf: { type: String, required: true },
            matricula: { type: String, required: true },
        }

        var salt = bcrypt.genSaltSync();
        const INITIAL_VALUES = {"senha":bcrypt.hashSync('1234', salt), "ativo":false}

        return (
            <Crud  object={pessoa} 
                   path={'/pessoas'} 
                   secao='Cadastro'
                   titulo='Pessoas'
                   formComponent={PessoaForm} 
                   initialFormValues={INITIAL_VALUES}
                   paginated={true} />
        )
    }

}

export default PessoaCrud
