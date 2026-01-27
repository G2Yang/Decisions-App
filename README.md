# 🎮 Decisions – Social Decision-Making App  

> **Turn everyday choices into fun, social mini-games — without the arguments!**  

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Java-blue" alt="Language">
  <img src="https://img.shields.io/badge/Backend-Javalin-orange" alt="Backend">
  <img src="https://img.shields.io/badge/Database-MySQL-red" alt="Database">
  <img src="https://img.shields.io/badge/Real%20Time-Firebase-yellow" alt="Firebase">
  <img src="https://img.shields.io/badge/License-CC--BY--NC--SA-lightgrey" alt="License">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Version-1.0.0-brightgreen" alt="Version">
  <img src="https://img.shields.io/badge/Status-90%25%20Complete-orange" alt="Status">
  <img src="https://img.shields.io/badge/Scrum-5%20Sprints-blueviolet" alt="Methodology">
</p>

---

## 📖 Table of Contents  
- [🎯 Purposes](#-purposes)  
- [📱 Description](#-description)  
- [👥 Team](#-team)  
- [⚙️ Technical Specifications](#️-technical-specifications)  
- [🏗️ Architecture](#️-architecture)  
- [🎮 Features](#-features)  
- [📊 Diagrams](#-diagrams)  
- [🚀 Installation](#-installation)  
- [📋 Usage](#-usage)  
- [🧪 Testing](#-testing)  
- [📈 Methodology](#-methodology)  
- [🔮 Future Improvements](#-future-improvements)  
- [📜 License](#-license)  
- [🙏 Acknowledgments](#-acknowledgments)  

---

## 🎯 Purposes  
*(Ref: Page 3)*  

The main goals of our application are:  
- **Connect with friends** – Build a lightweight social network for playful interaction.  
- **Play mini-games to make decisions** – Turn decision-making into quick, fun games.  
- **View friends' decisions** – See what your friends choose in a shared feed.  
- **Decide without discussions** – Avoid conflicts by letting a game decide.  

---

## 📱 Description  
*(Ref: Page 4)*  

**Decisions** is a mobile app that helps you make choices through quick, entertaining mini-games. Whether you're deciding with a friend or alone, the app picks a random game, determines a winner, and lets the winner share their decision with a photo.  

---

## 👥 Team  
*(Ref: Page 5)*  

| Role | Member |
|------|--------|
| **UI/UX, Animations, Games** | David Chaparro |
| **Backend, Database, Email** | Ismael Maridueña |
| **Online Features, Real-time Sync** | Zhi Yang Wu |

**Institution:** Institut Provençana – DAM-2  

---

## ⚙️ Technical Specifications  
*(Ref: Page 5)*  

### 🖥️ Server  
| Component | Requirement |
|-----------|-------------|
| **OS** | Ubuntu 22.04 |
| **RAM** | 6 GB (min) |
| **Storage** | 50 GB → 20 TB (scalable) |
| **Database** | MySQL 8.0 |
| **Backend** | Javalin 3.3.0 |

### 📱 Client  
| Component | Requirement |
|-----------|-------------|
| **OS** | Android 10+ |
| **Storage** | 16 GB |
| **RAM** | 4 GB |

### 💻 Developer  
| Component | Requirement |
|-----------|-------------|
| **OS** | Windows 10+ |
| **IDE** | Android Studio |
| **RAM** | 8 GB |
| **Storage** | 500 GB (recommended) |

---

## 🏗️ Architecture  
*(Ref: Page 6)*  




- **User:** Interacts via Android app.  
- **Mobile App:** Built in Java with Android Studio.  
- **Server:** Javalin-based REST API.  
- **Database:** MySQL for users, friendships, matches, and photos.  
- **Real-time:** Firebase for synchronized gameplay.  

---

## 🎮 Features  
*(Ref: Pages 8–15)*  

### ✅ Functional Requirements  

| ID | Feature | Description |
|----|---------|-------------|
| RF1 | Sign Up | Register with email, password, username |
| RF2 | Log In | Authenticate with email/password |
| RF3 | Recover Password | Email-based password reset |
| RF4 | View Interface | User-friendly, intuitive UI |
| RF5 | Add Friends | Search and send friend requests |
| RF6 | Manage Friend Requests | Accept/reject incoming requests |
| RF7 | Remove Friends | Delete friends from list |
| RF8 | Connect with Friend | Chat and interact after acceptance |
| RF9 | Play Random Minigame (Friend) | Real-time multiplayer games |
| RF10 | Play vs Robot | Solo play against AI |
| RF11 | Minigame: Elements | Water 🔥 Ice ❄️ game |
| RF12 | Minigame: Penalties | Soccer penalty shootout |
| RF13 | Minigame: Question Quiz | Trivia with timer and lives |
| RF14 | Take a Photo | Custom in-app camera |
| RF15 | Share on Social Feed | Post decision with photo |
| RF16 | Change Profile Data | Edit username, email, password |
| RF17 | Log Out | Secure session closure |
| RF18 | Change Language | 7 supported languages |
| RF19 | Games Guide | In-app tutorial for each game |

### 📈 Non-Functional Requirements  

| ID | Requirement | Description |
|----|-------------|-------------|
| NRF1 | Usability | Intuitive, responsive UI |
| NRF2 | Performance | Fast loading, handles many users |
| NRF3 | Security | Secure auth, SQL injection/XSS protection |
| NRF4 | Reliability | Error handling, backup systems |
| NRF5 | Compatibility | Works on various devices/OS |
| NRF6 | Scalability | Scales with user growth |

---

## 📊 Diagrams  
*(Ref: Pages 16–18, 33–34)*  

- **Class Diagram** – Object-oriented structure of the app.  
- **ER Diagram** – Database entity relationships.  
- **Use Case Diagram** – User interactions and system responses.  
- **Architecture Diagram** – High-level system overview.  

> *Note: Diagrams are available in the project documentation PDF.*

---

## 🚀 Installation  
*(Ref: Pages 60–70)*  

### 1. Server Setup  

```bash
# Update packages
sudo apt update

# Install MySQL
sudo apt install mysql-server

# Secure installation
sudo mysql_secure_installation

# Create database and user
CREATE DATABASE decisionsdb;
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'Admin123';
GRANT ALL PRIVILEGES ON decisionsdb.* TO 'admin'@'localhost';


# Upload JAR via FileZilla (SSH)
# Run as service
sudo systemctl enable javelin.service
sudo systemctl start javelin.service

```

Clone the repository

bash
```bash
git clone https://github.com/your-username/decisions-app.git
```
Open in Android Studio

Launch Android Studio

Select "Open an Existing Project"

Navigate to the cloned repository folder

Sync Gradle Dependencies

Wait for Gradle to sync automatically, or click "Sync Now" if prompted

Build and Run

Connect an Android device (min. Android 10) or start an emulator

Click the "Run" button (green play icon) or press Shift + F10

Configure Network (Ref: Page 72)

Ensure the server IP is correctly set in the app's configuration files

Open required ports on the server:
```bash
sudo ufw allow 7070  # Javalin port
sudo ufw allow 22    # SSH port
```


## 📋 Usage
## 👤 User Flow
Sign up / Log in – Create an account or authenticate

Add friends – Search users and send friend requests

Choose mode – Select "1vs1" (with friend) or "1vsRobot" (solo)

Enter decision – Type what you're deciding about

Play mini-game – Compete in randomly selected game

Share outcome – Winner can take and post a photo

Browse feed – Scroll through your and friends' decisions

## ⚙️ Settings Menu
Edit Profile – Update username, email, password

Change Language – 7 languages available

Games Guide – Tutorial for each mini-game

Log Out – End session securely

## 🧪 Testing
## ✅ Test Coverage
Module	Test Cases	Status
Authentication	Registration, Login, Password Recovery	✅ Complete
Friends System	Add, Remove, Request Management	✅ Complete
Mini-games	Elements, Penalties, Question Quiz	✅ Complete
Camera & Photos	Permissions, Capture, Upload	✅ Complete
Network	Online Play, Sync, Error Handling	✅ Complete
🧠 Key Validations
Username and email uniqueness

Password strength (min. 5 characters)

Email format validation (@ and domain)

Friend request mutual acceptance logic

Game timeout and AFK handling

## 📈 Methodology
# 🏃 Agile/Scrum Framework
Sprint |	Duration |	Focus	Completed
|----|-------------|-------------|
Sprint 1| 2 weeks |	Auth, UI, Database	✅ 100% |
Sprint 2|	2 weeks|	Friends, Settings, Mini-games	✅ 95%|
Sprint 3|	2 weeks	|Online Connectivity, Game Sync	✅ 90%|
Sprint 4|	2 weeks	|Photo Sharing, Password Recovery	✅ 85%|
Sprint 5|	2 weeks|	Polish, Bug Fixes, Documentation	✅ 90%|

# 📋 Product Backlog
Priority	User Stories
Critical	Register, Login, Main Interface
Major	Online Play, Decision Input, Friend System
Normal	Settings, Mini-games, Password Recovery
Minor	Tutorial, App Close, Language Change

## 🔮 Future Improvements
Priority	Feature	Justification
High	User Statistics Dashboard	Track wins/losses, decision history
High	Push Notifications	For game invites and friend requests
Medium	More Mini-games	Expand variety (e.g., "CowBoy" game)
Medium	Social Features	Comments, likes, external sharing
Low	Advanced Customization	Themes, avatars, game rules

## 📜 License
This project is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike (CC BY-NC-SA).
See LICENSE for details.

You are free to:

Share – copy and redistribute the material

Adapt – remix, transform, and build upon the material

Under the following terms:

Attribution – You must give appropriate credit

NonCommercial – You may not use the material for commercial purposes

ShareAlike – If you remix, you must distribute under the same license

## 🙏 Acknowledgments
Institut Provençana – For the DAM-2 course guidance and resources

Mentors & Professors – For technical and methodological support

Open-Source Community – For Javalin, OkHTTP, Firebase, and Android tools

Development Team – David, Ismael, and Yang for dedication and collaboration

<p align="center"> <strong>Decisions – Because choosing should be fun, not frustrating.</strong><br> <i>Developed with ❤️ for the DAM-2 Final Project</i> </p> ```
