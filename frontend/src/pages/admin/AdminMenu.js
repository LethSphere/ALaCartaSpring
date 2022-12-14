import React, {useEffect, useState} from "react";
import {Card, Nav} from "react-bootstrap";
import {foodList, get} from "../../utils/utils";
import {useNavigate} from "react-router-dom";
import background from '../../res/restaurant.jpg';
import {Helmet} from "react-helmet";
import {generatePDF, refreshToken} from "../../api/adminAPI";

function AdminMenu() {
    const [tokens, setTokens] = useState(get('tokens'))
    const [admin] = useState(get('admin-info'));
    const [restaurant] = useState(admin?.restaurant);
    const navigate = useNavigate();


    useEffect(() => {
        if (!admin)
            navigate('/admin/login')
    }, [])

    function generatePDFFunction(){
        generatePDF(admin, tokens.accessToken)
            .then()
            .catch(error => {
                if(error.response.status === 403){
                    refreshToken(tokens.refreshToken)
                        .then(tokens => {
                            setTokens(tokens)
                            localStorage.setItem('tokens', JSON.stringify(tokens))
                            generatePDF(admin, tokens.accessToken)
                                .then()
                        })
                }
            })
    }

    return (
        <div>
            <Helmet>
                <title>🍕 Admin | Menu</title>
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

            <div style={{padding: 60, backgroundImage: 'url(' + background + ')', backgroundSize: 'contain'}}>

                <button style={{backgroundColor: 'yellow', borderRadius: 10}}
                onClick={() => generatePDFFunction()}>
                    Generar PDF
                </button>

                <Card style={{
                    borderRadius: 30,
                    opacity:0.90,
                    marginLeft: 'auto',
                    marginRight: 'auto',
                    backgroundColor: '#144C96',
                    display: 'flex',
                    justifyContent: 'center',
                    width: 400,
                    height: 150
                }}>
                    <Card.Title style={{color: 'white',fontSize: 45, display: 'flex', justifyContent: 'center',fontFamily: 'sans-serif',fontStyle: 'italic'}}>
                       Carta {restaurant?.name}
                    </Card.Title>
                </Card>
                {restaurant?.menu.categories?.map(category => {
                    return <div>
                        <h1 style={{color: 'white', fontSize: 75, fontFamily: 'cursive'}}>
                            {category.category}
                        </h1>
                        <Card style={{
                            opacity: 0.90,
                            border: '7px solid',
                            borderColor: 'black',
                            borderRadius: 15,
                            backgroundColor: '#C41619',
                            backgroundSize: 'cover'
                        }}>
                            <Card.Body>
                                <Card.Title style={{color: 'Black',textAlign: "center"}}>
                                    {category.category}
                                </Card.Title>
                                {
                                    category.foodList.map(food => {
                                        return <div style={{height: 150, padding: 10}}>
                                            <Card style={{
                                                backgroundColor: 'slategray',
                                                color: 'white',
                                                border: '2px solid',
                                                borderColor: 'white',
                                                height: '100%'
                                            }}>
                                                <Card.Body>
                                                    <Card.Title>
                                                        {food.name}
                                                    </Card.Title>
                                                    <Card.Text>
                                                        {food.description}
                                                        <br/>
                                                        Price: {food.price}
                                                    </Card.Text>
                                                </Card.Body>
                                            </Card>
                                        </div>
                                    })
                                }
                            </Card.Body>
                        </Card>
                        <br/>
                        <br/>
                        <br/>
                    </div>
                })}
            </div>
        </div>
    );
}

export default AdminMenu;