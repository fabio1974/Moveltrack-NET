import React, { Component } from   'react'
import bcrypt from   'bcryptjs'


import Crud from '../commons/Crud';
import CurriculoForm from './CurriculoForm';

class CurriculoCrud extends Component {

    render() {

        const curriculo = {
            pessoa: {type: 'Pessoa', ref: 'Pessoa', required:true},
            observacao: { type: String, required: true },
            data: { type: Date, required: true },
            endereco: { type: String, required: true },
            trabalhoAtual: { type: String, required: false },
            
         //   telefones: [{type: String}],
         //   cursos: [curso],
         //   fomacoes: [formacao],
         //   experiencias: [experiencia],
         //   idiomas: [idioma]
        }

        var salt = bcrypt.genSaltSync();
        const INITIAL_VALUES = {"data": new Date(), "pessoa":{}, "cursos":[{}], "formacoes":[{}], "experiencias":[{}]}

        return (
            <Crud  object={curriculo} 
                   path={'/curriculos'} 
                   secao='Cadastro'
                   titulo='Currículo'
                   formComponent={CurriculoForm} 
                   initialFormValues={INITIAL_VALUES}
                   paginated={true} />
        )
    }

}

export default CurriculoCrud
