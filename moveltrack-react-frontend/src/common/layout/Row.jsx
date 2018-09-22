import React from   'react'

const Row =  props => {

  const className = props.className || ''

  return (
    <div key={props.key} className={`row ${className}`} >
      {props.children}
    </div>
)
}
export default Row
