import React, {Component} from 'react'
import Crud from '../commons/Crud';
import RastreadorForm from "./RastreadorForm";

class RastreadorCrud extends Component {

  render() {

    const obj = {
      imei: {type: String, required: true},
      rastreadorTipo:{type: String, required:true},
      chip: {type: 'Chip', required: true},
    }


    const INITIAL_VALUES = {}

    return (
      <Crud object={obj}
            path={'/rastreadors'}
            formComponent={RastreadorForm}
            initialFormValues={INITIAL_VALUES}
            paginated={true}/>
    )
  }

}


export default RastreadorCrud
