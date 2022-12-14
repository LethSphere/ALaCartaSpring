import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import Select from "react-select";
import {addFood, fetchMenu, refreshToken} from "../../api/adminAPI";
import {get} from "../../utils/utils";
import {Button, Card, Form, Nav} from "react-bootstrap";
import addFoodBck from '../../res/addfood.jpg';
import {Helmet} from "react-helmet";


function AddFood() {
    const [tokens, setTokens] = useState(get("tokens"));
    const navigate = useNavigate();
    const [admin = {
        administrator: {},
        adminId: '',
        email: '',
        restaurant: {}
    }] = useState(get('admin-info'));
    const [restaurant = {
        name: '',
        location: '',
        locationZone: {
            name: ''
        },
        deliveryZones: [],
        menu: {}
    }] = useState(admin?.restaurant);
    const [menu = {
        menuId: '',
        categories: []
    }] = useState(restaurant?.menu);
    const [categories = [{
        categoryId: '',
        category: '',
        foodList: [{
            foodId: '',
            name: '',
            description: '',
            price: null
        }]
    }]] = useState(menu?.categories);
    const [food, setFood] = useState({
        name: '',
        description: '',
        price: null
    });
    const [category, setCategory] = useState({
        categoryId: '',
        category: ''
    });
    const [error, setError] = useState('');

    function handleChange(event) {
        const {name, value} = event.target;
        setFood(prevState => {
            return {
                ...prevState,
                [name]: value
            };
        })
    }

    function handleSubmit(event) {
        addFood(category, food, tokens.accessToken)
            .then(() => {
                fetchMenu(restaurant, tokens.accessToken)
                    .then(response => {
                        let newAdmin = {
                            ...admin,
                            restaurant: {
                                ...restaurant,
                                menu: response
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
                            console.warn(tokens)
                            setTokens(tokens)
                            localStorage.setItem("tokens", JSON.stringify(tokens))
                            addFood(category, food, tokens.accessToken)
                                .then(() => {
                                    fetchMenu(restaurant, tokens.accessToken)
                                        .then(response => {
                                            let newAdmin = {
                                                ...admin,
                                                restaurant: {
                                                    ...restaurant,
                                                    menu: response
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
                } else {
                    setError(error.response.data.message)
                }
            });
        event.preventDefault();
    }

    useEffect(() => {
        if (!admin)
            navigate("/admin/login");
    }, [])


    return (
        <div style={{height: 800, backgroundImage: 'url(' + addFoodBck + ')', backgroundSize: 'cover'}}>
            <Helmet>
                <title>🍕 Admin | Agregar Comida</title>
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

            <Card style={{
                opacity: 0.85,
                marginLeft: 'auto',
                marginRight: 'auto',
                top: "10%",
                width: 500,
                height: 600,
                backgroundColor: '#CE2C2F',
                padding: 25,
                borderRadius: 15
            }}>
                <Card.Title style={{justifyContent: 'center', display: 'flex', color: '#000', fontSize: 40}}>
                    Agregar comida
                </Card.Title>
                <br/>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Categoria</Form.Label>
                        <Select options={
                            categories.map(cat => {
                                return {
                                    value: cat,
                                    label: cat.category
                                }
                            })
                        }
                                onChange={(selected) => setCategory(selected.value)}>
                        </Select>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Nombre</Form.Label>
                        <Form.Control
                            name={'name'}
                            type={'text'}
                            placeholder={'Ingrese un Nombre...'}
                            onChange={handleChange}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Descripcion</Form.Label>
                        <Form.Control
                            name={'description'}
                            type={'text'}
                            placeholder={'Ingrese una Descripcion...'}
                            onChange={handleChange}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Precio</Form.Label>
                        <Form.Control
                            name={'price'}
                            type={'number'}
                            step="0.01"
                            placeholder={'Ingrese un precio...'}
                            onChange={handleChange}/>
                    </Form.Group>

                    <text style={{color: 'black', justifyContent: 'center', display: 'flex'}}>
                        {error}
                    </text>

                    <Button variant="primary" type="submit" style={{width: 450, position: "relative", top: 20}}>
                        Añadir comida
                    </Button>
                </Form>
            </Card>
        </div>
    );
}

export default AddFood;