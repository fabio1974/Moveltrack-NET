
const initialState = {previousLocation: null, currentLocation: null,};

    const routerLocations = function (state = initialState, action) {
      const newState = { ...state };
      switch (action.type) {
          case "@@router/LOCATION_CHANGE":
              newState.previousLocation = state.currentLocation;
              newState.currentLocation = action.payload;
              return newState
        default:
          return state;
      }
    }

    export default routerLocations