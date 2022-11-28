const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const MessageSchema = new Schema({
    from_user: {
        type: String,
        required: true
    },
    to_user: {
        type: String,
        required: true
    },
    msg: {
        type: String
    },
    date: {
        type: Date,
        default: Date.now
    }
});

module.exports = Message = mongoose.model('messages', MessageSchema);