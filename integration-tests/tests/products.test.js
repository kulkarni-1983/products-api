const httpRequest = require('../common/httpRequest')

const { serverUrl, serverPort } = require('../common/config')

const baseUrl = `http://${serverUrl}:${serverPort}`
describe('Test products operations', () => {
  it('CRUD operations on product', async () => {
    const product = {
      name: 'test-add-name',
      description: 'something',
      price: 10.0,
      deliveryPrice: 2.0
    }
    // add the product
    let res = await httpRequest.post(`${baseUrl}/products`, product)
    expect(res.status).toEqual(200)
    product.id = res.data.id
    expect(res.data).toEqual(product)

    // get the product
    res = await httpRequest.get(`${baseUrl}/products/${product.id}`)
    expect(res.status).toEqual(200)
    expect(res.data).toEqual(product)

    // get all products
    res = await httpRequest.get(`${baseUrl}/products`)
    expect(res.status).toEqual(200)
    expect(res.data).toEqual([product])

    // get the product my name
    res = await httpRequest.get(`${baseUrl}/products/name=${product.name}`)
    expect(res.status).toEqual(200)
    expect(res.data).toEqual([product])

    // update the product
    product.price = 15.0
    product.deliveryPrice = 3.0
    res = await httpRequest.put(`${baseUrl}/products/${product.id}`, product)
    expect(res.status).toEqual(200)
    expect(res.data).toEqual(product)

    // get the product
    res = await httpRequest.get(`${baseUrl}/products/${product.id}`)
    expect(res.status).toEqual(200)
    expect(res.data).toEqual(product)

    // delete the product
    res = await httpRequest.deleteMethod(`${baseUrl}/products/${product.id}`)
    expect(res.status).toEqual(200)
    product.id = res.data.id
    expect(res.data).toEqual(product)

    // get the product again
    res = await httpRequest.get(`${baseUrl}/products/${product.id}`)
    expect(res.status).toEqual(403)
  })

  it('post the product which already exists', async () => {
    const product = {
      name: 'test-add-name',
      description: 'something',
      price: 10.0,
      deliveryPrice: 2.0
    }
    // add the product
    let res = await httpRequest.post(`${baseUrl}/products`, product)
    expect(res.status).toEqual(200)
    product.id = res.data.id
    expect(res.data).toEqual(product)

    // add the product again
    res = await httpRequest.post(`${baseUrl}/products`, product)
    expect(res.status).toEqual(403)

    // delete the product
    res = await httpRequest.deleteMethod(`${baseUrl}/products/${product.id}`)
    expect(res.status).toEqual(200)
    product.id = res.data.id
    expect(res.data).toEqual(product)
  })

  it('get the product which does not exist', async () => {
    // get the product
    const res = await httpRequest.get(`${baseUrl}/products/invalid-id`)
    expect(res.status).toEqual(403)
  })

  it('get the product by name which does not exist', async () => {
    // get the product
    const res = await httpRequest.get(`${baseUrl}/products/name=invalid-name`)
    expect(res.status).toEqual(403)
  })

  it('multiple products with the same  name', async () => {
    const product = {
      name: 'test-add-name',
      description: 'something',
      price: 10.0,
      deliveryPrice: 2.0
    }
    // add the product
    let res = await httpRequest.post(`${baseUrl}/products`, product)
    expect(res.status).toEqual(200)
    const id1 = res.data.id
    expect(res.data).toEqual({
      id: id1,
      ...product
    })

    // add the product with same name
    res = await httpRequest.post(`${baseUrl}/products`, product)
    expect(res.status).toEqual(200)
    const id2 = res.data.id
    expect(res.data).toEqual({
      id: id2,
      ...product
    })

    // get the products by name
    res = await httpRequest.get(`${baseUrl}/products/name=${product.name}`)
    expect(res.status).toEqual(200)
    expect(res.data).toHaveLength(2)

    // delete the products
    res = await httpRequest.deleteMethod(`${baseUrl}/products/${id1}`)
    expect(res.status).toEqual(200)
    res = await httpRequest.deleteMethod(`${baseUrl}/products/${id2}`)
    expect(res.status).toEqual(200)
  })

  it('Update the product which doesn\'t exist', async () => {
    const product = {
      name: 'test-add-name',
      description: 'something',
      price: 10.0,
      deliveryPrice: 2.0
    }
    const res = await httpRequest.put(`${baseUrl}/products/invalid-id`, product)
    expect(res.status).toEqual(403)
  })
})
