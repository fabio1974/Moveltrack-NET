import React, {Component} from   'react'


import EscalaForm from './EscalaForm'
import EscalaCrud2 from './EscalaCrud2';

class EscalaCrud extends Component {

  render() {

    const escala = {
      areaOrganizacional: {type: 'AreaOrganizacional', ref: 'AreaOrganizacional', required:true},
      ano: {type: String, required: true},
      mes: {type: String, required: true}
    }



    const INITIAL_VALUES = {}

    return (
      <EscalaCrud2 object={escala}
            path={'/escalas'}
            secao='Escalas'
            titulo='Geração de Escalas'
            formComponent={EscalaForm}
            initialFormValues={INITIAL_VALUES}
            paginated={true}/>
    )
  }
}

export default EscalaCrud
