const axios = require('axios')

const logger = require('./logger')

const get = async (url) => {
  try {
    const resp = await axios.get(url)
    logger.info('received response from get request', resp)
    return resp
  } catch (err) {
    logger.error('Failed to send get request with error, ', err)
    return {
      status: 403
    }
  }
}

const post = async (url, body={}) => {
  try {
    const resp = await axios.post(url, body)
    logger.info('received response from post request', resp)
    return resp
  } catch (err) {
    logger.error('Failed to send post request with error, ', err)
    return {
      status: 403
    }
  }
}

const deleteMethod = async (url, body={}) => {
  try {
    const resp = await axios.delete(url, body)
    logger.info('received response from delete request', resp)
    return resp
  } catch (err) {
    logger.error('Failed to send delete request with error, ', err)
    return {
      status: 403
    }
  }
}

module.exports.get = get
module.exports.post = post
module.exports.deleteMethod = deleteMethod
