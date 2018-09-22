import React, { Component } from   'react'
import { bindActionCreators } from 'redux'
import { connect } from   'react-redux'
import If from '../operator/If'

class SubTabContent extends Component {
    render() {
        const selected = this.props.subTab.selected === this.props.id
        const visible = this.props.subTab.visible[this.props.id]
        return (
            <If test={visible}>
                <div id={this.props.id}
                    className={`tab-pane  ${selected ? 'active' : ''}`}> 
                    {this.props.children}
                </div> 
            </If>
        )
    }
}

const mapStateToProps = state => ({subTab: state.subTab})
export default connect(mapStateToProps)(SubTabContent)