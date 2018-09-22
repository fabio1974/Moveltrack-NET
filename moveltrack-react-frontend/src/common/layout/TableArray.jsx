import React, {Component} from 'react'
import axios from "axios";
import consts from "../consts";
import {sendError} from "../utils";
import Grid from "./Grid";
import If from "../operator/If";
import _ from "lodash";


export default class OBDSensores extends Component {
  constructor(props) {
    super(props);
  }



  render() {
    return (
      <Grid className='table-responsive' col="12 6" >
        <If test={_.has(this.props,'label')}>
          <label htmlFor="tableArray">{this.props.label}</label>
        </If>
        <table id="tableArray"  className='table table-sm table-striped table-bordered table-hover'>
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
      return <tr>{this.renderHeaderColumns()}</tr>
  }

  renderHeaderColumns(){
    const {list} = this.props
    const obj = list[0]
    const keys = Object.keys(obj);
    return keys.map((key, index) => {
      if (index>0) //exclui o Id
        return (
          <th key={index} className="text-center">{key}</th>
        )
    })
  }

  renderRows() {
    const {list} = this.props
    return list.map((obj, index) => {
      return (
      <tr key={obj.id}>{this.renderRowColumns(obj)}</tr>
    )})
  }


  renderRowColumns(obj){
    return Object.values(obj).map((value,index) => {
      if (index>0) //exclui o Id
        return(
          <td key={index} className="text-center">{value}</td>
        )
    })
  }




  capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
  }


}


