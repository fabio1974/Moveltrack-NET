import React from 'react'
import Grid from '../layout/Grid'

export default props => (
    <Grid cols={props.cols}>
        <div className='form-group'>
            <table>
                <tbody>
                    <tr><td ><label className="btn btn-primary" htmlFor={props.name}>{props.label}</label>&nbsp;&nbsp;</td>

                        <td >

                            <select {...props.input} className='form-control form-control-sm'
                                placeholder={props.placeholder}
                                value={props.defaultValue}
                                onChange={props.onChange}
                                disabled={props.readOnly} type={props.type} >
                                {props.children}
                            </select>

                        </td>

                    </tr>
                </tbody>
            </table>

        </div>
    </Grid>
)
