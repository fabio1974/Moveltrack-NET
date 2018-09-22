import React, {Component} from 'react'
import Grid from '../layout/Grid'

export default class ButtonInputTextHorizontal extends Component {

	render() {
		

		return (
			<Grid cols={this.props.cols} >
				<div className="input-group input-group-sm width-160"  >
					<div className="input-group-prepend">
						<label className="btn  btn-primary btn-sm" >Ir para Página</label>
					</div>
					<input {...this.props.input} name={this.props.name} value={this.props.defaultValue} className="form-control form-control-sm input-sx" onChange={this.props.onChange} type="text" />
				</div>
			</Grid>
		)
	}
}


