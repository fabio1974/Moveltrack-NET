import {Actions} from "./Actions";


export function minimizeSideBar(value) {
  return {
    type: Actions.SIDEBAR_MINIMIZED,
    payload: value,
  }
}

