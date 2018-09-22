import React, {Component} from 'react'
import Grid from '../layout/Grid'
import If from '../operator/If'
import _ from 'lodash'
import {Field} from "redux-form";

export default class LabelAndSelectMes extends Component {
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

              <option key={0}>Escolha o Mês da Escala</option>
              <option key={1} value={1}>JANEIRO</option>
              <option key={2} value={2}>FEVEREIRO</option>
              <option key={3} value={3}>MARÇO</option>
              <option key={4} value={4}>ABRIL</option>
              <option key={5} value={5}>MAIO</option>
              <option key={6} value={6}>JUNHO</option>
              <option key={7} value={7}>JULHO</option>
              <option key={8} value={8}>AGOSTO</option>
              <option key={9} value={9}>SETEMBRO</option>
              <option key={10} value={10}>OUTUBRO</option>
              <option key={11} value={11}>NOVEMBRO</option>
              <option key={12} value={12}>DEZEMBRO</option>


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
              <option key={0}>Escolha o Mês da Escala</option>
              <option key={1} value={1}>JANEIRO</option>
              <option key={2} value={2}>FEVEREIRO</option>
              <option key={3} value={3}>MARÇO</option>
              <option key={4} value={4}>ABRIL</option>
              <option key={5} value={5}>MAIO</option>
              <option key={6} value={6}>JUNHO</option>
              <option key={7} value={7}>JULHO</option>
              <option key={8} value={8}>AGOSTO</option>
              <option key={9} value={9}>SETEMBRO</option>
              <option key={10} value={10}>OUTUBRO</option>
              <option key={11} value={11}>NOVEMBRO</option>
              <option key={12} value={12}>DEZEMBRO</option>

            </select>
          </div>
        </Grid>
      )

    }
  }
}

