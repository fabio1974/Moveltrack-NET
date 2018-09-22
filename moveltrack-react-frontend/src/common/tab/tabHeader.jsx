import React, { Component } from   'react'
import { bindActionCreators } from 'redux'
import { connect } from   'react-redux'

import If from '../operator/If'
import { selectTabAction } from './tabActions'

class TabHeader extends Component {
    render() {
        const selected = this.props.tab.selected === this.props.target
        const visible = this.props.tab.visible[this.props.target]
        return (
            <If test={visible}>
                <li className='nav-item'> 
                    <a href='javascript:;' 
                        className={selected ? 'active nav-link' : 'nav-link'}
                        data-toggle='tab'
                        onClick={() => this.props.selectTabAction(this.props.target)}
                        data-target={this.props.target}>
                        <i className={`fa fa-${this.props.icon}`}></i> {this.props.label}
                    </a> 
                </li> 
            </If>
        )
    }
}

const mapStateToProps = state => ({tab : state.tab})
const mapDispatchToProps = dispatch => bindActionCreators({selectTabAction}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(TabHeader)