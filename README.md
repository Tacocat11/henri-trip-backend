# Henri Trip — Backend

API REST de gestion de guides touristiques développée avec Java 21 et Spring Boot.

## Technologies

- Java 21
- Spring Boot
- Spring Security et JWT
- Spring Data JPA / Hibernate
- MariaDB
- Maven

## Fonctionnalités

### Administrateur

- Gestion des utilisateurs
- Création, modification et suppression des guides touristiques
- Gestion des activités touristiques
- Organisation des activités par jour et ordre de visite
- Gestion des invitations aux guides

### Utilisateur

- Authentification
- Consultation des guides auxquels il est invité
- Consultation des activités et du programme des guides

## Installation

### Prérequis

- Java 21
- Maven (ou Maven Wrapper fourni)
- MariaDB
- Node.js et npm pour le frontend Angular

### Configuration

**Base de données**

L'application nécessite une instance MariaDB accessible.

Créer une base de données nommée `henri_trip` :

```sql
CREATE DATABASE henri_trip
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

Adapter les paramètres de connexion dans `src/main/resources/application.properties` ou utiliser les variables d'environnement Spring Boot correspondantes :

| Propriété | Variable d'environnement | Valeur par défaut du projet |
|---|---|---|
| `spring.datasource.url` | `SPRING_DATASOURCE_URL` | `jdbc:mariadb://localhost:3306/henri_trip` |
| `spring.datasource.username` | `SPRING_DATASOURCE_USERNAME` | `root` |
| `spring.datasource.password` | `SPRING_DATASOURCE_PASSWORD` | Vide |

Les tables sont automatiquement créées au premier démarrage par Hibernate grâce à la configuration `spring.jpa.hibernate.ddl-auto=update`.

**Authentification JWT**

L'application utilise des tokens JWT pour l'authentification.

Définir la variable d'environnement `JWT_SECRET` avec une clé secrète aléatoire d'au moins 32 octets avant de démarrer le backend.

Exemple sous PowerShell :

```powershell
$env:JWT_SECRET = "votre-cle-secrete-aleatoire-de-32-caracteres-minimum"
```

Sous Linux/macOS :

```bash
export JWT_SECRET="votre-cle-secrete-aleatoire-de-32-caracteres-minimum"
```

La clé JWT ne doit pas être enregistrée dans le dépôt Git.

### Démarrage

À la racine du projet :

**Linux/macOS :**

```bash
./mvnw spring-boot:run
```

**Windows :**

```powershell
.\mvnw.cmd spring-boot:run
```

L'API est accessible par défaut sur `http://localhost:8080`.

## Données de démonstration

Le fichier `demo-data.sql` fournit un jeu de données permettant de tester les fonctionnalités de l'application.

Il contient :

- 1 compte administrateur
- 2 comptes utilisateurs
- 2 guides touristiques : *Week-end à Toulouse* et *Découverte du Tarn*
- 8 activités touristiques
- Des activités réparties sur plusieurs jours avec un ordre de visite
- Des invitations permettant de tester les droits d'accès aux guides

### Initialisation

1. Créer la base MariaDB `henri_trip`.
2. Configurer la connexion à MariaDB et la variable `JWT_SECRET`.
3. Démarrer le backend une première fois pour que Hibernate crée les tables.
4. Importer le fichier `demo-data.sql` dans la base `henri_trip` à l'aide d'un client MariaDB.
5. Démarrer le frontend Angular et se connecter avec un compte de démonstration.

Le script est conçu pour pouvoir être réexécuté sans dupliquer les données de démonstration existantes.

### Comptes de démonstration

| Rôle | Email | Mot de passe |
|---|---|---|
| Administrateur | `admin@henritrip.fr` | `AdminDemo2026!` |
| Utilisateur | `alice.demo@henritrip.fr` | `UserDemo2026!` |
| Utilisateur | `thomas.demo@henritrip.fr` | `UserDemo2026!` |

Ces identifiants sont destinés exclusivement à un environnement local de démonstration.

### Parcours de test suggéré

**Administrateur**

- Se connecter avec le compte administrateur.
- Consulter, créer, modifier et supprimer des guides.
- Gérer les activités et leur programmation.
- Gérer les utilisateurs et les invitations.

**Utilisateur**

- Se connecter avec Alice ou Thomas.
- Consulter les guides auxquels le compte est invité.
- Consulter les activités et leur organisation par jour.
- Vérifier les restrictions d'accès aux fonctionnalités d'administration.

## Principales routes API

| Méthode | Route | Description |
|---|---|---|
| POST | `/api/auth/login` | Authentification |
| GET | `/api/my/guides` | Guides accessibles à l'utilisateur |
| GET | `/api/my/guides/{id}` | Détail d'un guide accessible |
| GET/POST | `/api/guides` | Consultation et création de guides |
| PUT/DELETE | `/api/guides/{id}` | Modification et suppression |
| GET/POST | `/api/activities` | Consultation et création d'activités |
| PUT/DELETE | `/api/activities/{id}` | Modification et suppression |
| GET/POST | `/api/users` | Consultation et création d'utilisateurs |
| DELETE | `/api/users/{id}` | Suppression d'utilisateur |
| GET/POST | `/api/guides/{id}/invitations` | Gestion des invitations |

Les routes d'administration nécessitent une authentification avec le rôle `ADMIN`.

Les routes utilisateur nécessitent une authentification et appliquent les restrictions d'accès aux guides.

## Tests

Pour exécuter les tests automatisés du backend :

**Linux/macOS :**

```bash
./mvnw clean verify
```

**Windows :**

```powershell
.\mvnw.cmd clean verify
```

## Frontend Angular

L'interface utilisateur est développée avec Angular et disponible dans un dépôt séparé :

**https://github.com/Tacocat11/henri-trip-frontend**

Après avoir récupéré le projet frontend :

```bash
npm install
npm start
```

L'application est accessible par défaut sur `http://localhost:4200`.

Le backend doit être démarré et accessible sur `http://localhost:8080` pour permettre les échanges avec l'API.

## Notes techniques

- L'authentification repose sur Spring Security et des tokens JWT.
- Les mots de passe des utilisateurs sont stockés sous forme de hash BCrypt.
- Les droits d'accès sont gérés selon les rôles `ADMIN` et `USER`.
- La persistance des données repose sur Spring Data JPA et Hibernate.
- La configuration `ddl-auto=update` est utilisée pour simplifier l'initialisation de la base dans le cadre du test technique. Pour un environnement de production, un système de migrations versionnées serait préférable.
