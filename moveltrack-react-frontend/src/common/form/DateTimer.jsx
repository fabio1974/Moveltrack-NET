import React, { Component } from   'react'
import Datetime from 'react-datetime'
import Grid from '../layout/Grid'
import If from '../operator/If'
import _ from "lodash";
var moment = require('moment');
require('moment/locale/pt-br');

class DateTimer extends Component {

    constructor(props) {
        super(props)
    }

    render() {
      const props = this.props
      let date = null
      if (props.input && props.input.value && props.input.value.__proto__.constructor.name === 'Date')
        date = props.input.value
      else {
        try {
          date = new Date(props.input.value)
        } catch (err) {
          date = null
        } finally {
          date = null
        }
      }



      if (_.has(props, 'meta')) {

        const {meta: {touched, error, warning}} = props;

        return (
          <Grid cols={props.cols}>
            <div className='form-group'>
              <If test={props.label}>
                <label htmlFor={props.name}>{props.label}</label>&nbsp;{touched && ((error &&
                <span className='badge badge-danger'>{error}</span>) || (warning && <span>{warning}</span>))}
              </If>
              <Datetime className={props.className}  {...props.input} locale='pt-br' value={date}
                        onChange={param => props.input.onChange(param)}
                        inputProps={{placeholder: props.placeholder, disabled: props.readOnly, name: props.name}}/>
            </div>
          </Grid>
        )
      } else {
        return (
          <Grid cols={props.cols} className="margin-0 padding-0">
              <If test={props.label}>
                <label htmlFor={props.name}>{props.label}</label>
              </If>
              <Datetime className={props.className}  {...props.input} locale='pt-br' value={props.value} onChange={props.onChange}

                        inputProps={{placeholder: props.placeholder, disabled: props.readOnly, name: props.name}}/>
          </Grid>
        )
      }
    }
}

export default DateTimer






