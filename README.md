# Weather Forecast GUI Application

## Introduction

This program displays weather forecast information for South Korea using the official public API.  
A personal API authentication key is required.  
You can receive an authentication key for free from [here](https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15084084).

- Select your region, city, and district using the combo boxes.
- The weather forecast for the selected area will be displayed in real time.

## Development Environment

- Language: Java
- JDK: Adoptium Temurin JDK 21
- Build Tool: Maven

## How to Run

1. **Build the project with Maven:**
    ```
    mvn clean package
    ```
2. **Run the application:**
    ```
    java -jar target/weather-1.0.jar
    ```
3. **Enter your authentication key** when prompted.

## Features

- GUI-based weather forecast viewer for South Korea
- Region/city/district selection via combo boxes
- Real-time weather data retrieval from the public API
- Authentication key management (stored in your home directory)

## Configuration

- The authentication key is stored in a `.weather_config` file in your home directory.

## Project Structure

- `src/main/java/com/bang9634/` : Main application source code
- `src/main/java/com/bang9634/gui/` : GUI components
- `src/main/java/com/bang9634/util/` : Utility classes
- `src/main/java/com/bang9634/model/` : Data model classes

## License

[MIT](LICENSE)