import React, { Component } from   'react'
import { bindActionCreators } from 'redux'
import { connect } from   'react-redux'

import If from '../operator/If'
import { selectSubTabAction } from './subTabActions'

class SubTabHeader extends Component {
    render() {
        const selected = this.props.subTab.selected === this.props.target
        const visible = this.props.subTab.visible[this.props.target]
        return (
            <If test={visible}>
                <li className='nav-item'> 
                    <a href='javascript:;' 
                        className={selected ? 'active nav-link' : 'nav-link'}
                        data-toggle='tab'
                        onClick={() => this.props.selectSubTabAction(this.props.target)}
                        data-target={this.props.target}>
                        <i className={this.props.icon}></i> {this.props.label}
                    </a> 
                </li> 
            </If>
        )
    }
}

const mapStateToProps = state => ({subTab : state.subTab})
const mapDispatchToProps = dispatch => bindActionCreators({selectSubTabAction}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(SubTabHeader)