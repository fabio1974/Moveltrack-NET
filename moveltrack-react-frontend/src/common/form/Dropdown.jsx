import React, {Component} from 'react'
import Grid from '../layout/Grid'

export default class Dropdown extends Component {



    render() {

        const { props } = this


        return (
            <Grid cols={props.cols}>
                <div className='form-group'>
                    <label htmlFor={props.name}>{props.label}</label>
                    <select {...props.input}
                        className='form-control form-control-sm'
                        placeholder={props.placeholder}
                        value={props.defaultValue}
                        disabled={props.readOnly}
                        type={props.type} >
                        {props.children}
                    </select>
                </div>
            </Grid>
        )
    }
}
