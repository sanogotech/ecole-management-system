# 📚 Ecole Management System

Système complet de gestion d'étudiants avec :

- ✅ Spring Boot 3.4 (Java 17)
- ✅ Thymeleaf pour l'interface utilisateur
- ✅ H2 database en mémoire
- ✅ JUnit 5 + Mockito + Selenium
- ✅ JaCoCo pour couverture de code (objectif 95%)
- ✅ Docker + Docker Compose

---

## 🚀 Démarrage

### 📦 Exécution classique

```bash
mvn clean spring-boot:run
````

### 🐳 Exécution via Docker

```bash
docker-compose up --build
```

---

## ✅ Accès

* Accueil : [http://localhost:8080](http://localhost:8080)
* Liste étudiants : [http://localhost:8080/etudiants](http://localhost:8080/etudiants)

---

## 🧪 Lancer les tests

```bash
mvn test
```

---

## 📊 Générer le rapport JaCoCo

```bash
mvn clean verify
# Rapport disponible dans : target/site/jacoco/index.html
```

---

## 🧪 Tester avec Selenium

> ChromeDriver requis : [https://chromedriver.chromium.org/](https://chromedriver.chromium.org/)

```bash
mvn test -Dtest=SeleniumUITest
```

---

## 📁 Structure recommandée

```
src/
 └── main/
     ├── java/com/ecole/
     └── resources/templates/
 └── test/
     ├── java/com/ecole/service/
     └── java/com/ecole/selenium/
```

---

## 🛠 Améliorations futures

* Authentification (Spring Security)
* PostgreSQL (via Docker)
* Pagination et tri
* API REST complète

```



