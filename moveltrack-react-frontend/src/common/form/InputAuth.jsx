import React from   'react'
import If from '../operator/If'

export default props => (
    <If test={!props.hide}>

    <div className='form-group'>

        <div className="input-group">
            <div className="input-group-prepend">
                <span className="input-group-text">
                    <i className={props.icon}></i>
                </span>
            </div>
            <input {...props.input}
                className='form-control'
                placeholder={props.placeholder}
                readOnly={props.readOnly}
                type={props.type} />
        </div>

    </div>



    </If>
)




