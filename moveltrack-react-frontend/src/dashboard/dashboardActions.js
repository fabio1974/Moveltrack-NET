import axios from 'axios'
import consts from '../common/consts'


export function getSummary() {
    const request = axios.get(`${consts.API_URL}/billingCycles/summary`)
    return {
        type: 'BILLING_SUMMARY_FETCHED',
        payload: request
    }
}