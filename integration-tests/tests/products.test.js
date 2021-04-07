const httpRequest = require('../common/httpRequest')

const { serverUrl, serverPort } = require('../common/config')


describe('Test products operations', () => {
  it('Add get and remove product', async () => {
    const product = {
      "name": "test-add-name",
      "description": "something",
      "price": 10.0,
      "deliveryPrice": 2.0
    }
    // add the product
    var res = await httpRequest.post(`http://${serverUrl}:${serverPort}/products`, product)
    expect(res.status).toEqual(200)
    product.id = res.data.id
    expect(res.data).toEqual(product)

    // get the product
    res = await httpRequest.get(`http://${serverUrl}:${serverPort}/products/${product.id}`)
    expect(res.status).toEqual(200)
    expect(res.data).toEqual(product)

    // get the product my name
    res = await httpRequest.get(`http://${serverUrl}:${serverPort}/products/name=${product.name}`)
    expect(res.status).toEqual(200)
    expect(res.data).toEqual([product])

    // delete the product
    var res = await httpRequest.deleteMethod(`http://${serverUrl}:${serverPort}/products/${product.id}`)
    expect(res.status).toEqual(200)
    product.id = res.data.id
    expect(res.data).toEqual(product)

    // get the product again
    res = await httpRequest.get(`http://${serverUrl}:${serverPort}/products/${product.id}`)
    expect(res.status).toEqual(403)

  })
})
