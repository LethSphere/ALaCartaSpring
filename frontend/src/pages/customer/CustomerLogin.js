import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {Button, Card, Form, Nav} from "react-bootstrap";
import customerLogin from "../../res/customer_login.jpg";
import {Helmet} from "react-helmet";
import {loginToken} from "../../api/adminAPI";
import {sendLogin} from "../../api/customerAPI";

function CustomerLogin() {
    localStorage.clear();
    const navigate = useNavigate();
    const [accountDTO, setAccountDTO] = useState({
        credential: '',
        password: ''
    });
    const [error, setError] = useState('');

    function handleSubmit(event) {
        loginToken(accountDTO)
            .then(tokens => {
                localStorage.setItem("tokens", JSON.stringify(tokens))
                sendLogin(accountDTO)
                    .then(customerData => {
                        localStorage.setItem('customer-info', JSON.stringify(customerData))
                        navigate('/customer/home')
                    })
                    .catch(error => {
                        setError(error.response.data.message)
                    });
            })
            .catch(() => {
                setError("Invalid credentials")
            })
        event.preventDefault();
    }

    function handleChange(event) {
        const {name, value} = event.target
        setAccountDTO(prevState => {
            return {
                ...prevState,
                [name]: value
            };
        })
    }

    return (
        <div style={{height: 800, backgroundImage: 'url(' + customerLogin + ')', backgroundSize: 'cover',margin:'auto',}}>
            <Helmet>
                <title>🍔 Cliente | Login</title>
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
                    <Nav.Link href="/admin/login" style={{color: '#CE2C2F', fontSize: 20}}>Admin</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{}}>
                    <Nav.Link href="/customer/login" style={{color: '#CE2C2F', fontSize: 20}}>Cliente</Nav.Link>
                </Nav.Item>
            </Nav>

            <Card style={{
                
                alignContent: "center",
                opacity: 0.85,
                marginLeft: 'auto',
                marginRight: 'auto',
                top: "13%",
                width: 500,
                height: 475,
                backgroundColor: '#F24405',
                padding: 50
            }}>
                <Card.Title style={{justifyContent: 'center', display: 'flex', color: '#000', fontSize: 40}}>
                    Login Cliente
                </Card.Title>
                <br/>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Email</Form.Label>
                        <Form.Control
                            name={'credential'}
                            type={'email'}
                            placeholder={'Ingresa Email...'}
                            onChange={handleChange}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Contraseña</Form.Label>
                        <Form.Control
                            name={'password'}
                            type={'password'}
                            placeholder={'Ingresa Contraseña...'}
                            onChange={handleChange}/>
                    </Form.Group>

                    <text style={{color: 'red', justifyContent: 'center', display: 'flex'}}>
                        {error}
                    </text>

                    <Button variant="success" type="submit" style={{width: 400}}>
                        Entrar
                    </Button>
                </Form>

                <br/>
                <text style={{color: 'black', position: "relative", top: 10,}}>
                    No tienes cuenta aún?
                </text>

                <Button style={{width: 150, position: "relative", top: 20}} onClick={() => {
                    navigate('/customer/register')
                }}>
                    Registrate
                </Button>
            </Card>
        </div>
    );
}

export default CustomerLogin;
