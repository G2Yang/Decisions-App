<div align="center">

# 🎯 Decisions App

### An iOS app to make choices easier, developed with SwiftUI and Firebase.

![Swift](https://img.shields.io/badge/Swift-5.9-FA7343?logo=swift&logoColor=white)
![Platform](https://img.shields.io/badge/platform-android-000000?logo=apple&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-Cloud%20Service-FFCA28?logo=firebase)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

*A sleek and intuitive decision-making tool for your pocket.*

[![GitHub stars](https://img.shields.io/github/stars/G2Yang/Decisions-App?style=social)](https://github.com/G2Yang/Decisions-App/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/G2Yang/Decisions-App?style=social)](https://github.com/G2Yang/Decisions-App/network/members)

</div>

## 📋 Table of Contents
- [✨ Overview](#-overview)
- [🚀 Features](#-features)
- [🛠️ Built With](#️-built-with)
- [📸 Screenshots](#-screenshots)
- [⚙️ Getting Started](#️-getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [📖 Usage](#-usage)
- [🏗️ Project Structure](#️-project-structure)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)
- [✉️ Contact](#️-contact)

## ✨ Overview

**Decisions App** is a native iOS application designed to help users make everyday choices quickly and with a touch of fun. Whether you're deciding on a restaurant, a movie, or what task to tackle next, this app provides a visual and interactive way to pick an option from a customizable list.

The app demonstrates clean architecture, modern iOS development practices with **SwiftUI**, and seamless backend integration using **Firebase** for data synchronization across devices.

### 🎯 Why Decisions App?
- **Decision Fatigue Solution**: Makes trivial decisions effortless
- **Visual & Interactive**: Engaging wheel animation instead of boring lists
- **Cross-Device Sync**: Access your decision wheels anywhere
- **Privacy-Focused**: Your data stays yours

## 🚀 Features

| Feature | Description |
|---------|-------------|
| **🎡 Interactive Decision Wheel** | Spin a beautifully animated wheel to make random selections with smooth physics |
| **📝 Smart List Management** | Create, edit, and organize multiple decision categories with intuitive gestures |
| **☁️ Real-time Cloud Sync** | Automatic synchronization via Firebase (lists sync across all your Apple devices) |
| **🎨 Customizable Interface** | Choose wheel colors, sizes, and animations to match your style |
| **📊 Decision History** | Track past choices and outcomes for recurring decisions |
| **🔔 Haptic Feedback** | Premium tactile responses (Taptic Engine) for satisfying interactions |
| **🔍 Search & Filter** | Quickly find specific options in large decision lists |
| **📤 Share Lists** | Export and share your decision wheels with friends and family |
| **🌙 Dark Mode** | Full support for iOS Dark Mode with automatic switching |

## 🛠️ Built With

### **Frontend**
- **Language**: Swift 5.9
- **UI Framework**: SwiftUI
- **Architecture**: MVVM (Model-View-ViewModel)
- **Animations**: SwiftUI Animations + Custom Core Animation

### **Backend & Services**
- **Database**: Firebase Firestore (NoSQL, real-time)
- **Authentication**: Firebase Auth (Email/Password, Apple Sign-In)
- **Storage**: Firebase Storage (for user media)
- **Analytics**: Firebase Analytics

### **Development Tools**
- **IDE**: Xcode 15+
- **Dependency Management**: Swift Package Manager (SPM)
- **Version Control**: Git + GitHub
- **Design**: Figma (UI/UX prototyping)

### **Libraries & Dependencies**
```swift
// Main dependencies used in the project
- FirebaseFirestoreSwift  // Firestore with Codable support
- FirebaseAuth            // Authentication
- Lottie                  // Advanced animations (if used)
- SwiftUICharts           // For statistics visualization
