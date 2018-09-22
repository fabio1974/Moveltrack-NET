import React, {Component} from 'react'
import ChipForm from './ChipForm'
import Crud from '../commons/Crud';

class ChipCrud extends Component {

  render() {

    const chip = {
      numero: {type: String, required: true},
      iccid: {type: String, required: true},
      operadora: {type: String, required: true},
    }


    const INITIAL_VALUES = {}

    return (
      <Crud object={chip}
            path={'/chips'}
            formComponent={ChipForm}
            initialFormValues={INITIAL_VALUES}
            paginated={true}/>
    )
  }

}


export default ChipCrud
