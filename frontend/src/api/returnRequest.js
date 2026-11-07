// src/api/returnRequest.js
import adminRequest from '@/utils/adminRequest'

export function fetchReturnRequestPage(params) {
  return adminRequest({
    url: '/return-request/page',
    method: 'get',
    params
  })
}

export function updateReturnRequestStatus(id, status) {
  return adminRequest({
    url: `/return-request/status/${id}`,
    method: 'put',
    params: { status }
  })
}
