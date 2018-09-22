import React, { Component } from   'react'
import If from  '../operator/If'
export default class Grid extends Component {

    toCssClasses(numbers) {
        const cols = numbers ? numbers.split(' ') : []
        let classes = ''

        if(cols[0]) classes += `col-xs-${cols[0]}` 
        if(cols[1]) classes += ` col-sm-${cols[1]}`
        if(cols[2]) classes += ` col-md-${cols[2]}`
        if(cols[3]) classes += ` col-lg-${cols[3]}`

        return classes 
    }

    render() {
        const gridClasses = this.toCssClasses(this.props.cols || '')
        const className =  this.props.className?`  ${this.props.className}`  : ' '
        return (
            <div className={gridClasses + className }>

              <If test={this.props.align}>

                <div className={this.props.align}>
                  {this.props.children}
                </div>
              </If>

              <If test={!this.props.align}>
                {this.props.children}
              </If>


            </div> 
        )
    }
}
