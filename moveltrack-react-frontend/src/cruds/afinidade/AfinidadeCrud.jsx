import React, { Component } from   'react'


import AfinidadeForm from './AfinidadeForm'
import Crud from '../commons/Crud';

class AfinidadeCrud extends Component {

  render() {

    const afinidade = {
      descricao: { type: String, required: true },
      agente: { type: String, required: true },
      pessoas: { type: String, required: true} // TODO: Consultar tipos para ObjectID
    }

    const INITIAL_VALUES = {}

    return (
      <Crud  object={afinidade}
             path={'/afinidades'}
             formComponent={AfinidadeForm}
             initialFormValues={INITIAL_VALUES}
             paginated={true} />
    )
  }

}


export default AfinidadeCrud
