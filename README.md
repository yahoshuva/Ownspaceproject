# Ownspace

Ownspace is an online social media application designed to connect users with powerful features such as image detection, real-time chat, customizable profiles, and an image gallery.

## Features

- **Image Detection**: Automatically recognize and tag images uploaded by users.
- **Real-Time Chat**: Seamless messaging experience for users to communicate instantly.
- **User Profiles**: Personalized profiles with editable information.
- **Image Gallery**: Store, organize, and showcase user images.

## Technologies Used

- Java
- Spring Boot
- Postman (for API testing)
- Google CLI & Google SDK (for cloud integration and image detection)

## Installation

1. **Create the Project**
   - Use [Spring Initializr](https://start.spring.io/) to generate a new Spring Boot project.
   - Choose Java as the language and add required dependencies (Web, JPA, Security, etc.).

2. **Set Up Local Database**
   - Install and configure a local database (e.g., MySQL or PostgreSQL).
   - Update your `application.properties` or `application.yml` with the database connection details.

3. **Download Google CLI & SDK**
   - [Download Google Cloud CLI](https://cloud.google.com/sdk/docs/install)
   - Install the necessary components for image detection (e.g., Vision API).
   - Authenticate your CLI and set up your project as per Google Cloud documentation.

4. **API Testing**
   - Use Postman to test your RESTful endpoints.

## Usage

- Start the Spring Boot application locally.
- Access the web interface via `http://localhost:8080` (or configured port).
- Register a new user, update your profile, chat in real time, and upload images to test detection and gallery features.

