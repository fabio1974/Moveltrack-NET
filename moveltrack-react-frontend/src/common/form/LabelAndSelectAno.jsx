import React, {Component} from 'react'
import Grid from '../layout/Grid'
import If from '../operator/If'
import _ from 'lodash'
import {Field} from "redux-form";

export default class LabelAndSelectAno extends Component {
  constructor(props) {
    super(props);
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

              <option key={0}>Escolha o Ano</option>
              <option key={2018} value={2018}>2018</option>
              <option key={2019} value={2019}>2019</option>
              <option key={2020} value={2020}>2020</option>
              <option key={2021} value={2021}>2021</option>
              <option key={2022} value={2022}>2022</option>
              <option key={2023} value={2023}>2023</option>
              <option key={2024} value={2024}>2024</option>
              <option key={2025} value={2025}>2025</option>
              <option key={2026} value={2026}>2026</option>
              <option key={2027} value={2027}>2027</option>
              <option key={2028} value={2028}>2028</option>
              <option key={2029} value={2029}>2029</option>
              <option key={2030} value={2030}>2030</option>


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
              <option key={0}>Escolha o Ano</option>
              <option key={2018} value={2018}>2018</option>
              <option key={2019} value={2019}>2019</option>
              <option key={2020} value={2020}>2020</option>
              <option key={2021} value={2021}>2021</option>
              <option key={2022} value={2022}>2022</option>
              <option key={2023} value={2023}>2023</option>
              <option key={2024} value={2024}>2024</option>
              <option key={2025} value={2025}>2025</option>
              <option key={2026} value={2026}>2026</option>
              <option key={2027} value={2027}>2027</option>
              <option key={2028} value={2028}>2028</option>
              <option key={2029} value={2029}>2029</option>
              <option key={2030} value={2030}>2030</option>

            </select>
          </div>
        </Grid>
      )

    }
  }
}

