const Notification = require('../models/Notification');
exports.createNotification = async (req, res) => {
    try {
        const { notificationName, notificationDesc } = req.body;
        
        if (!notificationName || !notificationDesc) {
            return res.status(400).json({
                success: false,
                message: 'Please provide both notification name and description'
            });
        }

        const notification = new Notification({
            notificationName,
            notificationDesc
        });

        await notification.save();

        res.status(201).json({
            success: true,
            data: notification,
            message: 'Notification created successfully'
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};
exports.getAllNotifications = async (req, res) => {
    try {
        const notifications = await Notification.find().sort({ id: 1 });
        res.status(200).json({
            success: true,
            data: notifications
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};
exports.getNotificationById = async (req, res) => {
    try {
        const notification = await Notification.findOne({ id: req.params.id });
        if (!notification) {
            return res.status(404).json({
                success: false,
                message: 'Notification not found'
            });
        }
        res.status(200).json({
            success: true,
            data: notification
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};