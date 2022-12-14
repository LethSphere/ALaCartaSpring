import React, {useEffect, useState} from "react";
import {get} from "../../utils/utils";
import {sendOrder} from "../../api/customerAPI";
import {Button, Card, Form, Nav} from "react-bootstrap";
import {Helmet} from "react-helmet";
import cart from "../../res/cart.jpg";
import {refreshToken} from "../../api/adminAPI";

function CustomerCart() {
    const [tokens, setTokens] = useState(get("tokens"))
    const [customer = {
        customerId: null,
        email: '',
        name: '',
        address: '',
        addressZone: {
            id: null
        },
    }] = useState(get('customer-info'));
    const [products, setProducts] = useState(get('cart-products'));
    const [currentRestaurant] = useState(get('current-restaurant'));
    const [order, setOrder] = useState({
        products: []
    });
    const [error, setError] = useState('');
    const [details, setDetails] = useState('')

    useEffect(() => {
        setOrder({
            products: products
        });
    }, [])

    function handleChange(event) {
        let {value} = event.target
        setDetails(value)
    }

    function handleAddQuantity(item, value) {
        const index = products.findIndex(function (element) {
            return element.item.name === item.name
        });

        products[index].quantity += value;

        if (products[index].quantity === 0) {
            products.splice(index, 1);
        }

        setProducts(products)
        setOrder({
            products: products
        });
    }

    function handleSubmit() {
        sendOrder(order, customer.customerId, currentRestaurant.restaurantId, details, tokens.accessToken)
            .then(() => {
                setError('Your order has successfully been sent to the ' + currentRestaurant.name + ' restaurant');
            })
            .catch(error => {
                if (error.response.status === 403) {
                    refreshToken(tokens.refreshToken)
                        .then(tokens => {
                            setTokens(tokens)
                            localStorage.setItem("tokens", JSON.stringify(tokens))
                            sendOrder(order, customer.customerId, currentRestaurant.restaurantId, details, tokens.accessToken)
                                .then(() => {
                                    setError('Your order has successfully been sent to the ' + currentRestaurant.name + ' restaurant');
                                })
                                .catch(error => {
                                    setError(error.response.data.message)
                                })
                        })
                } else
                    setError(error.response.data.message)
            })
        setOrder(null)
        setProducts([])
        localStorage.setItem('current-restaurant', null)
        localStorage.setItem('cart-products', null)
    }

    return (
        <div style={{backgroundImage: 'url(' + cart + ')', backgroundSize:'cover'}}>
            <Helmet>
                <title>🛒 Cliente | Carrito</title>
            </Helmet>

            <Nav
                style={{
                    backgroundColor: '#F2F2F2', overflow: 'hidden',
                    opacity: 0.90,
                    position: 'fixed',
                    top: 0,
                    zIndex: 100,
                    width: '100%',
                    display: "flex",
                    justifyContent: "space-evenly",
                }}>
                <Nav.Item style={{}}>
                    <Nav.Link href="/customer/login" style={{color: '#CE2C2F', fontSize: 20}}>Inicio</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{}}>
                    <Nav.Link href="/customer/home" style={{color: '#CE2C2F', fontSize: 20}}>Menu</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{}}>
                    <Nav.Link href="/customer/orders" style={{color: '#CE2C2F', fontSize: 20}}>Ordenes</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{}}>
                    <Nav.Link onClick={() => {
                        localStorage.setItem('cart-products', JSON.stringify(products));
                        localStorage.setItem('current-restaurant', JSON.stringify(currentRestaurant));
                    }} href="/customer/cart" style={{color: '#CE2C2F', fontSize: 20}}>Carrito</Nav.Link>
                </Nav.Item>
            </Nav>

            <Form.Group className={'mb-3'}>
                <Form.Label style={{justifyContent: 'center', display: 'flex',paddingTop:70, fontSize:25}}>Detalles Adicionales</Form.Label>
                <Form.Control
                    name={'details'}
                    type={'text'}
                    placeholder={'Ingrese Detalles Adicionales...'}
                    onChange={handleChange}/>
            </Form.Group>

            {
                products?.map(product => {
                    return <div style={{height: 220, padding: 20}}>
                        <Card style={{
                            borderRadius: 25,
                            backgroundColor: 'slategray',
                            color: 'white',
                            border: '2px solid',
                            borderColor: 'red',
                            height: '100%'
                        }}>
                            <Card.Body>
                                <Card.Title>
                                    {product.item.name}
                                </Card.Title>
                                <Card.Text>
                                    {product.item.description}
                                    <br/>
                                    Price: {product.item.price}
                                </Card.Text>
                                <Button style={{width: 45}}
                                        onClick={() => handleAddQuantity(product.item, -1)}>
                                    -
                                </Button>
                                {product.quantity}
                                <Button style={{width: 45}}
                                        onClick={() => handleAddQuantity(product.item, +1)}>
                                    +
                                </Button>
                            </Card.Body>
                        </Card>
                    </div>
                })
            }

            <h2 style={{textAlign:"center",backgroundColor: 'rgba(242,242,242,0.85)'}}>
                Total: {
                products?.map((product) => product.item.price * product.quantity).reduce((acc, amount) => acc + amount, 0)
            }
            </h2>
            <div >

            
            <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top" style={{ textAlign:"center"}} >
            <input type="hidden" name="cmd" value="_xclick"></input>
            <input type="hidden" name="business" value="jetorresa@correo.udistrital.edu.co"></input>
            <input type="hidden" name="lc" value="AL"></input>
            <input type="hidden" name="item_name" value="Papas Firtas"></input>
            <input type="hidden" name="item_number" value="1"></input>
            <input type="hidden" name="amount" value= {products?.map((product) => product.item.price * product.quantity).reduce((acc, amount) => acc + amount, 0)/4500}></input>
            <input type="hidden" name="currency_code" value="USD"></input>
            <input type="hidden" name="button_subtype" value="services"></input>
            <input type="hidden" name="no_note" value="0"></input>
            <input type="hidden" name="bn" value="PP-BuyNowBF:btn_buynowCC_LG.gif:NonHostedGuest"></input>
            <input   type="image" src="https://www.paypalobjects.com/es_XC/AR/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!"></input>
            <img alt="" border="0" src="https://www.paypalobjects.com/es_XC/i/scr/pixel.gif" width="10" height="10"></img>
            </form>

            </div>

            <button style={{width: "100%", backgroundColor: 'green',}}
                    className={'btn btn-primary'}
                    onClick={handleSubmit}>
                Place Order
            </button>

            <h2>{error}</h2>
        </div>
    );
}

export default CustomerCart;