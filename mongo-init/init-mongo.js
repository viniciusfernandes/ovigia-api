// Use the admin database to create the new user
db = db.getSiblingDB('admin');

db.createUser({
  user: 'admin',
  pwd: '1234',
  roles: [
    {
      role: 'readWrite',
      db: 'ovigia'
    }
  ]
});

// Switch to the new database
db = db.getSiblingDB('ovigia');

// Create a collection
db.createCollection('mensalidades');
