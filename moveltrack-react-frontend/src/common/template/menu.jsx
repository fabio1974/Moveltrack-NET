import React from   'react'
import MenuItem from './menuItem'
import MenuTree from './menuTree'
import {Route, Switch } from   'react-router-dom'



export default props => (
    <div>



        <ul className='sidebar-menu'>
            <MenuTree label='Cadastro' icon='edit'>
                <MenuItem path='/pessoas' label='Pessoas' icon='usd' />
                
            </MenuTree>
        </ul>

       

    </div>

)