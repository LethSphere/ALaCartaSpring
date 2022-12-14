import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {sendRegister} from "../../api/customerAPI";
import {fetchZones, loginToken} from "../../api/adminAPI";
import Select from "react-select";
import {Button, Card, Form, Nav} from "react-bootstrap";
import customerRegisterImg from "../../res/customer_register.jpg";
import {Helmet} from "react-helmet";

function CustomerRegister() {
    localStorage.clear();
    const [customer, setCustomer] = useState({
        name: '',
        address: '',
        addressZone: {
            id: null
        }
    });
    const [user, setUser] = useState({
        email: '',
        password: '',
    });
    const [customerRegister, setCustomerRegister] = useState({
        customer: {
            name: '',
            address: '',
            addressZone: {
                id: null
            }
        },
        user: {
            email: '',
            password: '',
        }
    });
    const [zones, setZones] = useState([]);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    useEffect(() => {

        fetchZones()
            .then(response => {
                setZones(response)
            })
            .catch(error => {
                setError(error.response.data.message)
            });
    }, [])

    function handleChangeUser(event) {
        const {name, value} = event.target
        setUser(prevState => {
            return {
                ...prevState,
                [name]: value
            };
        })
    }

    function handleChangeCustomer(event) {
        const {name, value} = event.target
        setCustomer(prevState => {
            return {
                ...prevState,
                [name]: value
            };
        })
        setCustomerRegister(
            {
                customer: customer,
                user: user
            }
        )
    }

    async function handleSelect(selected) {
        setCustomerRegister(
            {
                customer: {
                    ...customer,
                    'addressZone': selected.value
                },
                user: user
            }
        )
    }

    function handleSubmit(event) {
        sendRegister(customerRegister)
            .then(response => {
                localStorage.setItem('customer-info', JSON.stringify(response));
                navigate('/customer/login');
            })
            .catch(error => {
                setError(error.response.data.message)
            });
        event.preventDefault()
    }

    return (
        <div style={{height: 850, backgroundImage: 'url(' + customerRegisterImg + ')', backgroundSize: 'cover',margin:'auto'}}>
            <Helmet>
                <title>🍔 Cliente | Register</title>
            </Helmet>

            <Nav
                style={{
                    backgroundColor: '#F2F2F2',
                    opacity: 0.90,
                    position: 'fixed',
                    top: 0,
                    zIndex: 100,
                    width: '100%',
                    display: "flex",
                    justifyContent: "space-evenly",

                }}>
                <Nav.Item style={{}}>
                    <Nav.Link href="/admin/login" style={{color: '#CE2C2F', fontSize: 20}}>Admin</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{}}>
                    <Nav.Link href="/customer/login" style={{color: '#CE2C2F', fontSize: 20}}>Cliente</Nav.Link>
                </Nav.Item>
            </Nav>

            <Card style={{
                alignContent: "center",
                marginLeft: 'auto',
                marginRight: 'auto',
                opacity: 0.85,
                top: "8%",
                width: 500,
                height: 750,
                backgroundColor: '#F24405',
                padding: 25
            }}>
                <Card.Title style={{justifyContent: 'center', display: 'flex', color: '#000', fontSize: 40}}>
                    Registro Cliente
                </Card.Title>
                <br/>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Email</Form.Label>
                        <Form.Control
                            name={'email'}
                            type={'email'}
                            placeholder={'Ingresa email...'}
                            onChange={handleChangeUser}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Contraseña</Form.Label>
                        <Form.Control
                            name={'password'}
                            type={'password'}
                            placeholder={'Ingresa Contraseña...'}
                            onChange={handleChangeUser}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Nombre</Form.Label>
                        <Form.Control
                            name={'name'}
                            type={'text'}
                            placeholder={'Ingresa Nombre...'}
                            onChange={handleChangeCustomer}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Dirección</Form.Label>
                        <Form.Control
                            name={'address'}
                            type={'text'}
                            placeholder={'Ingresa Dirección...'}
                            onChange={handleChangeCustomer}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Zona</Form.Label>
                        <Select
                            options={
                                zones.map((zone) => {
                                    return {
                                        
                                        value: {
                                            id: zone.id
                                        },
                                        label: zone.name
                                    }

                                })
                               
                            }
                            onChange={handleSelect}>
                        </Select>
                        
                    </Form.Group>


                    <text style={{color: 'Black', justifyContent: 'center', display: 'flex'}}>
                        {error}
                    </text>

                    <Button variant="success" type="submit" style={{width: 400, }}>
                        Registrate
                    </Button>
                </Form>

                <br/>
                <text style={{color: 'black', position: "relative", top: 10}}>
                    Ya tiene una cuenta?
                </text>

                <Button style={{width: 150, position: "relative", top: 20}} onClick={() => {
                    navigate('/customer/login')
                }}>
                    Login
                </Button>
            </Card>
        </div>
    );
}

export default CustomerRegister;