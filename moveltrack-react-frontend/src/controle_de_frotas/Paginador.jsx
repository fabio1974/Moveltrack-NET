import React, {Component} from 'react'
import ButtonInputTextHorizontal from "../common/form/ButtonInputTextHorizontal";
import ButtonSelectHorizontal from "../common/form/ButtonSelectHorizontal";
import Grid from "../common/layout/Grid";
import Row from "../common/layout/Row";
import If from "../common/operator/If";

class Paginador extends Component {

  constructor(props) {
    super(props)
  }



  render() {

    return (
      <div>
        <Row>
          <If test={!this.props.hideA}>
            <Grid cols={this.props.hideB ? '12 6 6 6' : '12 3 3 3'}>
              <nav className="" aria-label="pagination">
                <ul className="pagination pagination-sm">
                  <li className="page-item">
                    <button onClick={this.props.goFirst1} className="page-link"><i className="fa fa-fast-backward"/></button>
                  </li>
                  <li className="page-item">
                    <button onClick={this.props.goPrevious} className="page-link"><i className="fa fa-step-backward"/>
                    </button>
                  </li>
                  <li className="page-item">
                    <button onClick={this.props.goNext} className="page-link"><i className="fa fa-step-forward"/></button>
                  </li>
                  <li className="page-item">
                    <button onClick={this.props.goLast} className="page-link"><i className="fa fa-fast-forward"/></button>
                  </li>
                </ul>
              </nav>
            </Grid>
          </If>

          <If test={!this.props.hideB}>
            <Grid cols='12 3 3 3'>
              <ButtonInputTextHorizontal name='pageToGo' onChange={this.props.goToPage} onClick={this.props.goToPage}
                                         label="Ir Página" classButton='btn btn-sm btn-primary'
                                         classInput=' input-sm width-40'/>
            </Grid>
          </If>

          <If test={!this.props.hideC}>
            <Grid cols='12 3 3 3'>

              <ButtonSelectHorizontal
                name='pageSize' defaultValue={this.props.pageSize}
                onChange={this.props.changePageSize}
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
              <b
                className="font-xs">Pág{this.props.state.currentPage + 1}/{this.props.state.lastPage + 1}&nbsp;Tot: {this.props.state.count}</b>
            </If>
          </Grid>

        </Row>

      </div>
    )
  }


}

export default Paginador
