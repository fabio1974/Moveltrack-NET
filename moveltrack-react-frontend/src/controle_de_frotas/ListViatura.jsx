import React, {Component} from 'react'
import PropTypes from 'prop-types';


export default class TrackList extends Component {
  constructor(props) {
    super(props);

  }

  componentWillMount() {
  }

  componentDidMount() {
  }


  render() {
    return (
      <div className='table-responsive'>
        <table className='table table-sm table-striped table-bordered table-hover'>
          <thead>
          <tr>
            {this.renderHead()}
          </tr>
          </thead>
          <tbody>
            {this.renderRows()}
          </tbody>
        </table>
      </div>
    )
  }

  renderHead() {
    const {object,filter} = this.props.state
    return Object.keys(object).map((fieldKey, index, array) => {
      if(object[fieldKey].show)
        return (
          <th className="text-left" key={index}>
            {object[fieldKey].descryption}
            <input key={index}
                   name={`${fieldKey}filter`}
                   value={(filter || {})[fieldKey]}
                   onChange={(e) => this.props.executeFilter(fieldKey,e)}
                   className='input-xs form-control' type='text'/>
          </th>
        )
    })
  }

  renderRows() {
    const {object,pageList} = this.props.state
    return pageList.map((object, index) => (
      this.renderColumns(object)
    ))
  }

  /*object: {
    placa: {type: String, descryption: 'Placa', show: true, filter: true},
    marcaModelo: {type: String, descryption: 'Marca/Modelo', show: true, filter: true},
    chassi: {type: String, descryption: 'Chassi', show: false, filter: false},
    cor: {type: String, descryption: 'Cor', show: false, filter: false}
  }*/




  getFieldsFromObject(object) {

  }


  normalize(object) {
    if (object.pessoa)
      object = {...object, pessoa: {id: object.pessoa.id, nome: object.pessoa.nome}}
    return object
  }


  renderColumns(object) {
    return (
      <tr key={object.id}>
        <td className="text-center">
          <button type="button" onClick={(e) => this.props.rastrearViatura(e)} value={object.placa}
                  className="btn btn-primary btn-xs width-80">{object.placa}</button>
        </td>
        <td className="text-center">{object.marcaModelo}</td>
      </tr>
    )
  }

  capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
  }

  rastrear(e) {
    console.log("e", e.target.value)
  }

}


