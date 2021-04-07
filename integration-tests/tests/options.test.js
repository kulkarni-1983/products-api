const httpRequest = require('../common/httpRequest')

const { serverUrl, serverPort } = require('../common/config')

const baseUrl = `http://${serverUrl}:${serverPort}`
describe('Test options operations', () => {
  const product = {
    name: 'test-add-name',
    description: 'something',
    price: 10.0,
    deliveryPrice: 2.0
  }

  beforeAll(async () => {
    // add the product
    const res = await httpRequest.post(`${baseUrl}/products`, product)
    product.id = res.data.id
  })

  afterAll(async () => {
    // delete the product
    await httpRequest.deleteMethod(`${baseUrl}/products/${product.id}`)
  })

  it('CRUD operations on options', async () => {
    const option = {
      name: 'option-name',
      description: 'Description of the product option',
      productId: product.id
    }
    // add the option
    let res = await httpRequest.post(`${baseUrl}/products/${product.id}/options`, option)
    expect(res.status).toEqual(200)
    option.id = res.data.id
    option.productId = product.id
    expect(res.data).toEqual(option)

    // get all options
    res = await httpRequest.get(`${baseUrl}/products/${product.id}/options`)
    expect(res.status).toEqual(200)
    expect(res.data).toEqual([option])

    // get option by id
    res = await httpRequest.get(`${baseUrl}/products/${product.id}/options/${option.id}`)
    expect(res.status).toEqual(200)
    expect(res.data).toEqual(option)

    // update the option
    option.name = 'updated-option-name'
    res = await httpRequest.put(`${baseUrl}/products/${product.id}/options/${option.id}`, option)
    expect(res.status).toEqual(200)
    expect(res.data).toEqual(option)

    // get option by id
    res = await httpRequest.get(`${baseUrl}/products/${product.id}/options/${option.id}`)
    expect(res.status).toEqual(200)
    expect(res.data).toEqual(option)

    // delete the option by id
    res = await httpRequest.deleteMethod(`${baseUrl}/products/${product.id}/options/${option.id}`)
    expect(res.status).toEqual(200)
    expect(res.data).toEqual(option)

    // get option by id
    res = await httpRequest.get(`${baseUrl}/products/${product.id}/options/${option.id}`)
    expect(res.status).toEqual(403)
  })

  it('get all options for invalid product', async () => {
    // get all options
    const res = await httpRequest.get(`${baseUrl}/products/invalid-product/options`)
    expect(res.status).toEqual(403)
  })

  it('get a option for invalid product', async () => {
    // get all options
    const res = await httpRequest.get(`${baseUrl}/products/invalid-product/options/option-id`)
    expect(res.status).toEqual(403)
  })

  it('get options for invalid option id', async () => {
    // get all options
    const res = await httpRequest.get(`${baseUrl}/products/${product.id}/options/invalid-option`)
    expect(res.status).toEqual(403)
  })

  it('add option for invalid product', async () => {
    const option = {
      name: 'option-name',
      description: 'Description of the product option',
      productId: product.id
    }
    // add the option
    const res = await httpRequest.post(`${baseUrl}/products/invalid-product/options`, option)
    expect(res.status).toEqual(403)
  })

  it('update option for invalid product', async () => {
    const option = {
      name: 'option-name',
      description: 'Description of the product option',
      productId: product.id
    }
    // update the option
    const res = await httpRequest.put(`${baseUrl}/products/invalid-product/options/option-id`, option)
    expect(res.status).toEqual(403)
  })

  it('update option for invalid option id', async () => {
    const option = {
      name: 'option-name',
      description: 'Description of the product option',
      productId: product.id
    }
    // update the option
    const res = await httpRequest.put(`${baseUrl}/products/${product.id}/options/option-id`, option)
    expect(res.status).toEqual(403)
  })

  it('delete option for invalid product', async () => {
    // delete the option
    const res = await httpRequest.deleteMethod(`${baseUrl}/products/invalid-product/options/option-id`)
    expect(res.status).toEqual(403)
  })

  it('delete option for invalid option id', async () => {
    // delete the option
    const res = await httpRequest.deleteMethod(`${baseUrl}/products/${product.id}/options/option-d`)
    expect(res.status).toEqual(403)
  })
})
