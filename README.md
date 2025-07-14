## Introduction

This program displays weather forecast information for South Korea.  
A personal API authentication key is required.  
You can receive an authentication key for free from [here](https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15084084).

Just run the program and enter your authentication key in the text box.  
If the authentication key is valid, the forecast information will be displayed.

## Development Environment

- Language: Java
- JDK: Adoptium Temurin JDK 21
- Build Tool: Maven

## How to Run

1. Build the project with Maven:
    ```
    mvn clean package
    ```
2. Run the application:
    ```
    java -jar target/weather-1.0.jar
    ```
3. Enter your authentication key when prompted.

## Configuration

- The authentication key is stored in a `.weather_config` file in your home directory.

## Project Structure

- `src/main/java/com/bang9634/` : Main application source code
- `src/main/java/com/bang9634/gui/` : GUI components
- `src/main/java/com/bang9634/util/` : Utility classes

## License

[MIT](LICENSE)