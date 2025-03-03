# Java API Generator – AI-powered API creation

## 🔹 Overview
Java API Generator is a tool that automates the creation of Java APIs using **Spring Boot**, **Freemarker templates**, and **AI-driven code generation**.

With this tool, you can:  
✅ **Generate a Java Controller** automatically from an entity name.  
✅ **Create a complete Java class** from a text prompt using AI.  
✅ **Build a fully functional API**, packaged in a ZIP file, ready to run.

---

## ⚙️ Tech Stack
- **Java 21** (Records, String Templates, Pattern Matching)
- **Spring Boot 3** (Backend and API structure)
- **Freemarker** (Template engine for dynamic code generation)
- **Mistral AI** (via Hugging Face API for AI-powered class generation)
- **Swagger/OpenAPI** (for API documentation and testing)

---

## 🛠 Installation & Usage

### 1️⃣ Clone the repository
```sh
git clone https://github.com/sbrunomello/java-api-generator.git
cd java-api-generator
```

### 2️⃣ Set up environment variables
Create a `.env` file in the root directory and configure your Hugging Face API key:
```ini
AI_API_URL=https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct
AI_API_KEY=your_huggingface_api_key
```

### 3️⃣ Run the application
Make sure you have **Java 21+** installed, then execute:
```sh
./mvnw spring-boot:run
```

### 4️⃣ Test the API
Once the application is running, access **Swagger UI** to test the endpoints:  
📌 `http://localhost:8080/swagger-ui.html`

---

## 🎯 API Endpoints

### 📌 Generate a Java Controller
**Endpoint:** `POST /api/generator/generate`  
**Params:**
- `packageName` (e.g., `"com.example"`)
- `entityName` (e.g., `"User"`)

📌 **Example Request:**
```sh
curl -X POST "http://localhost:8080/api/generator/generate?packageName=com.example&entityName=User"
```

---

### 📌 Generate a Java Class using AI
**Endpoint:** `POST /api/generator/generate-ai`  
**Params:**
- `className` (e.g., `"OrderService"`)
- `prompt` (e.g., `"Create a service class to manage orders"`)

📌 **Example Request:**
```sh
curl -X POST "http://localhost:8080/api/generator/generate-ai?className=OrderService&prompt=Create+a+service+class+to+manage+orders"
```

---

### 📌 Generate a Complete API Project
**Endpoint:** `GET /api/generator/simple-api`  
**Params:**
- `projectName` (e.g., `"MyProject"`)
- `packageName` (e.g., `"com.example"`)

📌 **Example Request:**
```sh
curl -X GET "http://localhost:8080/api/generator/simple-api?projectName=MyProject&packageName=com.example"
```

This will return a **ZIP file** containing a fully structured Spring Boot API project.

---

## 🔜 Next Steps
🚀 Build a **front-end in Angular** for better usability.  
🚀 Integrate **AI-powered class generation** into complete API creation.  
🚀 Replace cloud-based AI with a **local AI model**, making code generation **faster, more secure, and fully independent**.

---

## 👨‍💻 Contributing
Pull requests are welcome! If you’d like to contribute:
1. **Fork the repository**.
2. **Create a new branch** (`git checkout -b feature-branch`).
3. **Commit your changes** (`git commit -m "Add new feature"`).
4. **Push the branch** (`git push origin feature-branch`).
5. **Open a Pull Request**.

---
