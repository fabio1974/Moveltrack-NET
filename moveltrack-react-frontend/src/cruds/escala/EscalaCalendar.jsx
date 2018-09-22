import React, {Component} from "react";
import BigCalendar from "react-big-calendar";
import moment from "moment";
import "react-big-calendar/lib/css/react-big-calendar.css";
import consts from "../../common/consts";
import axios from "axios";
import {sendError} from "../../common/utils";
import {formValueSelector} from "redux-form";
import {bindActionCreators} from "redux";
import {cancelFormView, initAction} from "../commons/crudActions";
import {selectSubTabAction, showSubTabsAction} from "../../common/tab/subTabActions";
import {getOperacaoStatuss} from "../commons/tabelaApoioActions";
import connect from "react-redux/es/connect/connect";

moment.locale("en");
BigCalendar.momentLocalizer(moment);


moment.locale('pt-BR');
BigCalendar.momentLocalizer(moment);

const allViews = Object.keys(BigCalendar.Views).map(k => BigCalendar.Views[k])

class EscalaCalendar extends Component {

  constructor(props){
    super(props)
    this.state = {
      view: "month",
      width: '100%',
      events: []
    };

  }


  componentDidMount() {
    window.addEventListener("resize", () => {
      /*this.setState({
        width: window.innerWidth,
        height: window.innerHeight
      });*/
    });

    this.loadEvents()
  }

  loadEvents() {

    const url = `${consts.API_URL}/plantaos?search=escala:${this.props.escala.id}`
    axios.get(url).then(resp => {
      this.setState(prevState => ({
        events: resp.data.content.map(plantao => (
          {
            'title': plantao.pessoa.nome + ' -  '+ this.getTurma(plantao.sigla) /*+ ' ' + moment(plantao.inicioExpediente).format("HH:mm[h] [de] DD/MM") + ' até ' + moment(plantao.fimExpediente).format("HH:mm[h] [de] DD/MM")*/,
            'start': new Date(plantao.inicioExpediente),
            'end': new Date(plantao.fimExpediente),
            'turno': plantao.sigla
          }
        ))
      }))
    }).catch(e => sendError(e))
  }

  getTurma(turno){
    let turma = ''
    switch (turno) {
      case 'TURNO1':
        turma = 'ALPHA'
        break;
      case 'TURNO2':
        turma = 'BRAVO'
        break;
      case 'TURNO3':
        turma = 'CHARLIE'
        break;
      case 'TURNO4':
        turma = 'DELTA'
        break;
      default:
        turma = ''
        break;
    }
    return turma
  }

  eventStyleGetter(event, start, end, isSelected) {
    console.log("sigla",event.turno);
    let backgroundColor = '#CCC'
    switch (event.turno) {
      case 'TURNO1':
            backgroundColor = '#20a8d8'
            break;
      case 'TURNO2':
        backgroundColor = '#4dbd74'
            break;
      case 'TURNO3':
        backgroundColor = '#ffc107'
            break;
      case 'TURNO4':
        backgroundColor = '#f86c6b'
            break;
      default:
        backgroundColor = '#FF0'
        break;
    }

    var style = {
      backgroundColor: backgroundColor,
      borderRadius: '0px',
      opacity: 0.8,
      color: 'black',
      border: '0px',
      display: 'block'
    };
    return {
      style: style
    };
  }


  render() {
    return (

      <div className='card'>
        <div className='card-header'><strong>({this.props.escala.areaOrganizacional.sigla})&nbsp;{this.props.escala.areaOrganizacional.descricao}</strong></div>

        <div className='card-body row ' >


        <BigCalendar
          style={{height: 600, width: "100%"}}
          toolbar={true}
          events={this.state.events}
          showMultiDayTimes={true}
          step={60}
          views={allViews}
          onView={() => { }}
          defaultDate={new Date(this.props.escala.ano,this.props.escala.mes-1, 1)}
          onNavigate={date => this.setState({date})}
          formats={{ timeGutterFormat: 'HH:mm' }}
          eventPropGetter={(this.eventStyleGetter)}
        />


        </div>
      </div>
    );
  }
}

const selector = formValueSelector('objectForm')
const mapStateToProps = state => ({
  escala: selector(state,'id','ano','mes','areaOrganizacional'),
})
export default connect(mapStateToProps,null)(EscalaCalendar)
