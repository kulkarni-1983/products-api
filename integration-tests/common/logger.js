const winston = require('winston')

const { logLevel } = require('./config')
const options = {
  level: logLevel,
  format: winston.format.json(),
  defaultMeta: {
    service: 'products-api'
  },
  transports: [new winston.transports.Console({ handleExceptions: true })]
}

const logger = winston.createLogger(options)

module.exports = logger
