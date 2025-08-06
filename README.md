# 🚀 Java + TestNG Selenium Framework

Este repositorio contiene un **framework base de automatización** construido para practicar y demostrar buenas prácticas en Java.  
Tras clonar el proyecto, basta ejecutar:

```
# clona el repositorio
git clone https://github.com/<tu-usuario>/<tu-repo>.git
cd <repositorio>

# compila y lanza la suite completa
mvn test
```
## 🌟 Features

| Categoría            | Características clave |
|----------------------|------------------------|
| **Arquitectura**     | • Patrón **Page Object Model (POM)**.<br>• `BasePage` con helpers (`click`, `write`, `read`). |
| **Driver Management**| • `DriverFactory` crea **una única** instancia de WebDriver.<br>• Soporta **Chrome / Firefox / Edge** con **WebDriverManager**.<br>• Flags **headless** (`headless=true`) y ajuste de ventana **1920×1080**. |
| **Sincronización**   | • Esperas explícitas centralizadas (`Waits`).<br>• Eliminación de _flakiness_ al interactuar con elementos dinámicos. |
| **Configuración externa** | • `config.properties` gobierna browser, URL base, time-outs, headless, etc. |
| **Reportes**         | • **ExtentReports 5** – HTML con capturas automáticas en fallos.<br>• Logs paso a paso visibles en el mismo informe. |
| **Logging**          | • Acceso al objeto `ExtentTest` desde cualquier test (`ExtentTestListener.getTest()`). |
| **Repositorio limpio** | • `.gitignore` excluye `/target`, `/reports`, binarios y metadatos IDE.<br>• Maven Wrapper (`mvnw`, `.mvn/`) versionado → **clona y corre**. |
| **Escalabilidad futura** | • Estructura lista para componentes reutilizables (header, modales).<br>• Se puede extender a **Selenium Grid** o **Docker** cuando sea necesario. |
