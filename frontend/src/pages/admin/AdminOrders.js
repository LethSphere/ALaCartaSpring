import React, {useEffect, useState} from "react";
import {get} from "../../utils/utils";
import {Button, Card, CardGroup, Nav} from "react-bootstrap";
import receipt from '../../res/reciept.jpg';
import Select from "react-select";
import {changeStatus, fetchOrders, refreshToken} from "../../api/adminAPI";
import {Helmet} from "react-helmet";
import {Multiselect} from 'multiselect-react-dropdown';

function AdminOrders() {
    const [tokens, setTokens] = useState(get("tokens"));
    const [admin = {
        administrator: {},
        adminId: '',
        email: '',
        restaurant: {}
    }] = useState(get('admin-info'));
    const [restaurant = {
        restaurantId: null,
        name: '',
        location: '',
        locationZone: {
            name: ''
        },
        deliveryZones: [],
        menu: {},
        orders: []
    }] = useState(admin?.restaurant);
    const [orders = [{
        orderId: null,
        products: [],
        state: {
            orderStatus: ''
        }
    }], setOrders] = useState(restaurant?.orders);
    const [filterOrders, setFilterOrders] = useState(orders);
    const options = [
        {value: 'DECLINED', label: 'Declined'},
        {value: 'ACCEPTED', label: 'Accepted'},
        {value: 'IN_DELIVERY', label: 'In delivery'},
        {value: 'DELIVERED', label: 'Delivered'}];
    const filterOptions = ['PENDING', 'ACCEPTED', 'DECLINED', 'IN_DELIVERY', 'DELIVERED'];
    const [changedOrder, setChangedOrder] = useState({
            orderId: null,
            state: {
                orderStatus: ''
            }
        }
    );
    const [error, setError] = useState('');
    const colorStatus = {
        'PENDING': 'rgba(255,143,94,0.85)',
        'ACCEPTED': 'rgba(60,194,0,0.85)',
        'DECLINED': 'rgba(255,47,33,0.85)',
        'IN_DELIVERY': 'rgba(117,189,255,0.85)',
        'DELIVERED': 'rgba(131,255,187,0.85)'
    };

    useEffect(() => {
        fetchOrders(restaurant, tokens.accessToken)
            .then(response => {
                console.warn(response)
                setOrders(response.response)
            })
            .catch(error => {
                refreshToken(tokens.refreshToken)
                    .then(tokens => {
                        setTokens(tokens)
                        localStorage.setItem("tokens", JSON.stringify(tokens))
                        fetchOrders(restaurant, tokens.accessToken)
                            .then(response => {
                                console.warn(response)
                                setOrders(response.response)
                            })
                            .catch(error => {
                                setError(error.response.data.message)
                            })
                    })
                    .catch(error => {
                        setError(error.response.data.message)
                    })
            })
    }, [])

    function handleFilter(selectedList) {
        if (selectedList.length === 0)
            setFilterOrders(orders);
        else {
            let newOrders = orders.filter(function (item) {
                console.warn(item)
                return selectedList.includes(item.state.orderStatus);
            }).map(function (item) {
                return item;
            });
            setFilterOrders(newOrders);
        }
    }

    function handleSelect(selected, orderId) {
        setError('')
        setChangedOrder({
            orderId: orderId,
            state: {
                orderStatus: selected.value
            }
        })
    }

    function handleSubmit() {
        changeStatus(changedOrder, tokens.accessToken)
            .then(() => {
                fetchOrders(restaurant, tokens.accessToken)
                    .then(response => {
                        let newAdmin = {
                            ...admin,
                            restaurant: {
                                ...restaurant,
                                orders: response.response
                            }
                        }
                        localStorage.setItem('admin-info', JSON.stringify(newAdmin));
                        window.location.reload(false);
                    })
                    .catch(error => {
                        setError(error.response.data.message)
                    })
            })
            .catch(error => {
                if (error.response.status === 403) {
                    refreshToken(tokens.refreshToken)
                        .then(tokens => {
                            setTokens(tokens)
                            localStorage.setItem("tokens", JSON.stringify(tokens))
                            changeStatus(changedOrder, tokens.accessToken)
                                .then(() => {
                                    fetchOrders(restaurant, tokens.accessToken)
                                        .then(response => {
                                            let newAdmin = {
                                                ...admin,
                                                restaurant: {
                                                    ...restaurant,
                                                    orders: response.response
                                                }
                                            }
                                            localStorage.setItem('admin-info', JSON.stringify(newAdmin));
                                            window.location.reload(false);
                                        })
                                        .catch(error => {
                                            setError(error.response.data.message)
                                        })
                                })
                                .catch(error => {
                                    setError(error.response.data.message)
                                })
                        })
                } else
                    setError(error.response.data.message)
            })
    }

    return (
        <div style={{backgroundImage: 'url(' + receipt + ')', backgroundSize: 'cover'}}>
            <Helmet>
                <title>🍕 Admin | Orders</title>
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
                    <Nav.Link href="/admin/login" style={{color: '#CE2C2F', fontSize: 20}}>Inicio</Nav.Link>
                </Nav.Item>
               
                <Nav.Item style={{}}>
                    <Nav.Link href="/admin/addFood" style={{color: '#CE2C2F', fontSize: 20}}>Añadir Comidas</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{}}>
                    <Nav.Link href="/admin/menu" style={{color: '#CE2C2F', fontSize: 20}}>Menu</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{}}>
                    <Nav.Link href="/admin/orders" style={{color: '#CE2C2F', fontSize: 20}}>Ordenes</Nav.Link>
                </Nav.Item>
            </Nav>
            <h2 style={{paddingTop:50}}>
                Filtro Por Estado
            </h2>
            <Multiselect options={filterOptions} isObject={false}
                         onSelect={handleFilter} onRemove={handleFilter}>

            </Multiselect>
            <div>
                {
                    filterOrders.map((order) => {
                        return <div style={{position:"relative", padding:10}}>
                            <Card key={order.products + order.orderId}
                                  style={{
                                      backgroundColor: colorStatus[order.state.orderStatus],
                                      padding: 20,
                                      border: '5px solid',
                                      borderColor: 'black',
                                      borderRadius: 20,

                                  }}>
                                <h2>#{order.orderId} | Status: {order.state.orderStatus}</h2>
                                <CardGroup>
                                    {
                                        order.products.map(product => {
                                            return <div style={{height: 100, width: 250, padding: 5}}>
                                                <Card key={product.item + product.quantity}
                                                      style={{
                                                          padding: 10,
                                                          borderRadius: 20,
                                                          backgroundColor: 'slategray',
                                                          color: 'white',
                                                          border: '2px solid',
                                                          borderColor: 'white',
                                                          height: '100%'
                                                      }}>
                                                    <Card.Title style={{display: 'flex', justifyContent: 'center'}}>
                                                        {product.item.name} * {product.quantity}
                                                    </Card.Title>
                                                    <Card.Text style={{display: 'flex', justifyContent: 'center'}}>
                                                        Price: {product.quantity * product.item.price}
                                                    </Card.Text>
                                                </Card>
                                            </div>
                                        })
                                    }
                                </CardGroup>
                                <br/>
                                <Select
                                    options={options}
                                    onChange={(selected) => handleSelect(selected, order.orderId)}/>
                                <br/>
                                <Button
                                    style={{width: 300}}
                                    className={'btn btn-primary'}
                                    onClick={handleSubmit}>
                                    Change status
                                </Button>
                                {
                                    changedOrder.orderId === order.orderId ?
                                        <h4 style={{color: 'red'}}>{error}</h4> : null
                                }
                            </Card>
                        </div>
                    })
                }
            </div>
        </div>
    );
}

export default AdminOrders;