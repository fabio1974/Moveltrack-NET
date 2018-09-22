import React, {Component} from 'react'
import Grid from '../layout/Grid'

export default class ButtonSelectHorizontal extends Component {


    render() {

       
        return (
            <Grid cols={this.props.cols}>

                <div className="input-group width-160"  >
                    <span className="input-group-prepend">
                        <label className={this.props.classButton} htmlFor={this.props.name}>{this.props.label}</label>
                            </span>
                    <select {...this.props.input} className='form-control form-control-sm'
                        placeholder={this.props.placeholder}
                        value={this.props.defaultValue}
                        onChange={this.props.onChange}
                        disabled={this.props.readOnly} type={this.props.type} >
                        {this.props.children}
                    </select>
                </div>
            </Grid>
        )
    }

}
