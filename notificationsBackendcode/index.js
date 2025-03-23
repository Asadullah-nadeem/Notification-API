const express = require('express');
const cors = require('cors');
const connectDB = require('./config/db');
const notificationRoutes = require('./routes/notificationRoutes');
const app = express();
connectDB();
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use('/api', notificationRoutes);
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({
        success: false,
        message: 'Something went wrong!'
    });
});
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});