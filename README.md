
# Henri Trip — Backend

API REST de gestion de guides touristiques développée avec Java 21 et Spring Boot.

## Technologies

- Java 21
- Spring Boot
- Spring Security et JWT
- Spring Data JPA
- MariaDB
- Maven

## Fonctionnalités

### Administrateur
- Gestion des utilisateurs
- Création, modification et suppression des guides
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
- Maven (ou Maven Wrapper)
- MariaDB

### Configuration

Créer une base MariaDB :

```sql
CREATE DATABASE henri_trip;
```

Configurer la connexion à la base dans `application.properties` ou via les variables d'environnement prévues par l'application.

Définir la variable d'environnement `JWT_SECRET` avec une clé secrète suffisamment longue.

Exemple PowerShell :

```powershell
$env:JWT_SECRET = "remplacer-par-une-cle-secrete-aleatoire-de-32-caracteres-minimum"
```

### Démarrage

```powershell
.\mvnw.cmd spring-boot:run
```

L'API est accessible sur `http://localhost:8080`.

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

Les routes d'administration nécessitent le rôle ADMIN.

## Tests

```powershell
.\mvnw.cmd clean verify
```

## Frontend

Application Angular disponible dans un dépôt séparé.
