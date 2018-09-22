import React, {Component} from 'react'
import ArmaForm from './ArmaForm'
import Crud from '../commons/Crud';

class ArmaCrud extends Component {

  render() {

    const arma = {
      serial: {type: String, required: true},
      registro: {type: String, required: true},
      fabricante: {type: String, required: true},
      modelo: {type: String, required: true},
      calibre: {type: String, required: true},
    }


    const INITIAL_VALUES = {}

    return (
      <Crud object={arma}
            path={'/armas'}
            formComponent={ArmaForm}
            initialFormValues={INITIAL_VALUES}
            paginated={true}/>
    )
  }

}


export default ArmaCrud
