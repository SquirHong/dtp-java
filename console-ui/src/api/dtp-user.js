import request from '@/utils/request'

export function getList(data) {
  return request({
    url: '/v1/cs/auth/users/page',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/v1/cs/auth/users/update',
    method: 'put',
    data
  })
}

export function createUser(data) {
  return request({
    url: '/v1/cs/auth/users/add',
    method: 'post',
    data
  })
}

export function deleteUser(name) {
  return request({
    url: '/v1/cs/user/remove/' + name,
    method: 'delete'
  })
}
