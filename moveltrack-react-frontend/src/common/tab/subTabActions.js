export function selectSubTabAction(tabId) {
    return {
        type: 'SUB_TAB_SELECTED',
        payload: tabId
    }
}

export function showSubTabsAction(...tabIds) {
    const tabsToShow = {}
    tabIds.forEach(e => tabsToShow[e] = true)
    return {
        type: 'SUB_TAB_SHOWED',
        payload: tabsToShow
    }
}