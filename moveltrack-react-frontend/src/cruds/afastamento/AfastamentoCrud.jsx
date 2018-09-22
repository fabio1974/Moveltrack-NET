import React, {Component} from 'react'
import AfastamentoForm from './AfastamentoForm'
import Crud from '../commons/Crud';
import Pessoas from "../operacao/Pessoas";


class AfastamentoCrud extends Component {

  render() {

    const afastamento = {
      tipo: {type: String, required: true},
      pessoa: {type: 'Pessoa', ref: 'Ressoa', required: true},
      inicio: {type: Date, required: true},
      fim: {type: Date, required: true}
    }

    const INITIAL_VALUES = {}

    return (
      <Crud object={afastamento}
            path={'/afastamentos'}
            formComponent={AfastamentoForm}
            initialFormValues={INITIAL_VALUES}
            paginated={true}
      />
    )

  }

}
export default AfastamentoCrud
