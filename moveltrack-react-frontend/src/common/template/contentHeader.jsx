import React from   'react'

export default props => (
    <nav  aria-label="breadcrumb">
        

<ol  className='breadcrumb' >
    <li className="breadcrumb-item">{props.secao}</li>
    <li className="breadcrumb-item"><a href={props.path}>{props.titulo}</a></li>
</ol>

    </nav>
)

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}