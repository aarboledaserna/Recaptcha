const express = require('express');
const bodyParser = require('body-parser');
const axios = require('axios');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3500;

// Middleware para parsear el cuerpo de las solicitudes
app.use(bodyParser.urlencoded({ extended: true }));

// Servir el archivo index.html
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});

// Ruta para manejar la verificación del reCAPTCHA
app.post('/verify', async (req, res) => {
    const secretKey = ''; // Reemplaza con tu clave secreta
    const responseKey = req.body['g-recaptcha-response'];
    const userIP = req.ip;

    // Verificar el token con la API de Google
    const verificationUrl = `https://www.google.com/recaptcha/api/siteverify?secret=${secretKey}&response=${responseKey}&remoteip=${userIP}`;

    try {
        const response = await axios.post(verificationUrl);
        const data = response.data;

        if (data.success) {
            res.send(`Verificación exitosa. Nombre: ${req.body.name}, Email: ${req.body.email}`);
        } else {
            res.send("Verificación fallida. Por favor intenta nuevamente.");
        }
    } catch (error) {
        res.send("Error al verificar el reCAPTCHA.");
    }
});

// Iniciar el servidor
app.listen(PORT, () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
