const mongoose = require('mongoose');

const notificationSchema = new mongoose.Schema({
    id: {
        type: Number,
        unique: true
    },
    notificationName: {
        type: String,
        required: true
    },
    notificationDesc: {
        type: String,
        required: true
    },
    createdAt: {
        type: Date,
        default: Date.now
    }
});

// Auto-increment ID 
notificationSchema.pre('save', async function(next) {
    if (this.isNew) {
        const lastNotification = await this.constructor.findOne().sort('-id');
        this.id = lastNotification && lastNotification.id ? lastNotification.id + 1 : 1;
    }
    next();
});

module.exports = mongoose.model('Notification', notificationSchema);