import React, {Component} from 'react'
import Grid from '../layout/Grid'
import If from '../operator/If'
import _ from 'lodash'
import {Field} from "redux-form";
import {pad} from '../utils'

export default class LabelAndSelectMinuto extends Component {
  constructor(props) {
    super(props);
  }

  renderOptions(){
    let options = []
    for(let i=0;i<=59;i++){
        options.push(<option value={i} key={i}>{pad(2,`${i}`)}</option>)
    }
    return options



  }

  render() {
    const props = this.props

    if (_.has(props, 'meta')) {


      const {meta: {touched, error, warning}} = props;

      return (
        <Grid cols={props.cols}>
          <div className='form-group'>
            <If test={props.label}>
              <label htmlFor={props.name}>{props.label}</label>&nbsp;{touched && ((error &&
              <span className='badge badge-danger'>{error}</span>) || (warning && <span>{warning}</span>))}
            </If>

            <select {...props.input} className='form-control form-control-sm' placeholder={props.placeholder}
                    size={props.size} multiple={props.multiple}
                    disabled={props.readOnly} type={props.type}>

              {this.renderOptions()}


            </select>
          </div>
        </Grid>
      )
    }else{

      return (
        <Grid cols={props.cols}>
          <div className='form-group'>
            <If test={props.label}>
              <label htmlFor={props.name}>{props.label}xxx</label>
            </If>

            <select {...props.input} className='form-control form-control-sm' placeholder={props.placeholder} onChange={props.onChange}
                    size={props.size} multiple={props.multiple}
                    disabled={props.readOnly} type={props.type}>
              {this.renderOptions()}

            </select>
          </div>
        </Grid>
      )

    }
  }
}

