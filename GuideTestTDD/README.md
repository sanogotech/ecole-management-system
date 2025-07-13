# 🧪 Guide Complet des Tests en Java Spring Boot (TDD, JUnit, Mockito, JaCoCo, Selenium)

---

## 1. 🎯 Introduction à TDD (Test Driven Development)

**Principe** : écrire les tests **avant** le code pour guider la conception.

---

### Exemple 1 : Test unitaire simple (Red - Green - Refactor)

```java
// Given: création d'un étudiant
Etudiant e = new Etudiant("Ali", "Diop");

// When: on ajoute l’étudiant via le service
Etudiant saved = service.ajouter(e);

// Then: on vérifie que le nom est correct
assertEquals("Ali", saved.getNom());
```

*Commentaire* : ce test sera d'abord rouge (méthode non implémentée), puis on écrira le code minimal pour le passer (vert), enfin on pourra refactorer.

---

### Exemple 2 : Test d'exception (gestion d'erreur)

```java
@Test
void shouldThrowExceptionWhenEtudiantSansNom() {
    Etudiant e = new Etudiant(null, "Diop");

    // On s'attend à ce que l'ajout d'un étudiant sans nom lance une exception
    assertThrows(IllegalArgumentException.class, () -> service.ajouter(e));
}
```

*Commentaire* : ce test assure que les règles métier sont respectées (RG : un étudiant doit avoir un nom).

---

### Exemple 3 : Test paramétré avec JUnit 5

```java
@ParameterizedTest
@CsvSource({
    "Ali, Diop",
    "Fatou, Ndiaye",
    "John, Doe"
})
void shouldAddMultipleEtudiants(String nom, String prenom) {
    Etudiant e = new Etudiant(nom, prenom);
    when(repo.save(e)).thenReturn(e);

    Etudiant saved = service.ajouter(e);
    assertEquals(nom, saved.getNom());
}
```

*Commentaire* : test efficace pour vérifier plusieurs cas sans dupliquer le code.

---

## 2. 🧪 Tests unitaires avec Mockito

---

### Exemple 1 : Mock du repository et vérification d'appel

```java
@Test
void shouldCallSaveOnRepository() {
    Etudiant e = new Etudiant("Ali", "Diop");
    when(repo.save(e)).thenReturn(e);

    service.ajouter(e);

    // Vérifie que repo.save(e) a bien été appelé exactement une fois
    verify(repo, times(1)).save(e);
}
```

---

### Exemple 2 : Simuler un retour vide

```java
@Test
void shouldReturnEmptyListWhenNoEtudiants() {
    when(repo.findAll()).thenReturn(Collections.emptyList());

    List<Etudiant> etudiants = service.lister();

    assertTrue(etudiants.isEmpty());
}
```

*Commentaire* : on contrôle le comportement quand la base est vide.

---

### Exemple 3 : Vérifier suppression par ID

```java
@Test
void shouldDeleteEtudiantById() {
    Long id = 1L;

    service.supprimer(id);

    verify(repo).deleteById(id);
}
```

*Commentaire* : ce test assure que la suppression appelle la bonne méthode du repo.

---

## 3. 🔄 Tests d’intégration avec MockMvc

---

### Exemple 1 : Tester la page liste étudiants

```java
@Test
void shouldReturnEtudiantListView() throws Exception {
    when(service.lister()).thenReturn(List.of(new Etudiant("Ali", "Diop")));

    mockMvc.perform(get("/etudiants"))
        .andExpect(status().isOk())
        .andExpect(view().name("etudiants/list"))
        .andExpect(model().attributeExists("etudiants"))
        .andExpect(content().string(containsString("Ali")));
}
```

*Commentaire* : on teste à la fois le rendu HTML et la présence du modèle.

---

### Exemple 2 : Tester la page formulaire d’ajout

```java
@Test
void shouldShowNewEtudiantForm() throws Exception {
    mockMvc.perform(get("/etudiants/nouveau"))
        .andExpect(status().isOk())
        .andExpect(view().name("etudiants/form"));
}
```

*Commentaire* : simple vérification d’accès à la page de formulaire.

---

### Exemple 3 : Soumission de formulaire POST

```java
@Test
void shouldAddEtudiantAndRedirect() throws Exception {
    mockMvc.perform(post("/etudiants")
            .param("nom", "Ali")
            .param("prenom", "Diop"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/etudiants"));
}
```

*Commentaire* : on simule la soumission et la redirection attendue.

---

## 4. 👁️ Tests UI automatisés avec Selenium WebDriver

---

### Exemple 1 : Accéder à la page et vérifier le titre

```java
@Test
void shouldLoadEtudiantPage() {
    driver.get("http://localhost:8080/etudiants");
    assertEquals("Liste des étudiants", driver.getTitle());
}
```

*Commentaire* : test simple vérifiant le chargement correct.

---

### Exemple 2 : Remplir le formulaire et soumettre

```java
@Test
void shouldAddEtudiantFromUI() {
    driver.get("http://localhost:8080/etudiants/nouveau");

    driver.findElement(By.name("nom")).sendKeys("Ali");
    driver.findElement(By.name("prenom")).sendKeys("Diop");
    driver.findElement(By.tagName("button")).click();

    assertTrue(driver.getPageSource().contains("Ali"));
}
```

*Commentaire* : simule un vrai utilisateur ajoutant un étudiant.

---

### Exemple 3 : Cliquer sur un lien et vérifier redirection

```java
@Test
void shouldNavigateToFormPage() {
    driver.get("http://localhost:8080/etudiants");

    WebElement link = driver.findElement(By.linkText("Ajouter un étudiant"));
    link.click();

    assertEquals("Ajouter un étudiant", driver.getTitle());
}
```

*Commentaire* : vérifie les liens et navigation.

---

## 5. 📊 Couverture de code avec JaCoCo

---

### Exemple 1 : Configuration basique dans pom.xml

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>verify</phase>
            <goals><goal>report</goal></goals>
        </execution>
    </executions>
</plugin>
```

*Commentaire* : agent JaCoCo s’injecte au moment des tests, rapport généré après.

---

### Exemple 2 : Exécution du rapport

```bash
mvn clean verify
# puis ouvrir target/site/jacoco/index.html dans un navigateur
```

*Commentaire* : commande Maven standard pour tests + rapport.

---

### Exemple 3 : Analyse d’un rapport JaCoCo

* Instructions couvertes : 97%
* Branches couvertes : 85%
* Méthodes couvertes : 92%
* Classes couvertes : 100%

*Commentaire* : viser ≥ 95% sur instructions pour une qualité optimale.

---

## 6. 🛠️ Bonnes pratiques générales

---

### Exemple 1 : Nommage clair des tests

```java
@Test
void shouldReturnEmptyListWhenNoEtudiants() { ... }
```

*Commentaire* : décrit clairement ce que teste la méthode.

---

### Exemple 2 : Utilisation de `@BeforeEach` pour préparer le contexte

```java
@BeforeEach
void setup() {
    etudiant = new Etudiant("Ali", "Diop");
}
```

*Commentaire* : évite duplication de code dans chaque test.

---

### Exemple 3 : Isoler les tests

* Les tests unitaires ne doivent pas accéder à la base réelle.
* Utiliser Mockito pour simuler les dépendances.
* Garder les tests rapides et indépendants.

---

## 7. ⚙️ Intégration continue (GitHub Actions)

---

### Exemple 1 : Workflow simple

```yaml
name: CI Spring Boot Tests

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
      - run: mvn clean verify
```

---

### Exemple 2 : Rapport JaCoCo dans CI

Ajouter un job qui analyse les résultats et bloque la pipeline si couverture trop faible.

---

### Exemple 3 : Notifications Slack ou Email

Configurer un step pour envoyer un résumé du build.

---

## 8. 🐳 Dockerisation & tests dans container

---

### Exemple 1 : Dockerfile simple

```Dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

### Exemple 2 : docker-compose.yml basique

```yaml
version: '3'
services:
  app:
    build: .
    ports:
      - "8080:8080"
```

---

### Exemple 3 : Exécution et test

```bash
mvn clean package
docker-compose up --build
# Puis lancer Selenium pour tester l’UI dans le container
```

---

# Conclusion

Avec ces nombreux exemples détaillés et commentés, vous avez désormais une vision 360° de la mise en œuvre des tests dans un projet Spring Boot.

---



# 🚀 Prochaines pistes d’amélioration détaillées pour un projet Spring Boot testé

---

## 1. Ajout de Spring Security

**Pourquoi l’ajouter ?**
La sécurité est une exigence incontournable dans toute application moderne. Spring Security permet de gérer finement l’authentification (qui est l’utilisateur ?) et l’autorisation (à quoi a-t-il accès ?).

**Avantages :**

* Protection des API REST et des pages web.
* Support des mécanismes standards : login form, JWT, OAuth2, LDAP, SSO.
* Facilité à écrire des tests pour vérifier la sécurité des endpoints.

**Exemple de test sécurisé avec MockMvc :**

```java
@Test
@WithMockUser(username="admin", roles={"ADMIN"})
void shouldAllowAdminAccess() throws Exception {
    mockMvc.perform(get("/admin/dashboard"))
           .andExpect(status().isOk());
}

@Test
void shouldDenyAnonymousAccess() throws Exception {
    mockMvc.perform(get("/admin/dashboard"))
           .andExpect(status().isUnauthorized());
}
```

**Conseils pratiques :**

* Testez systématiquement avec un utilisateur authentifié et un utilisateur anonyme.
* Utilisez `@WithUserDetails` pour simuler des utilisateurs existants dans la base.
* Ne désactivez pas la sécurité dans les tests d’intégration, sauf exception justifiée.

---

## 2. Cucumber avec Gherkin

**Pourquoi utiliser Cucumber ?**
Pour améliorer la collaboration entre développeurs, testeurs et métiers. Grâce au langage naturel Gherkin, tout le monde peut comprendre, écrire et valider les scénarios de tests.

**Avantages :**

* Documentation vivante, à jour, qui sert aussi de base pour les tests automatisés.
* Facilite la compréhension des exigences métier.
* Intégration possible avec Selenium pour des tests UI réels.

**Exemple de scénario Gherkin :**

```gherkin
Feature: Gestion des étudiants

Scenario: Ajouter un étudiant avec succès
  Given Je suis sur la page d'ajout d'étudiant
  When Je saisis "Ali" dans le champ nom
  And Je saisis "Diop" dans le champ prénom
  And Je soumets le formulaire
  Then Je vois "Ali Diop" dans la liste des étudiants
```

**Exemple d’implémentation Java d’une étape :**

```java
@When("Je saisis {string} dans le champ nom")
public void je_saisis_nom(String nom) {
    driver.findElement(By.name("nom")).sendKeys(nom);
}
```

**Conseils pratiques :**

* Rédigez des scénarios courts et clairs, évitez les détails techniques.
* Utilisez des tags pour organiser vos scénarios et cibler des suites spécifiques.
* Intégrez les scénarios dans vos pipelines CI pour un retour rapide.

---

## 3. TestContainers pour PostgreSQL

**Pourquoi TestContainers ?**
Les tests unitaires avec mocks ne suffisent pas toujours, et les bases en mémoire comme H2 ne reproduisent pas toujours fidèlement le comportement d’une vraie base SQL. TestContainers lance une instance PostgreSQL dans un container Docker, offrant une base réelle temporaire pour les tests.

**Avantages :**

* Isolation totale des tests.
* Réalisme des tests d’intégration, avec vrai moteur SQL.
* Support des migrations Flyway/liquibase.
* Intégration transparente avec Spring Boot via propriétés dynamiques.

**Exemple d’utilisation :**

```java
@Container
static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
    .withDatabaseName("testdb")
    .withUsername("user")
    .withPassword("pass");

@DynamicPropertySource
static void registerPgProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
}
```

**Conseils pratiques :**

* Assurez-vous que Docker est installé sur les machines locales et CI.
* Limitez la taille et la durée de vie des données pour accélérer les tests.
* Utilisez les transactions dans les tests pour rollback automatique.

---

## 4. Intégration SonarQube

**Pourquoi SonarQube ?**
Pour garantir la qualité du code sur le long terme. SonarQube analyse automatiquement votre code à chaque build, détecte bugs, failles de sécurité, duplications, complexité excessive, et mesure la couverture des tests.

**Avantages :**

* Alertes précoces pour corriger avant que les problèmes deviennent critiques.
* Suivi continu de la dette technique.
* Intégration dans les pipelines CI/CD avec règles personnalisables.
* Rapport détaillé et visuel accessible à toute l’équipe.

**Exemple d’intégration GitHub Actions :**

```yaml
- name: SonarQube Scan
  uses: SonarSource/sonarcloud-github-action@v1
  with:
    projectKey: your_project_key
    organization: your_org
    token: ${{ secrets.SONAR_TOKEN }}
```

**Conseils pratiques :**

* Adoptez des règles progressives pour ne pas décourager l’équipe.
* Automatisez l’analyse pour chaque pull request.
* Exploitez les rapports pour planifier des refactorings ciblés.

---

# Résumé

| **Évolution**      | **Quand ?**                                         | **Ce que cela apporte**                        |
| ------------------ | --------------------------------------------------- | ---------------------------------------------- |
| Spring Security    | Dès que la sécurité devient essentielle             | Protection, contrôle d’accès et tests associés |
| Cucumber + Gherkin | Pour impliquer le métier et clarifier les exigences | Collaboration, documentation vivante           |
| TestContainers     | Pour des tests d’intégration réalistes              | Fiabilité, isolation et réalisme               |
| SonarQube          | Dès le début ou lors de l’intégration continue      | Qualité, sécurité, maintenabilité              |

---

# Pièges à éviter

* **Spring Security** : Ne désactivez pas la sécurité dans les tests d’intégration, testez toujours les scénarios réels.
* **Cucumber** : Ne créez pas de scénarios trop longs et techniques, restez focus métier.
* **TestContainers** : Attention au temps de démarrage des containers, limitez le parallélisme si ressources limitées.
* **SonarQube** : Ne surchargez pas les règles au départ ; déployez progressivement pour ne pas freiner l’équipe.

---

# Conclusion

Ces pistes vous permettront d’élever la qualité, la sécurité, la collaboration, et la fiabilité de votre projet Spring Boot. Elles couvrent l’ensemble du cycle de vie, du développement à la production, avec des outils modernes et éprouvés.

---



