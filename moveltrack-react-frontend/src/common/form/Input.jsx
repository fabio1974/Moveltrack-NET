import React from   'react'
import Grid from '../layout/Grid'

export default props => (
    <Grid cols={props.cols}>
    <input {...props.input}
        className='form-control form-control-sm'
        placeholder={props.placeholder}
        readOnly={props.readOnly}
        type={props.type} />
    </Grid>    
)
