import React, { Component } from   'react'
import PropTypes from   'prop-types'; // ES6
import { bindActionCreators } from 'redux'
import { connect } from   'react-redux'
import {  getPageList } from './listAction'
import { showUpdateFormAction, showDeleteFormAction, addFilterAction, insereCurriculoPessoaAction  } from './crudActions'
import Moment from 'react-moment'
import {withRouter} from   'react-router-dom'
import If from '../../common/operator/If'
import { LOCATION_CHANGE } from   'react-router-redux'


class List extends Component {
    constructor(props) {
        super(props);

    }

    componentWillMount() {
       
    }


    render() {
        return (
            <div className='table-responsive'>
                <table className='table table-sm table-striped table-bordered table-hover'>
                    <thead>
                        <tr>
                            {this.renderHead()}
                            <th className='table-actions on-bottom text-center'>
                                <button onClick={() => this.props.getPageList()} className="btn btn-sm btn-primary">Filtrar</button>
                            </th>
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
        const { filters } = this.props
        const fields = this.props.fields || []
        const objectName = this.props.path.substring(1)
        return fields.map((field, index) => (
            <th className="text-left" key={index}>
                {this.capitalizeFirstLetter(field)}
                <input key={index}
                    name={`${field}filter`}
                    value={(filters[objectName] || {})[field]}
                    onChange={(e) => this.addFieldToFilter(e, field)}
                    className='input-xs form-control' type='text' />
            </th>
        ))
    }



    getFieldsFromObject() {
        const fields = Object.keys(this.props.object)
        if (this.props.onlyRequireds) {
            var riquiredFields = []
            fields.map(field => {
                if (this.props.object[field].required)
                    this.props.riquiredFields.push(field)
            });
            return this.props.requiredFields
        } else
            return fields
    }


    addFieldToFilter(e, field) {
        const objectName = this.props.path.substring(1)
        const { filters } = this.props
        const anterior = filters[objectName] || {}
        const clone = { ...filters, [objectName]: { ...anterior, [field]: e.target.value } }
        this.props.addFilterAction(clone)
    }


    renderRows() {
        const list = this.props.list || []
        const objectName = this.props.path.substring(1)
        return list.map((object, index) => (

            


            <tr key={object.id} >
                {this.renderColumns(object)}
                
                <td className='text-center'>
                    <button title='Visualizar Escala' className='btn btn-warning btn-xs' onClick={() => this.props.showUpdateFormAction(object)}>
                        <i className={`fa fa-${objectName === 'escalas'?'search':'pencil'}`}></i>
                    </button>
                    <button className='btn btn-danger btn-xs' onClick={() => this.props.showDeleteFormAction(object)}>
                        <i className='fa fa-trash-o'></i>
                    </button>

                    <If test={objectName === 'pessoas'}>
                        <button className='btn btn-warning btn-xs' onClick={() => this.props.insereCurriculoPessoaAction(object,this.props.history)}>
                            <i className='fa fa-file-o'></i>
                        </button>
                    </If>

                  <If test={objectName === 'viaturas'}>
                    <button className='btn btn-warning btn-xs' onClick={() => this.props.trackViatura(object)}>
                      <i className='fa fa-map-marker '></i>
                    </button>
                  </If>



                </td>
            </tr>
        ))
    }



    normalize(object){
        if(object.pessoa)
        object = {...object,pessoa:{id:object.pessoa.id,nome:object.pessoa.nome}}
        
        return object
    }



    renderColumns(object) {
        const fields = this.props.fields || []

        return fields.map((field, index) => {

            const isDate = this.props.object[field].type.name === 'Date' || false
            const dateStr = object[field]
            if (isDate) {
                object[field] = new Date(object[field])
            }

            if (isDate)
                return (
                    <td className="text-center" key={index}>
                        <Moment format="DD/MM/YY mm:ss">{object[field]}</Moment>
                    </td>
                )

            
            else if(this.props.object[field].type === 'Pessoa'){
                return (
                <td className="text-center" key={index}>
                    {object[field]?object[field].nome:''}
                </td>
                )
            }

            else if(this.props.object[field].type === 'Chip'){
              return (
                <td className="text-center" key={index}>
                  {object[field]?object[field].iccid :''}
                </td>
              )
            }


            else if(this.props.object[field].type === 'AreaOrganizacional'){
            return (
              <td className="text-center" key={index}>
                {object[field]?object[field].descricao:''}
              </td>
            )

          }else


            return (
                <td className="text-center" key={index}>
                    {object[field]}
                </td>
            )
        })
    }

    capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    }


}



const mapStateToProps = state => ({ list: state.crudReducer.list, filters: state.crudReducer.filters })
const mapDispatchToProps = dispatch => bindActionCreators({  
    showUpdateFormAction, 
    showDeleteFormAction,
    getPageList,
    addFilterAction,
    insereCurriculoPessoaAction  
}, dispatch)

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(List))



List.propTypes = {
    fields: PropTypes.array.isRequired,
    path: PropTypes.string.isRequired
};

/* export function mapBindActionCreators(actions, dispatch) {
    return actions.map(action => bindActionCreator(action, dispatch))
} */
