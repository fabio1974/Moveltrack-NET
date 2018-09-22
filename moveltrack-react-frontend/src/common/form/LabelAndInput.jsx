import React, {Component} from 'react'
import Grid from '../layout/Grid'
import If from "../operator/If";
import _ from "lodash";


export default class LabelAndInput extends Component {
  constructor(props) {
    super(props)
  }

  render() {

    const props = this.props



    if (_.has(props, 'meta')) {
      const {meta: {touched, error, warning}} = props;
      return (
        <Grid cols={props.cols}>
          <div className='form-group'>
            <label htmlFor={props.name}>{props.label}</label>&nbsp;{touched && ((error &&
            <span className='badge badge-danger'>{error}</span>) || (warning && <span>{warning}</span>))}
              <input {...props.input} className='form-control form-control-sm' placeholder={props.placeholder}    readOnly={props.readOnly} type={props.type}/>
        </div>
        </Grid>
      )
    } else {
      return (
        <Grid cols={props.cols}>
          <div className='form-group'>
            <label htmlFor={props.name}>{props.label}</label>
              <input {...props.input} value={props.value} className='form-control form-control-sm' placeholder={props.placeholder}   readOnly={props.readOnly} type={props.type}/>
          </div>
        </Grid>
      )
    }
  }
}





