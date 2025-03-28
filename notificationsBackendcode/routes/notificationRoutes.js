const express = require('express');
const router = express.Router();
const notificationController = require('../controllers/notificationController');

router.post('/notifications', notificationController.createNotification);
router.get('/notifications', notificationController.getAllNotifications);
router.get('/notifications/:id', notificationController.getNotificationById);

module.exports = router;