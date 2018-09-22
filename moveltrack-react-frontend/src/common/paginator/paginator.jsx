import React, { Component, Children } from   'react'
import { bindActionCreators } from 'redux'
import { connect } from   'react-redux'
import PaginatorBar from './PaginatorBar'


class Paginator extends Component {

    constructor(props) {
		super(props)
    }
    
    
    componentWillMount() {
    }


    goFirst() {
	}


    render() {
        const {children: child} = this.props;    
        return (
            <div >
                {React.cloneElement(Children.only(child),{paginated: true})}
                <div className="h-divider"/>
                <PaginatorBar hideA={this.props.hideA} hideB={this.props.hideB} hideC={this.props.hideC} hideD={this.props.hideD} path={this.props.path} />
            </div>
        )
    }
}


const mapStateToProps = state => (
	{
		pageSize: state.paginatorReducer.pageSize, 
	 	currentPage: state.paginatorReducer.currentPage, 
		pageToGo: state.paginatorReducer.pageSize, 
		lastPage:state.paginatorReducer.lastPage, 
		count: state.paginatorReducer.count   
	})



export default connect(mapStateToProps,null)(Paginator)
