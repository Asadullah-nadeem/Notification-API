
# **Notification API using Android, Node.js, and MongoDB**

## **Overview**

This project provides a **Notification API** built with **Node.js** and  **MongoDB** , which integrates with an **Android application** using **Volley and WorkManager** for efficient API communication and background task execution.

---

## **Technologies Used**

### **Backend (Node.js & MongoDB)**

* **Node.js** - Server-side framework.
* **Express.js** - Fast and minimal web framework.
* **MongoDB** - NoSQL database for storing notifications.
* **Mongoose** - ODM for MongoDB.
* **CORS** - Middleware to enable cross-origin requests.

### **Android (Client-side)**

* **Volley** - Networking library for API requests.
* **WorkManager** - Background task execution.

---

## **Backend Setup**

### **1️⃣ Install Dependencies**

Run the following command in your **Node.js project** directory:

```sh
npm install express mongoose cors
```

### **2️⃣ Package.json Dependencies**

Your `package.json` should include:

```json
"dependencies": {
    "cors": "^2.8.5",
    "express": "^4.21.2",
    "mongoose": "^8.12.2"
}
```

### **3️⃣ Start the Server**

Run the server:

```sh
node index.js
```

or

```sh
nodemon index.js
```

---

## **Android Setup**

### **1️⃣ Add Dependencies**

In your  **Android `build.gradle` (Module: app)** , add:

```gradle
dependencies {
    implementation 'androidx.work:work-runtime:2.8.1'
    implementation 'com.android.volley:volley:1.2.1'
}
```

### **2️⃣ Make an API Request with Volley**

Example  **Volley request** :https://google.github.io/volley/simple.html

```java
final TextView textView = (TextView) findViewById(R.id.text);
// ...

// Instantiate the RequestQueue.
RequestQueue queue = Volley.newRequestQueue(this);
String url = "https://www.google.com";

// Request a string response from the provided URL.
StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
        // Display the first 500 characters of the response string.
        textView.setText("Response is: " + response.substring(0,500));
    }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
        textView.setText("That didn't work!");
    }
});

// Add the request to the RequestQueue.
queue.add(stringRequest);
```

---

## **API Endpoints**

| Method           | Endpoint                   | Description               |
| ---------------- | -------------------------- | ------------------------- |
| **POST**   | `/api/notifications`     | Create a new notification |

---

## **POST API Endpoints**

| Key           | Value                   | 
| ---------------- | -------------------------- |
| `notificationName`    |      Test 1                    |
| `notificationDesc`    |    This is a Test notification |

---

## **Database Schema (MongoDB)**

```javascript
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
```

---

## **Running the Project**

1. **Start MongoDB** (`mongod`)
2. **Run the backend** (`node index.js`)
3. **Run the Android app** and test API calls.

---

### **Image:**
  
![1](https://github.com/user-attachments/assets/fe79a8bf-6411-42f0-94c5-f46ddb6dc4e8)
![7](https://github.com/user-attachments/assets/05990b70-48ed-4a6d-98b2-f0fde63aa217)
![6](https://github.com/user-attachments/assets/0d215e51-ddc0-4f65-8e2c-38f44450858a)
![5](https://github.com/user-attachments/assets/e13ae5ea-f9f4-44a8-961b-81b1bf234c61)
![4](https://github.com/user-attachments/assets/131745da-db5d-40c8-aa73-2ab24211b4ff)
![3](https://github.com/user-attachments/assets/5d44467f-8361-402f-8c5c-b065ffaaa319)
![2](https://github.com/user-attachments/assets/9f53f659-b0a0-41e4-91eb-22fc7e876025)
  
