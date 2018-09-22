import React, {Component} from 'react'
import axios from "axios";
import consts from "../consts";
import {sendError} from "../utils";
import Grid from "./Grid";
import If from './../operator/If'
import _ from 'lodash'

export default class OBDSensores extends Component {
  constructor(props) {
    super(props);
  }



  render() {


    return (
      <Grid  col="12 6" >
        <If test={_.has(this.props,'label')}>
        <label htmlFor="tableObject">{this.props.label}</label>
        </If>
        <table id="tableObject" className='table table-sm table-striped table-bordered table-hover'>
          <thead>
            {this.renderHeader()}
          </thead>
          <tbody>
            {this.renderRows()}
          </tbody>
        </table>
      </Grid>
    )
  }

  renderHeader() {
      return <tr><th key={0} className="text-center">Campo</th><th key={1} className="text-center">Valor</th></tr>
  }

  renderRows() {
    const {object} = this.props
    const keys = Object.keys(object)
    return keys.map((key, index) => {
      return (
      <tr key={index}>
        <td  className="text-center">{key}</td>
        <td  className="text-center">{object[key]}</td>
      </tr>
    )})
  }


  capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
  }

}



