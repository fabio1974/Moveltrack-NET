import React from   'react'

export default props => {
  const className =  props.className?`  ${props.className}`  : ' '
  console.log("props",props.className)
  return (
    <div className={'meucontainer' + className}>{props.children}</div>
)}
