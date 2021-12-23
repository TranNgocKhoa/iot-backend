db = db.getSiblingDB('iot');
db.createCollection('air_device_data');
db.air_device_data.createIndex(
    {
        deviceId: 1, timestamp: 1
    }
);