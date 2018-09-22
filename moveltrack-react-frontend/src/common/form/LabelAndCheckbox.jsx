import React from   'react'
import Grid from '../layout/Grid'

export default props => (
    <Grid cols={props.cols}>
        <div className='form-group'>
            <input {...props.input} className='form-check-input'
                 placeholder={props.placeholder}
                 disabled={props.readOnly} type='checkbox'>
            </input>&nbsp;     
            <label htmlFor={props.name}>{props.label}</label>
        </div>
    </Grid>
)
