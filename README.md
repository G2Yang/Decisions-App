<div align="center">

# 🎯 Decisions App

### An Android app to make choices easier, developed with Java and Firebase.

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
- [⚙️ Getting Started](#️-getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [📖 Usage](#-usage)
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
- **Language**:  Java
- **Architecture**: MVVM (Model-View-ViewModel)

### **Backend & Services**
- **Database**: Firebase Firestore (NoSQL, real-time)
- **Authentication**: Firebase Auth (Email/Password, Apple Sign-In)
- **Storage**: Firebase Storage (for user media)
- **Analytics**: Firebase Analytics

### **Development Tools**
- **IDE**: Android Studi
- **Version Control**: Git + GitHub
- **Design**: Figma (UI/UX prototyping)

## ⚙️ Getting Started
Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

## Prerequisites
macOS: Sonoma (14.0) or later

Xcode: 15.0 or later (download from Mac App Store)

iOS: 17.0+ target deployment

Apple Developer Account (for physical device testing)

Firebase Account (free tier available)

## Installation
1. Clone the Repository
bash
git clone https://github.com/G2Yang/Decisions-App.git
cd Decisions-App
2. Open in Xcode
bash
open Decisions.xcodeproj
# or if using a workspace
open Decisions.xcworkspace
3. Firebase Configuration
Step 1: Create Firebase Project

Go to Firebase Console

Click "Add Project" → Name it "Decisions-App"

Disable Google Analytics (optional)

Step 2: Register iOS App

Click the iOS icon (🏠) in project overview

Enter your bundle ID (check in Xcode: com.yourname.Decisions)

Download GoogleService-Info.plist

Step 3: Add Firebase to Xcode

Drag GoogleService-Info.plist to Xcode project root

Ensure "Copy items if needed" is checked

Add to all targets

Step 4: Enable Firebase Services
In Firebase Console, enable:

Authentication (Email/Password, Apple Sign-In)

Firestore Database (Start in test mode)

Storage (optional, for future features)

4. Build and Run
Select your target device/simulator (iPhone 15 Pro recommended)

Press ⌘ + R or click the Play button ▶️

For first run, you might need to trust the developer in Settings → General → Device Management

## 📖 Usage
Basic Workflow
Launch the app and sign in/create account

Create your first decision wheel:

Tap + button

Enter category name (e.g., "Dinner Options")

Add items (e.g., "Pizza", "Sushi", "Burgers")

Customize colors if desired

Make a decision:

Open your wheel

Tap "Spin" button

Wheel animates and selects random option

Option is saved to history

Advanced Features
Multi-select Wheels: Create wheels that can select multiple winners

Weighted Options: Assign probabilities to certain choices

Shared Wheels: Collaborate with friends on group decisions

Siri Shortcuts: "Hey Siri, decide what's for dinner"

## 📄 License
Distributed under the MIT License. See LICENSE file for more information.

text
MIT License

Copyright (c) 2024 Zhiyang Wu

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
## ✉️ Contact
Zhiyang Wu, Ismael, Cege - Movile Developer

GitHub: @G2Yang

Email: yangtrabajos@gmail.com
