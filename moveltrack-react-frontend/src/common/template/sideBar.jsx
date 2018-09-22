import React from   'react'
import {menu} from './../../main/app'

export default props => (
    <aside className='main-sidebar'>
        <section className='sidebar'>
            {menu()}
        </section>
    </aside>
)