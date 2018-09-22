import React, {Component, PropTypes} from 'react'
import Select from 'react-select'
import Grid from '../layout/Grid'
import consts from '../consts'
import 'react-select/dist/react-select.css'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import {setOptionState} from '../../cruds/commons/crudActions'
import If from '../operator/If';
import axios from 'axios'
import {sendError} from '../utils';

class SelectInputAsync extends Component {
    constructor(props) {
        super(props)
    }

   

    componentWillMount() {
        this.props.setOptionState({isLoading:false,value:this.props.input.value})
    }

    getOptions(searchText) {

        if (!searchText || searchText.length < 3) {
            const options = []
            return Promise.resolve({ options: options });
        }

        const path = this.props.path
        const query = this.props.objectLabelProperty + ':' + searchText 
        const url = `${consts.API_URL}${path}/?search=${query}`
        console.log(url)


        return axios(url)
            .then((response) => {
                return response.data.content;
            }).then((json) => {
                var options = [];
                json.forEach(element => {
                    options.push({
                    [this.props.objectLabelProperty]: `${element[this.props.objectLabelProperty]}${this.props.objectLabelProperty2?('-'+element[this.props.objectLabelProperty2]):''}`,
                        id: element['id']
                    })
                });
                return { options: options, complete:true};
            }).catch(e =>{
                sendError(e)
            })
    }


    onChange(event) {
        this.props.setOptionState({isLoading:false,value:event})
        this.props.input.onChange(event);
    }





  /*   filterOptions(options, filter, currentValues) {
        return options;
    } */

    render() {
        return (
            <Grid cols={this.props.cols}>
                <div className='form-group'>

                    <If test={this.props.label}>
                        <label htmlFor={this.props.name}>{this.props.label}</label>
                    </If>
                
                    <Select.Async {...this.props} className={this.props.className}
                        value={this.props.optionState.value || {}}
                        onChange={this.onChange.bind(this)}
                       
                        loadOptions={this.getOptions.bind(this)}
                        filterOption={() => true}
                        disabled={this.props.readOnly}
                        autoload={false}
                        clearable={true}
                        minimumInput={3}
                        cache={false}
                        disableCache={true}
                        isLoading={true}
                        labelKey={this.props.objectLabelProperty}
                        valueKey={'id'}
                    />
                </div>
            </Grid>
        );
    }
}

const mapStateToProps = state => ({ optionState: state.crudReducer.optionState })
const mapDispatchToProps = dispatch => bindActionCreators({ setOptionState }, dispatch)
export default connect(mapStateToProps,mapDispatchToProps)(SelectInputAsync)

