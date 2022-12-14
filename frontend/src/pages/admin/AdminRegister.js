import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {sendRegister} from "../../api/adminAPI";
import {Button, Card, Form, Nav} from "react-bootstrap";
import adminRegister from "../../res/admin_register.jpg";
import {Helmet} from "react-helmet";

function AdminRegister() {
    localStorage.clear();
    const [accountDTO, setAccountDTO] = useState({
        credential: '',
        password: ''
    });
    const [error, setError] = useState("");
    const navigate = useNavigate();

    function handleSubmit(event) {
        sendRegister(accountDTO)
            .then(response => {
                localStorage.setItem('admin-info', JSON.stringify(response));
                navigate('/admin/login');
            })
            .catch(error => {
                setError(error.response.data.message)
            });
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
        <div style={{height: 1000, backgroundImage: 'url(' + adminRegister + ')', backgroundSize: 'cover'}}>
            <Helmet>
                <title> Admin | Register</title>
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
                height: 450,
                backgroundColor: '#F24405',
                padding: 25
            }}>
                <Card.Title style={{justifyContent: 'center', display: 'flex', color: '#000', fontSize: 40}}>
                    Registro Admin
                </Card.Title>
                <br/>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Email</Form.Label>
                        <Form.Control
                            name={'credential'}
                            type={'email'}
                            placeholder={'Ingresa email...'}
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
                        Registrar
                    </Button>
                </Form>

                <br/>
                <text style={{color: 'black', position: "relative", top: 10}}>
                Ya tiene una cuenta?
                </text>

                <Button style={{width: 100, position: "relative", top: 20}} onClick={() => {
                    navigate('/admin/login')
                }}>
                    Login
                </Button>
            </Card>
        </div>
    );
}

export default AdminRegister;