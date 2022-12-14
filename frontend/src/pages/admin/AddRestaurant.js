import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Button, Card, Form} from "react-bootstrap";
import {addRestaurant, fetchZones, refreshToken} from "../../api/adminAPI";
import Select from "react-select";
import {Multiselect} from 'multiselect-react-dropdown';
import {Helmet} from "react-helmet";
import add_restaurant from "../../res/add_restaurant.jpg";

function get(key) {
    let admin_info = localStorage.getItem(key);
    return JSON.parse(admin_info);
}

function AddRestaurant() {
    const navigate = useNavigate();
    const [tokens, setTokens] = useState(get("tokens"));
    const [admin = {
        adminId: '',
        email: '',
        restaurant: ''
    }] = useState(get('admin-info'));
    const [restaurant, setRestaurant] = useState({
        name: '',
        location: '',
        locationZone: {
            id: ''
        }
    });
    const [zones, setZones] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!admin)
            navigate('/admin/register')

        fetchZones()
            .then(response => {
                setZones(response)
            })
            .catch(error => {
                setError(error.response.data.message)
            });
    }, []);

    function handleChange(event) {
        const {name, value} = event.target;
        setRestaurant(prevState => {
            return {
                ...prevState,
                [name]: value
            };
        })
    }

    function handleSelect(selected) {
        setRestaurant(prevState => {
            return {
                ...prevState,
                'locationZone': selected.value
            };
        })
    }

    function onSelect(selectedList) {
        setRestaurant(prevState => {
            return {
                ...prevState,
                'deliveryZones': selectedList
            };
        })
    }

    function handleSubmit(event) {
        addRestaurant(admin.adminId, restaurant, tokens.accessToken)
            .then(() => {
                navigate('/admin/login');
            })
            .catch(error => {
                if (error.response.status === 403) {
                    refreshToken(tokens.refreshToken)
                        .then(tokens => {
                            setTokens(tokens)
                            localStorage.setItem("token", JSON.stringify(tokens))
                            addRestaurant(admin.adminId, restaurant, tokens.accessToken)
                                .then(() => {
                                    navigate('/admin/login');
                                })
                                .catch(error => {
                                    setError(error.response.data.message)
                                })
                        })
                } else
                    setError(error.response.data.message)
            })
        event.preventDefault()
    }

    return (
        <div style={{height: 800, backgroundImage: 'url(' + add_restaurant + ')', backgroundSize: 'cover'}}>
            <Helmet>
                <title>🍕 Admin | Agregar Restaurante</title>
            </Helmet>
          
            <Card style={{
                alignContent: "center",
                opacity: 0.97,
                marginLeft: 'auto',
                marginRight: 'auto',
                top: "2%",
                width: 700,
                height: 650,
                backgroundColor: '#F3C464',
                padding: 25
            }}>
                <Card.Title style={{justifyContent: 'center', display: 'flex', color: '#000', fontSize: 40}}>
                   Agrega tu Restaurante
                </Card.Title>
                <br/>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Nombre del
                            Restaurante</Form.Label>
                        <Form.Control
                            name={'name'}
                            type={'text'}
                            placeholder={'Ingresa el Nombre...'}
                            onChange={handleChange}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Ubicacion del
                            Restaurante</Form.Label>
                        <Form.Control
                            name={'location'}
                            type={'text'}
                            placeholder={'Ingresa la Ubicacion...'}
                            onChange={handleChange}/>
                    </Form.Group>

                    <Form.Group>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Localidad</Form.Label>
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
                            onChange={handleSelect}
                        >
                        </Select>
                    </Form.Group>
                    <br/>

                    <Form.Group>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Localidades de Entrega</Form.Label>
                        <Multiselect
                            options={
                                zones.map(zone => {
                                    return {
                                        name: zone.name,
                                        id: zone.id
                                    }
                                })
                            }
                            onSelect={onSelect}
                            closeIcon={'close'}
                            displayValue="name"/>
                    </Form.Group>

                    <br/>
                    <div style={{color: 'red', justifyContent: 'center', display: 'flex'}}>
                        {error}
                    </div>

                    <Button variant="success" type="submit" style={{width: 600}}>
                        Añadir Restaurante
                    </Button>
                </Form>

                <br/>
                <div style={{color: 'black', position: "relative", top: 10}}>
                    No estas listo? Vuelve Despues!
                </div>

                <Button style={{width: 150, position: "relative", top: 20}} onClick={() => {
                    navigate('/admin/register')
                }}>
                    Atras
                </Button>
            </Card>
        </div>
    );
}

export default AddRestaurant;