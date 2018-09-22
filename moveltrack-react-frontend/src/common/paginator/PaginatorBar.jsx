import React, {Component} from 'react'
import Grid from '../layout/Grid'
import {goAction, goToPageAction, changePageSizeAction} from './paginatorActions'
import {getPageList} from '../../cruds/commons/listAction'


import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import {Actions} from '../../main/Actions'
import Row from '../layout/Row'
import ButtonInputTextHorizontal from '../form/ButtonInputTextHorizontal';
import ButtonSelectHorizontal from '../form/ButtonSelectHorizontal';
import If from '../operator/If'

class PaginatorBar extends Component {

  constructor(props) {
    super(props)

    this.changePageSize = this.changePageSize.bind(this);
    this.goFirst = this.goFirst.bind(this);
    this.goNext = this.goNext.bind(this);
    this.goLast = this.goLast.bind(this);
    this.goPrevious = this.goPrevious.bind(this);
    this.goToPage = this.goToPage.bind(this);

  }


  goFirst() {
    const {pageSize, currentPage, goAction, path, getPageList} = this.props
    if (currentPage == 0) return
    goAction(Actions.GO_FIRST, 0)
    getPageList(path, pageSize, 0)
  }


  goNext() {
    const {pageSize, currentPage, goAction, lastPage, path, getPageList} = this.props;
    if (currentPage == lastPage) return
    const currenPageLocal = currentPage + 1
    goAction(Actions.GO_NEXT, currenPageLocal)
    getPageList(path, pageSize, currenPageLocal)
  }

  goPrevious() {
    const {pageSize, currentPage, goAction, path, getPageList} = this.props;
    if (currentPage == 0) return
    const currenPageLocal = currentPage - 1
    goAction(Actions.GO_PREVIOUS, currenPageLocal)
    getPageList(path, pageSize, currenPageLocal)
  }

  goLast() {
    const {pageSize, currentPage, lastPage, goAction, path, getPageList} = this.props
    if (currentPage == lastPage) return
    const currenPageLocal = lastPage
    goAction(Actions.GO_LAST, currenPageLocal)
    getPageList(path, pageSize, currenPageLocal)
  }


  goToPage(e) {
    const {pageSize, lastPage, goAction, goToPageAction, path, getPageList} = this.props
    const newPage = e.target.value - 1
    if (newPage < 0) {
      goToPageAction(0)
      e.target.value = ''
      return
    } else if (newPage > lastPage) {
      e.target.value = ''
      goToPageAction(lastPage)
      return
    }
    goAction(Actions.GO_TO_PAGE, newPage)
    getPageList(path, pageSize, newPage)
  }


  changePageSize(e) {
    const {changePageSizeAction, getPageList} = this.props
    const pageSize = e.target.value
    changePageSizeAction(pageSize)
    goAction(Actions.GO, 0)
    getPageList()
  }

  render() {
    return (
      <Row>

        <If test={!this.props.hideA}>
          <Grid cols={this.props.hideB ? '12 6 6 6' : '12 3 3 3'}>
            <nav className="" aria-label="pagination">
              <ul className="pagination pagination-sm">
                <li className="page-item">
                  <button onClick={this.goFirst} className="page-link"><i className="fa fa-fast-backward"/></button>
                </li>
                <li className="page-item">
                  <button onClick={this.goPrevious} className="page-link"><i className="fa fa-step-backward"/></button>
                </li>
                <li className="page-item">
                  <button onClick={this.goNext} className="page-link"><i className="fa fa-step-forward"/></button>
                </li>
                <li className="page-item">
                  <button onClick={this.goLast} className="page-link"><i className="fa fa-fast-forward"/></button>
                </li>
              </ul>
            </nav>
          </Grid>
        </If>

        <If test={!this.props.hideB}>
          <Grid cols='12 3 3 3'>
            <ButtonInputTextHorizontal name='pageToGo' onChange={this.goToPage} onClick={this.goToPage}
                                       label="Ir Página" classButton='btn btn-sm btn-primary'
                                       classInput=' input-sm width-40'/>
          </Grid>
        </If>


        <If test={!this.props.hideC}>
          <Grid cols='12 3 3 3'>

            <ButtonSelectHorizontal
              name='pageSize' defaultValue={this.props.pageSize}
              onChange={this.changePageSize}
              label="Tam. da Página"
              classButton='btn btn-sm btn-primary'
              classSelect=' input-sm width-60'
              icon='lock'>
              <option value='5'>5</option>
              <option value='10'>10</option>
              <option value='15'>15</option>
              <option value='20'>20</option>
              <option value='25'>25</option>
              <option value='50'>50</option>
            </ButtonSelectHorizontal>

          </Grid>
        </If>

        <Grid align="pull-right" className="maxWidth" cols={this.props.hideB ? '12 6 6 6' : '12 3 3 3'}>
          <If test={!this.props.hideD}>
            <b className="font-xs" >Pág{this.props.currentPage + 1}/{this.props.lastPage + 1}&nbsp;Tot: {this.props.count}</b>
          </If>
        </Grid>

      </Row>
    )
  }


}

const mapStateToProps = state => (
  {
    pageSize: state.paginatorReducer.pageSize,
    currentPage: state.paginatorReducer.currentPage,
    pageToGo: state.paginatorReducer.pageToGo,
    lastPage: state.paginatorReducer.lastPage,
    count: state.paginatorReducer.count
  })

const mapDispatchToProps = dispatch => bindActionCreators({
  goAction,
  changePageSizeAction,
  getPageList,
  goToPageAction
}, dispatch)

export default connect(mapStateToProps, mapDispatchToProps)(PaginatorBar)
